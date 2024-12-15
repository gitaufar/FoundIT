import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foundit.R
import com.example.foundit.data.Database
import com.example.foundit.ui.theme.poppinsFamily
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Stable
data class PasswordOutputTransformation(private val pad: Boolean) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        //ubah
        if (pad) {
            // Pad the text with placeholder chars if too short.
            // (___) ___-____
            val padCount = 10 - length
            repeat(padCount) { append('_') }
        }

        // (123) 456-7890
        if (length > 0) insert(0, "(")
        if (length > 4) insert(4, ") ")
        if (length > 9) insert(9, "-")
    }
}


@SuppressLint("ShowToast")
@Preview
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {

    val userText = remember { TextFieldState("") }
    val passText = remember { TextFieldState("") }
    var text by remember { mutableStateOf("") }
    var isPassVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            painter = painterResource(id = R.drawable.ilustrasi_login),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(color = Color(0xFFE6ECF8))
                .padding(start = 31.dp, top = 42.dp, end = 31.dp)
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            BasicTextField(
                state = userText,
                decorator = { innerText ->
                    Row(
                        modifier = Modifier
                            .size(350.dp, 40.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(color = Color.White)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp, 16.dp),
                            painter = painterResource(R.drawable.user_icon),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            if (userText.text.isEmpty()) {
                                Text(
                                    text = "Username",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerText()
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            BasicTextField(
                state = passText,
                decorator = { innerText ->
                    Row(
                        modifier = Modifier
                            .size(350.dp, 40.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(color = Color.White)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp, 20.dp),
                            painter = painterResource(R.drawable.pass_icon),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (passText.text.isEmpty()) {
                                Text(
                                    text = "Password",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerText()
                        }
                        Icon(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(16.dp)
                                .height(16.dp),
                            painter = painterResource(id = R.drawable.hide_pass),
                            contentDescription = "image description",
                            tint = Color(0xFF6F6F6F)
                        )
                    }
                },
//                outputTransformation = PasswordOutputTransformation(false)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Switch(
                    checked = false,
                    onCheckedChange = {

                    }
                )
                Text(
                    text = "Ingat saya",
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 23.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Lupa password?",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF5E36D4),
                    )
                )
            }
            Spacer(modifier = Modifier.size(32.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = {
                    coroutineScope.launch {
                        val loginResult = Database().login(
                            username = userText.text.toString().trim(),
                            password = passText.text.toString().trim()
                        )
                        if (loginResult) {
                            navController!!.navigate("home_screen")
                        } else {
                            Toast.makeText(context,"akun tidak ditemukan, periksa kembali username dan password",Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF2C599D)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Masuk",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(Color(0x121E1E1E))
                )
                Text(
                    text = "Atau",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color(0x121E1E1E))
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF2C599D)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color(0xFFFAFAFA),
                            shape = RoundedCornerShape(size = 30.dp)
                        )
                        .width(30.dp)
                        .height(30.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(0.dp)
                            .width(18.00009.dp)
                            .height(18.00027.dp),
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillBounds
                    )
                }
                Spacer(modifier = Modifier.size(7.dp))
                Text(
                    text = "Masuk dengan Google",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Belum punya akun?",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    modifier = Modifier
                        .clickable {
                            navController!!.navigate("register_screen")
                        },
                    text = " Register",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF5E36D4),
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}