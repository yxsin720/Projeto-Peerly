package com.example.Peerly.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.Peerly.R
import com.example.Peerly.data.AuthRepository
import com.example.Peerly.session.UserSession
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private enum class DialogType { WrongPassword, Unregistered, Generic }

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf(DialogType.Generic) }

    val scope = rememberCoroutineScope()
    val repo = remember { AuthRepository() }

    fun doLogin() {
        if (email.isBlank() || password.isBlank()) {
            dialogTitle = "Campos em falta"
            dialogMessage = "Preenche o email e a palavra-passe."
            dialogType = DialogType.Generic
            showDialog = true
            return
        }

        isLoading = true
        scope.launch {
            try {
                val user = repo.login(email.trim(), password)
                UserSession.setUser(user)
                isLoading = false
                navController.navigate("welcome") {
                    popUpTo("login") { inclusive = true }
                }
            } catch (e: HttpException) {
                isLoading = false
                if (e.code() == 401) {
                    val exists = try { repo.emailExists(email.trim()) } catch (_: Exception) { false }
                    if (exists) {
                        dialogTitle = "Credenciais erradas"
                        dialogMessage = "O email existe, mas a palavra-passe está incorreta."
                        dialogType = DialogType.WrongPassword
                    } else {
                        dialogTitle = "Utilizador sem registo"
                        dialogMessage = "Este email ainda não está registado. Queres criar conta?"
                        dialogType = DialogType.Unregistered
                    }
                } else {
                    dialogTitle = "Erro ${e.code()}"
                    dialogMessage = "Não foi possível entrar agora. Tenta novamente."
                    dialogType = DialogType.Generic
                }
                showDialog = true
            } catch (_: IOException) {
                isLoading = false
                dialogTitle = "Sem ligação"
                dialogMessage = "Verifica a tua ligação à internet e tenta novamente."
                dialogType = DialogType.Generic
                showDialog = true
            } catch (ex: Exception) {
                isLoading = false
                dialogTitle = "Ocorreu um erro"
                dialogMessage = ex.message ?: "Tenta novamente."
                dialogType = DialogType.Generic
                showDialog = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6C63FF), Color(0xFF4B39EF))
                )
            )
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Logo Peerly",
                modifier = Modifier.size(180.dp) // aumentado
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF4B39EF),
                    focusedBorderColor = Color(0xFF4B39EF),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF4B39EF),
                    unfocusedLabelColor = Color.Gray,
                    focusedLeadingIconColor = Color(0xFF4B39EF),
                    unfocusedLeadingIconColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Palavra-passe") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        if (passwordVisible)
                            Icon(Icons.Default.VisibilityOff, contentDescription = "Esconder")
                        else
                            Icon(Icons.Default.Visibility, contentDescription = "Mostrar")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF4B39EF),
                    focusedBorderColor = Color(0xFF4B39EF),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF4B39EF),
                    unfocusedLabelColor = Color.Gray,
                    focusedLeadingIconColor = Color(0xFF4B39EF),
                    unfocusedLeadingIconColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = { if (!isLoading) doLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B39EF))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("ENTRAR", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Esqueceste a palavra-passe?",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { /* TODO */ }
                )
                Text(
                    text = "Não tens conta? Criar conta",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { navController.navigate("criar_conta") }
                )
            }


            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.4f),
                    thickness = 1.dp
                )
                Text(
                    text = "  OU  ",
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.4f),
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            OutlinedButton(
                onClick = { /* TODO Google */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_google),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Entrar com Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { /* TODO Facebook */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_facebook),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Entrar com Facebook", color = Color.Black)
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(dialogTitle, fontWeight = FontWeight.Bold, color = Color(0xFF4B39EF))
            },
            text = {
                Text(dialogMessage, color = Color.Black.copy(alpha = 0.85f))
            },
            confirmButton = {
                when (dialogType) {
                    DialogType.Unregistered -> {
                        TextButton(onClick = {
                            showDialog = false
                            navController.navigate("criar_conta")
                        }) { Text("Criar conta", color = Color(0xFF4B39EF), fontWeight = FontWeight.Bold) }
                    }
                    else -> {
                        TextButton(onClick = { showDialog = false }) {
                            Text("OK", color = Color(0xFF4B39EF), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            dismissButton = {
                if (dialogType == DialogType.Unregistered) {
                    TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = Color.Gray) }
                }
            },
            shape = RoundedCornerShape(12.dp),
            containerColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyApplicationPeerly4Theme {
        LoginScreen(navController = rememberNavController())
    }
}
