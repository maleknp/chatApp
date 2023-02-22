package com.example.chatapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.destinations.ChatRoomScreenDestination
import com.example.chatapp.destinations.LoginScreenDestination
import com.example.chatapp.destinations.SignUpScreenDestination
import com.example.chatapp.destinations.UserScreenDestination
import com.example.chatapp.ui.LoginViewModel
import com.example.chatapp.ui.MessageViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
//        FirebaseApp.initializeApp(this)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }

    }

    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
    }

}

@Composable
@Destination
fun SignUpScreen(nav: DestinationsNavigator,
                 viewModel: LoginViewModel= hiltViewModel(),
                 m: MessageViewModel= hiltViewModel()
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var value by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    Image(
        modifier = Modifier.fillMaxWidth(),
        painter =  painterResource(R.drawable.rectangle_8),
        contentDescription = null
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .height(615.dp),
            shape = RoundedCornerShape(60.dp,0.dp,0.dp,0.dp)
        ) {
            Card(modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFFFFFF)),) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                        Spacer(modifier = Modifier.size(20.dp))

                            Text(
                                text = "Sign Up", color = Color(0xFFFFA925),
                                fontSize = 36.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold, modifier = Modifier.padding(0.dp,30.dp,0.dp,0.dp)
                            )

                        Spacer(modifier = Modifier.size(70.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {
                            OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email, onValueChange = { email = it },
                            textStyle = TextStyle.Default.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFFFA925),
                                unfocusedBorderColor = Color.White,
                                backgroundColor = Color(0xFFF5F5F5)
                            ),

                            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = Color(0xFFC5C5C7)) },

                            placeholder = {
                                Text(text = "Email", fontSize = 14.sp, color = Color(0xFFcccccc))
                            },
                            singleLine = true,
                        )
                        }


                        Spacer(modifier = Modifier.size(30.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = password, onValueChange = { password = it },
                                textStyle = TextStyle.Default.copy(
                                    fontSize = 14.sp,
                                    color = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFFFFA925),
                                    unfocusedBorderColor = Color.White,
                                    backgroundColor = Color(0xFFF5F5F5)
                                ),

                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Lock Icon", tint = Color(0xFFC5C5C7)
                                    )
                                },


                                trailingIcon = {
                                    IconButton(onClick = { showPassword = !showPassword }) {
                                        Icon(
                                            painter = if (showPassword) painterResource(R.drawable.vector__4_) else painterResource(R.drawable.vector__5_),
                                            contentDescription = if (showPassword) "Show Password" else "Hide Password"
                                        )
                                    }
                                },
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),

                                placeholder = {
                                    Text(
                                        text = "Password",
                                        fontSize = 14.sp,
                                        color = Color(0xFFE5E5E5)
                                    )
                                },
                                singleLine = true,
                            )
                        }

                        Spacer(modifier = Modifier.size(170.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {
                             val context = LocalContext.current
                            Button(
                                onClick = {
                                    viewModel.signUp(email,password)
                                    Toast.makeText(context,"the SignUp is successful",Toast.LENGTH_SHORT).show()
                                    nav.navigate(LoginScreenDestination)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA925))
                            ) {
                                Text(
                                    "Sign Up",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(0.dp,5.dp,0.dp,5.dp),
                                    color = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
                                )
                            }

                        }

                        Spacer(modifier = Modifier.size(5.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {

                            Text(
                                "Do you have a account?",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp),
                                color = androidx.compose.ui.graphics.Color.Black
                            )

                            TextButton(onClick = { nav.navigate(LoginScreenDestination) },colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFA925)
                            )) {
                                Text(
                                    "Login here",
                                    fontSize = 12.sp,
                                    color = androidx.compose.ui.graphics.Color(0xFFFFA925)
                                )
                            }

                        }

                    }


            }
        }
    }
}


@Composable
@Destination(start = true)
fun LoginScreen(nav: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var value by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    Image(
        modifier = Modifier.fillMaxWidth(),
        painter =  painterResource(R.drawable.rectangle_8),
        contentDescription = null
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .height(615.dp),
            shape = RoundedCornerShape(60.dp,0.dp,0.dp,0.dp)
        ) {
            Card(modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFFFFFF)),) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "Log In", color = Color(0xFFFFA925),
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold, modifier = Modifier.padding(0.dp,30.dp,0.dp,0.dp)
                    )

                    Spacer(modifier = Modifier.size(70.dp))

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                             value = email, onValueChange = { email = it }
                            ,
                            textStyle = TextStyle.Default.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFFFA925),
                                unfocusedBorderColor = Color.White,
                                backgroundColor = Color(0xFFF5F5F5)
                            ),

                            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = Color(0xFFC5C5C7)) },

                            placeholder = {
                                Text(text = "Email", fontSize = 14.sp, color = Color(0xFFE5E5E5))
                            },
                            singleLine = true,
                        )
                    }


                    Spacer(modifier = Modifier.size(30.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password, onValueChange = { password = it },
                            textStyle = TextStyle.Default.copy(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFFFA925),
                                unfocusedBorderColor = Color.White,
                                backgroundColor = Color(0xFFF5F5F5)
                            ),

                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock Icon", tint = Color(0xFFC5C5C7)
                                )
                            },


                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        painter = if (showPassword) painterResource(R.drawable.vector__4_) else painterResource(R.drawable.vector__5_),
                                        contentDescription = if (showPassword) "Show Password" else "Hide Password"
                                    )
                                }
                            },
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),

                            placeholder = {
                                Text(
                                    text = "Password",
                                    fontSize = 14.sp,
                                    color = Color(0xFFE5E5E5)
                                )
                            },
                            singleLine = true,
                        )
                    }
                    
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = {  }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFA925)
                        ),) {
                            Text(
                                "Forgot password",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color(0xFFFFA925)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(120.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {

                        Button(
                            onClick = {
                                viewModel.logIn(email,password)

                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA925))
                        ) {
                            Text(
                                "Log In",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(0.dp,5.dp,0.dp,5.dp),
                                color = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
                            )
                        }

                    }
                    val context = LocalContext.current

                    if(viewModel.isLogin.value){
                        Toast.makeText(context,"the Login is successful",Toast.LENGTH_SHORT).show()
                        viewModel.isLogin.value=false
                        nav.navigate(UserScreenDestination)
                    }


                    Spacer(modifier = Modifier.size(5.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 30.dp, 0.dp), horizontalArrangement = Arrangement.Center) {

                        Text(
                            "Do you have a account?",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp),
                            color = androidx.compose.ui.graphics.Color.Black
                        )

                        TextButton(onClick = { nav.navigate(SignUpScreenDestination) }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFA925)
                        ),) {
                            Text(
                                "Sign Up here",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color(0xFFFFA925)
                            )
                        }

                    }

                }


            }
        }
    }
}


@Composable
@Destination
fun ChatRoomScreen(
    nav: DestinationsNavigator,
    messageViewModel: MessageViewModel= hiltViewModel()
    ) {

    var sendMessage by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column() {
                Row() {
                    Column() {
                        Button(
                            onClick = {
                                 nav.navigate(UserScreenDestination)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFFAFAFA),
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
                                .width(48.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                            contentPadding = PaddingValues(0.dp, 10.dp, 0.dp, 10.dp)
                        ) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            Icon(
                                painter = painterResource(R.drawable.vector__6_),
                                tint = Color(0xFFA3A3A3),
                                contentDescription = "Back"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))
                    Box() {
                        Image(
                            painter = painterResource(R.drawable.unsplash_gkxkby_c_dk),
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp),
                            contentDescription = "Profile Pic"
                        )
                        Icon(
                            modifier = Modifier
                                .padding(40.dp, 0.dp, 0.dp, 0.dp)
                                .width(10.dp)
                                .height(10.dp),
                            painter = painterResource(R.drawable.unsplash_tn8dlxwudma),
                            tint = Color(0xFF22C55E),
                            contentDescription = "Online"
                        )
                    }

                    Spacer(modifier = Modifier.size(15.dp, 15.dp))
                    Column(modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp)) {
                        Text(
                            "Alberto Moedano",
                            fontSize = 14.sp,
                            color = androidx.compose.ui.graphics.Color.Black
                        )
                        Text(
                            "Online now",
                            fontSize = 12.sp,
                            color = androidx.compose.ui.graphics.Color(0xFFFFA925)
                        )
                    }

                }


            }




            Column() {
                Row() {

                    Column() {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFFAFAFA),
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
                                .width(48.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                            contentPadding = PaddingValues(0.dp, 10.dp, 0.dp, 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.vector__7_),
                                tint = Color(0xFFA3A3A3),
                                contentDescription = "Call"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp, 10.dp))
                    Column() {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFFAFAFA),
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
                                .width(48.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.vector__8_),
                                tint = Color(0xFFA3A3A3),
                                contentDescription = "Video"
                            )
                        }
                    }
                }
            }
        }


        val messages = messageViewModel.messages



        LazyColumn(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .weight(1f)
                .fillMaxSize()
        ) {

            items(messages.size) { message ->
                if (messageViewModel.checkMe(messages[message].email.toString())) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            modifier = Modifier
//                        .selectable(
//                            selected = message.id == selectedItem,
//                            onClick = { nav.navigate(showNoteDestination(message))
//                            })
                                .padding(5.dp),
                            backgroundColor = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row() {
                                    Text(text = messages[message].message, fontSize = 12.sp)
                                }

                            }
                        }

                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Card(
                            modifier = Modifier
//                        .selectable(
//                            selected = message.id == selectedItem,
//                            onClick = { nav.navigate(showNoteDestination(message))
//                            })
                                .padding(5.dp),
                            backgroundColor = Color(0xFFFFA925),
                            shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 20.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row() {
                                    Text(text = messages[message].message, fontSize = 12.sp)
                                }

                            }
                        }

                    }
                }
            }
        }











        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA)),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row() {


                    Column() {
                        Row() {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = sendMessage, onValueChange = { sendMessage = it },
                                textStyle = TextStyle.Default.copy(
                                    fontSize = 14.sp,
                                    color = androidx.compose.ui.graphics.Color.Black
                                ),

                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Gray,
                                    disabledTextColor = Color.Transparent,
                                    backgroundColor = Color(0xFFFAFAFA),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Type something..",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9A9A9A),
                                        modifier = Modifier.padding(5.dp,0.dp,0.dp,0.dp)
                                    )
                                },
                                leadingIcon = {
                                    Button(
                                    onClick = {

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFF5F5F5),
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    ),
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                                    contentPadding = PaddingValues(10.dp)
                                ) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                    Icon(
                                        painter = painterResource(R.drawable.vector__9_),
                                        tint = Color(0xFFFFA925),
                                        contentDescription = "Plus"
                                    )
                                }
                                              },

                                trailingIcon = {
                            Button(
                                onClick = {
                                          messageViewModel.sendMessage("hi",sendMessage)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFFF5F5F5),
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ),
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.vector__10_),
                                    tint = Color(0xFFFFA925),
                                    contentDescription = "Send"
                                )
                            }
                                },
//                            singleLine = true,
                            )

                        }

                    }



                }






//                Column() {
//                    Row() {
//
//                        Column() {
//                            Button(
//                                onClick = { },
//                                colors = ButtonDefaults.buttonColors(
//                                    backgroundColor = Color(0xFFF5F5F5),
//                                    contentColor = androidx.compose.ui.graphics.Color.White
//                                ),
//                                modifier = Modifier
//                                    .align(Alignment.End)
//                                    .width(48.dp)
//                                    .height(48.dp),
//                                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
//                                contentPadding = PaddingValues(10.dp)
//                            ) {
//                                Icon(
//                                    painter = painterResource(R.drawable.vector__10_),
//                                    tint = Color(0xFFFFA925),
//                                    contentDescription = "Send"
//                                )
//                            }
//                        }
//                    }
//                }



            }

        }
    }


}


@Composable
@Destination
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun UserScreen(nav: DestinationsNavigator){

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                          nav.navigate(ChatRoomScreenDestination)
                },
                contentColor = Color(0xFFFFA925),
                backgroundColor = Color(0xFFFFA925),

//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color(0xFFFFA925),
//                    contentColor = androidx.compose.ui.graphics.Color.White
//                ),
                modifier = Modifier
                    .size(48.dp, 48.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.frame),
                    contentDescription = "Chat"
                )            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,

        ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .padding(10.dp, 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = {
//                            nav.navigate(searchNoteDestination)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFAFAFA),
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                        modifier = Modifier
//                .align(Alignment.End)
                            .size(48.dp, 48.dp),
                        shape = RoundedCornerShape(15.dp),
                        contentPadding = PaddingValues(0.dp, 10.dp, 0.dp, 10.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xffa3a3a3))
                    }

                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Home",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,

                        color = androidx.compose.ui.graphics.Color.Black,
                    )

                    Column() {
                        Row(
                        ) {

                            Column() {
                                Button(
                                    onClick = {
//                            nav.navigate(searchNoteDestination)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFFfffff),
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(48.dp, 48.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.unsplash_tn8dlxwudma),
                                        contentDescription = "User"
                                    )
                                }
                            }

                        }

                    }
                }

//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(20.dp), horizontalArrangement = Arrangement.Center) {
//            Card(modifier = Modifier
//                .size(375.dp, 80.dp)
//                ) {
//            Row(modifier = Modifier.fillMaxSize()) {
//             Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center,
//                 modifier = Modifier
//                     .padding(10.dp)
//                     .align(alignment = Alignment.CenterVertically)) {
//               Image(modifier = Modifier.size(48.dp,48.dp),painter = painterResource(R.drawable.unsplash_tn8dlxwudma__1_), contentDescription = "user" )
//             }
//                Column( modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
//                    Text(text = "Alberto dwdfw", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
//                    Text(text = "say any thing ...", fontSize = 10.sp, color = Color(0xffFFA925))
//                }
//                Spacer(modifier = Modifier.width(130.dp))
//
//                Column( horizontalAlignment = Alignment.End,modifier = Modifier
//                    .align(alignment = Alignment.CenterVertically)
//                    .fillMaxWidth()
//                    .padding(5.dp)) {
//                  Text(textAlign = TextAlign.Center,text = " 2 min ago ", fontSize = 10.sp,)
//                    Spacer(modifier = Modifier.height(5.dp))
//                  Card(shape = RoundedCornerShape(10.dp),modifier = Modifier.size(16.dp,16.dp), backgroundColor = Color(0xffFFA925)) {
//                      Text(textAlign = TextAlign.Center,text = "1", fontSize = 10.sp, color = Color(0xffffffff))
//                  }
//
//              }
//            }
//            }
//        }
                Box() {


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {


                        // Add 5 items
                        items(8) { index ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp), horizontalArrangement = Arrangement.Center
                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(375.dp, 80.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        Column(
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .align(alignment = Alignment.CenterVertically)
                                        ) {
                                            Image(
                                                modifier = Modifier.size(48.dp, 48.dp),
                                                painter = painterResource(R.drawable.unsplash_tn8dlxwudma),
                                                contentDescription = "user"
                                            )
                                        }
                                        Column(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                                            Text(
                                                text = "Alberto dwdfw",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "say any thing ...",

                                                fontSize = 10.sp,
                                                color = Color(0xffFFA925)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(130.dp))

                                        Column(
                                            horizontalAlignment = Alignment.End, modifier = Modifier
                                                .align(alignment = Alignment.CenterVertically)
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Text(
                                                textAlign = TextAlign.Center,
                                                text = " 2 min ago ",
                                                fontSize = 10.sp,
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Card(
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.size(16.dp, 16.dp),
                                                backgroundColor = Color(0xffFFA925)
                                            ) {
                                                Text(
                                                    textAlign = TextAlign.Center,
                                                    text = "1",
                                                    fontSize = 10.sp,
                                                    color = Color(0xffffffff)
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }


                    }


//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(0.dp, 0.dp, 30.dp, 50.dp),
//                        verticalAlignment = Alignment.Bottom,
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        Button(
//                            onClick = {
////                            nav.navigate(searchNoteDestination)
//                            },
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = Color(0xFFFFA925),
//                                contentColor = androidx.compose.ui.graphics.Color.White
//                            ),
//                            modifier = Modifier
//                                .size(48.dp, 48.dp),
//                            shape = RoundedCornerShape(40.dp),
//                            contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 0.dp)
//                        ) {
//                            Image(
//                                painter = painterResource(R.drawable.frame__1_),
//                                contentDescription = "Chat"
//                            )
//                        }
//                    }
                }
            }        }
    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatAppTheme {
        Greeting("Android")
    }
}
