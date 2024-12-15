package com.example.foundit.data

import android.util.Log
import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Status
import com.example.foundit.enumclass.Tipe
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Database {
    private val dbUser = "sa"
    private val dbPassword = "aufar180505"

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun connectToDatabase(): Connection? {
        return suspendCoroutine { continuation ->
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // Menyiapkan driver jTDS
                    Class.forName("net.sourceforge.jtds.jdbc.Driver")

                    // URL koneksi dengan jTDS
                    val connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://192.168.1.5:1433/foundit_fix",
                        dbUser,
                        dbPassword
                    )

                    // Mengembalikan koneksi jika berhasil
                    continuation.resume(connection)
                    Log.d("DatabaseConnection", "Koneksi berhasil ke database.")
                } catch (e: Exception) {
                    e.printStackTrace()
                    continuation.resumeWithException(e)
                }
            }
        }
    }

    suspend fun login(username: String, password: String): Boolean {

        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            connection?.let { conn ->
                val statement = conn.createStatement()
                val result =
                    statement.executeQuery("SELECT nama,id_user,email,no_telepon FROM [User] WHERE nama = '$username' AND password = '$password'")
                if (result.next()) {
                    User.userName = result.getString("nama")
                    User.id = result.getInt("id_user")
                    User.email = result.getString("email")
                    User.nomor_telepon = result.getString("no_telepon")
                    return@withContext true
                }
            }
            return@withContext false
        }
    }

    suspend fun register(username: String, password: String, email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            connection?.let { conn ->
                val statement = conn.createStatement()
                try {
                    // Gunakan executeUpdate untuk query INSERT
                    val rowsAffected = statement.executeUpdate(
                        "INSERT INTO [User] (nama, password, email, is_register) " +
                                "VALUES ('$username', '$password', '$email', 1)"
                    )
                    return@withContext rowsAffected > 0 // True jika ada baris yang dimasukkan
                } catch (e: Exception) {
                    e.printStackTrace() // Cetak error jika terjadi masalah
                    return@withContext false
                } finally {
                    statement.close() // Tutup statement
                    conn.close() // Tutup koneksi
                }
            }
            return@withContext false
        }
    }


    suspend fun getBarangData(): List<Barang> {

        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            val barangList = mutableListOf<Barang>()

            connection?.let { conn ->
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM Barang")

                // Parsing hasil query menjadi objek Barang
                while (resultSet.next()) {
                    val barang = Barang(
                        resultSet.getInt("id_barang"),
                        Categories.valueOf(resultSet.getString("kategori")),
                        Tipe.valueOf(resultSet.getString("tipe_barang")),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("deskripsi"),
                        resultSet.getDate("tanggal"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("kontak_pelapor"),
                        resultSet.getString("foto_barang"),
                        Status.valueOf(resultSet.getString("status_barang")),
                        resultSet.getBytes("foto_barang_binary")
                    )
                    barangList.add(barang)
                }
            }
            return@withContext barangList
        }
    }

    suspend fun insertLaporan(laporan: Laporan, barang: Barang, tanggal_kirim: String): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            connection?.let { conn ->
                try {
                    // Query INSERT untuk tabel Laporan dengan pengambilan ID menggunakan SCOPE_IDENTITY
                    val laporanSql = """
                    INSERT INTO Laporan (id_user, barang_laporan, tipe_laporan, status_laporan, tanggal_laporan, tanggal_update)
                    VALUES (?, ?, ?, 'Pending', GETDATE(), GETDATE());
                    SELECT SCOPE_IDENTITY() AS NewId
                """.trimIndent()

                    conn.prepareStatement(laporanSql).use { laporanStatement ->
                        User.id?.let { laporanStatement.setInt(1, it) }
                        laporanStatement.setString(2, laporan.barang)
                        laporanStatement.setString(3, laporan.tipe_laporan.toString())

                        // Eksekusi dan ambil ID yang baru dibuat
                        val resultSet = laporanStatement.executeQuery()
                        var newLaporanId: Int? = null
                        if (resultSet.next()) {
                            newLaporanId = resultSet.getInt("NewId")
                        }

                        if (newLaporanId != null) {
                            // Query INSERT untuk tabel Barang menggunakan PreparedStatement
                            val barangSql = """
                            INSERT INTO Barang (nama_barang, tipe_barang, status_barang, deskripsi, kategori, lokasi, tanggal, kontak_pelapor, foto_barang_binary, id_laporan)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """.trimIndent()
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                            val dateParsed = dateFormat.parse(tanggal_kirim) // barang.tanggal berformat "2024-12-15 18:30"
                            val sqlTimestamp = Timestamp(dateParsed.time)
                            Log.d("tanggal","${sqlTimestamp}")

                            conn.prepareStatement(barangSql).use { barangStatement ->
                                barangStatement.setString(1, barang.nama)
                                barangStatement.setString(2, barang.tipe.toString())
                                barangStatement.setString(3, laporan.status_laporan.toString())
                                barangStatement.setString(4, barang.deskripsi)
                                barangStatement.setString(5, barang.kategori.toString())
                                barangStatement.setString(6, barang.lokasi)
                                barangStatement.setTimestamp(7, sqlTimestamp)
                                barangStatement.setString(8, barang.kontak)
                                barangStatement.setBytes(9, barang.foto_binary)
                                barangStatement.setInt(10, newLaporanId)

                                val rowsAffected = barangStatement.executeUpdate()
                                return@withContext rowsAffected > 0
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    conn.close()
                }
            }
            return@withContext false
        }
    }


    suspend fun getBarangHilangData(): List<Barang> {
        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            val barangList = mutableListOf<Barang>()

            connection?.let { conn ->
                val statement = conn.createStatement()
                val resultSet =
                    statement.executeQuery("SELECT * FROM Barang WHERE tipe_barang = 'LOST'")

                // Parsing the query result into Barang objects
                while (resultSet.next()) {
                    val barang = Barang(
                        resultSet.getInt("id_barang"),
                        Categories.valueOf(resultSet.getString("kategori")),
                        Tipe.valueOf(resultSet.getString("tipe_barang")),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("deskripsi"),
                        resultSet.getDate("tanggal"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("kontak_pelapor"),
                        resultSet.getString("foto_barang"),
                        Status.valueOf(resultSet.getString("status_barang")),
                        resultSet.getBytes("foto_barang_binary")
                    )
                    barangList.add(barang)
                }
            }

            return@withContext barangList
        }
    }

    suspend fun getBarangDataId(id: Int): Barang? {
        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            var barang: Barang? = null

            connection?.let { conn ->
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery(
                    "SELECT b.*, u.nama FROM Barang b " +
                            "INNER JOIN Laporan l ON b.id_laporan = l.id_laporan " +
                            "INNER JOIN [User] u ON l.id_user = u.id_user " +
                            "WHERE b.id_barang = $id"
                )

                if (resultSet.next()) {
                    barang = Barang(
                        resultSet.getInt("id_barang"),
                        Categories.valueOf(resultSet.getString("kategori")),
                        Tipe.valueOf(resultSet.getString("tipe_barang")),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("deskripsi"),
                        resultSet.getTimestamp("tanggal"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("kontak_pelapor"),
                        resultSet.getString("foto_barang"),
                        Status.valueOf(resultSet.getString("status_barang")),
                        resultSet.getBytes("foto_barang_binary"),
                        resultSet.getString("nama")
                    )
                }
            }

            return@withContext barang
        }
    }

    suspend fun getBarangTemuanData(): List<Barang> {
        // Use Dispatchers.IO for database connection
        return withContext(Dispatchers.IO) {
            val connection = connectToDatabase()
            val barangList = mutableListOf<Barang>()

            connection?.let { conn ->
                val statement = conn.createStatement()
                val resultSet =
                    statement.executeQuery("SELECT * FROM Barang WHERE tipe_barang = 'FOUND'")

                // Parsing the query result into Barang objects
                while (resultSet.next()) {
                    val barang = Barang(
                        resultSet.getInt("id_barang"),
                        Categories.valueOf(resultSet.getString("kategori")),
                        Tipe.valueOf(resultSet.getString("tipe_barang")),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("deskripsi"),
                        resultSet.getDate("tanggal"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("kontak_pelapor"),
                        resultSet.getString("foto_barang"),
                        Status.valueOf(resultSet.getString("status_barang")),
                        resultSet.getBytes("foto_barang_binary")
                    )
                    barangList.add(barang)
                }
            }

            return@withContext barangList
        }
    }
}