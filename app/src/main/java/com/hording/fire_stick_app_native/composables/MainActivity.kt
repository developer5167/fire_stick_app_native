package com.hording.fire_stick_app_native.composables

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.models.ActivationResponse
import com.hording.fire_stick_app_native.ui.theme.FirestickappnativeTheme
import com.hording.fire_stick_app_native.viewModels.MainActivityViewMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation(mainActivityViewMode: MainActivityViewMode = hiltViewModel()) {
    val navController = rememberNavController()
    val activationResponse = mainActivityViewMode.activationResponse.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "decider"
    ) {
        composable("home") {
            HomeScreen(
                mainActivityViewMode,
                state = activationResponse.value,
                onSuccess = {
                    navController.navigate("adsPlayCompose")
                }
            )
        }
        composable("adsPlayCompose") {
            AdsPlayCompose()
        }
        composable("decider") {
            val token by mainActivityViewMode.token.observeAsState()

            LaunchedEffect(token) {
                when {
                    token == null -> {
                        // Do nothing, still loading DataStore
                    }
                    token.isNullOrEmpty() -> {
                        navController.navigate("home") {
                            popUpTo("decider") { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate("adsPlayCompose") {
                            popUpTo("decider") { inclusive = true }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun HomeScreen(


    mainActivityViewMode: MainActivityViewMode,
    state: ApiResponse<ActivationResponse>,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        when (state) {
            is ApiResponse.Success -> {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                onSuccess()
            }

            is ApiResponse.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
    if (state is ApiResponse.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val activationCode = remember { mutableStateOf("RH7HG7") }
    val email = remember { mutableStateOf("kruparao.g@kapilit.com") }
    val password = remember { mutableStateOf("djdd9ddg") }
    val showPassword = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        OutlinedTextField(

            label = { Text("Enter Activation Code",color=Color.Black) },
            value = activationCode.value,
            onValueChange = {
                activationCode.value = it
                mainActivityViewMode.activationRequest.activation_code = it
            },
        )
        OutlinedTextField(
            label = { Text("Enter Email") },
            value = email.value,
            onValueChange = {
                email.value = it
                mainActivityViewMode.activationRequest.email = it
            },
        )
        OutlinedTextField(
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text("Enter Password") },
            value = password.value,
            onValueChange = {
                password.value = it
                mainActivityViewMode.activationRequest.password = it
            },
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        imageVector = if (showPassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
        )
        Button(onClick = { mainActivityViewMode.activateDevice() }) {
            Text("Activate",color=Color.White)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirestickappnativeTheme {

    }
}