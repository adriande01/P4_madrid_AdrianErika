package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p4_madrid_adrianerika.db.UserEntity
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel
import java.security.MessageDigest

fun sha256(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

@Composable
fun RegisterScreen(
    viewModel: ViewModel,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMsg = "Fill in all fields"
                    return@Button
                }
                val user = UserEntity(
                    id = username,
                    username = username,
                    passwordHash = sha256(password)
                )
                viewModel.registerUser(user, onSuccess = onNavigateToLogin, onError = { errorMsg = it })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}