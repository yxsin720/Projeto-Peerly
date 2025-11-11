package com.example.Peerly.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

@Composable
fun CriarContaScreen(navController: NavController) {
    val repo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var createdOk by remember { mutableStateOf(false) }

    fun submit() {
        error = null
        if (nome.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
            error = "Preenche todos os campos."
            return
        }
        if (password != confirm) {
            error = "As palavras-passe não coincidem."
            return
        }

        loading = true
        scope.launch {
            try {

                val created = repo.registerViaUsers(
                    fullName = nome.trim(),
                    email = email.trim(),
                    password = password             )


                val logged = repo.login(email.trim(), password)

                           UserSession.setUser(logged)

                createdOk = true
                     navController.navigate("home") {
                    popUpTo("criar_conta") { inclusive = true }
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                error = e.message ?: "Falha ao criar conta."
            } finally {
                loading = false
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color(0xFF4B39EF),
        focusedBorderColor = Color(0xFF4B39EF),
        unfocusedBorderColor = Color(0xFFBDBDBD),
        focusedLabelColor = Color(0xFF4B39EF),
        unfocusedLabelColor = Color(0xFF7A7A7A),
        focusedLeadingIconColor = Color(0xFF4B39EF),
        unfocusedLeadingIconColor = Color(0xFF7A7A7A)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF6C63FF), Color(0xFF4B39EF)))
            )
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Peerly",
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .size(220.dp)
            )

            OutlinedTextField(
                value = nome, onValueChange = { nome = it },
                label = { Text("Nome completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Palavra-passe") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = confirm, onValueChange = { confirm = it },
                label = { Text("Confirmar palavra-passe") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                colors = textFieldColors
            )

            if (error != null) {
                Text(error!!, color = Color(0xFFFFE082), fontSize = 13.sp)
            }
            if (createdOk) {
                Text("Conta criada com sucesso!", color = Color.White, fontSize = 13.sp)
            }

            Button(
                onClick = { if (!loading) submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A41B5)),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    if (loading) "A criar conta..." else "Criar conta",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f).height(1.dp)
                )
                Text(
                    "  OU  ",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f).height(1.dp)
                )
            }

            OutlinedButton(
                onClick = { /* Google SignUp (futuro) */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                enabled = !loading
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.icon_google),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Criar conta com Google", color = Color.Black, fontSize = 15.sp)
                }
            }

            OutlinedButton(
                onClick = { /* Facebook SignUp (futuro) */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                enabled = !loading
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.icon_facebook),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Criar conta com Facebook", color = Color.Black, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Já tens conta? Entrar",
                color = Color.White,
                modifier = Modifier.clickable(enabled = !loading) { navController.popBackStack() }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
private fun CriarContaPreviewPreview() {
    MyApplicationPeerly4Theme {
        CriarContaScreen(navController = rememberNavController())
    }
}