package com.example.foundit.ui.screen

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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foundit.R
import com.example.foundit.ui.theme.poppinsFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.foundit.data.Database
import kotlinx.coroutines.launch

@Preview
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){

    val userText = remember { TextFieldState("") }
    val passText = remember { TextFieldState("") }
    val emailText = remember { TextFieldState("") }
    val confirmPassText = remember { TextFieldState("") }
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
            painter = painterResource(id = R.drawable.register_ilustrasi),
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
                text = "Register",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
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
                BasicTextField(
                    state = emailText,
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
                                painter = painterResource(R.drawable.email_icon),
                                contentDescription = "",
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.size(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                if (emailText.text.isEmpty()) {
                                    Text(
                                        text = "Email",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                innerText()
                            }
                        }
                    }
                )
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
                        }
                    },
//                outputTransformation = PasswordOutputTransformation(false)
                )

                BasicTextField(
                    state = confirmPassText,
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
                                if (confirmPassText.text.isEmpty()) {
                                    Text(
                                        text = "Confirm Password",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                innerText()
                            }
                        }
                    },
//                outputTransformation = PasswordOutputTransformation(false)
                )
                Spacer(modifier = Modifier.size(8.dp))
                val annotatedText = buildAnnotatedString {
                    pushStyle(SpanStyle(color = Color.Black))
                    append("Dengan mendaftar, Anda setuju dengan ")
                    pop()
                    pushStyle(SpanStyle(color = Color(0xFF5E36D4)))
                    append("Syarat & Ketentuan")
                    pop()
                    pushStyle(SpanStyle(color = Color.Black))
                    append(" dan Kebijakan Privasi kami")
                }
                Text(
                    text = annotatedText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(300)
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    onClick = {
                        coroutineScope.launch {
                            if(
                                userText.text.isEmpty() ||
                                passText.text.isEmpty() ||
                                emailText.text.isEmpty() ||
                                confirmPassText.text.toString() != passText.text.toString()
                            ){
                                Toast.makeText(context, "Username,email,password tidak boleh kosong dan pastikan confirm password sama dengan password",Toast.LENGTH_LONG).show()
                            } else {
                                val registerResult = Database().register(
                                    username = userText.text.toString(),
                                    password = passText.text.toString(),
                                    email = emailText.text.toString()
                                )
                                if (registerResult) {
                                    navController!!.navigate("login_screen")
                                    Toast.makeText(context,"Register berhasil",Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context,"Register gagal coba ulang beberapa saat",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF2C599D)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Daftar",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Sudah punya akun?",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF000000),
                        )
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                navController!!.navigate("login_screen")
                            },
                        text = " Login",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF5E36D4),
                        )
                    )
                }
            }
        }
    }
}
