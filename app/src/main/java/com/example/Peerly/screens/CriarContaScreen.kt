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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Peerly.R

@Composable
fun CriarContaScreen(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    fun submit() {
        if (nome.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
            error = "Preenche todos os campos."
            return
        }
        if (password != confirm) {
            error = "As palavras-passe não coincidem."
            return
        }
        loading = false
        navController.navigate("welcome") { popUpTo("criar_conta") { inclusive = true } }
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
            .background(Brush.verticalGradient(listOf(Color(0xFF6C63FF), Color(0xFF4B39EF))))
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LOGO — maior e central
            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Peerly",
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .size(220.dp) // aumenta aqui se quiseres ainda maior
            )

            // Campos
            OutlinedTextField(
                value = nome, onValueChange = { nome = it },
                label = { Text("Nome completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
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
                colors = textFieldColors
            )

            // Erro (se houver)
            if (error != null) {
                Text(error!!, color = Color.White, fontSize = 12.sp)
            }

            // Botão Criar conta
            Button(
                onClick = { if (!loading) submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A41B5))
            ) {
                Text("Criar conta", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            // OU com barras
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

            // Google
            OutlinedButton(
                onClick = { /* TODO: Google SignUp */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
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

            // Facebook
            OutlinedButton(
                onClick = { /* TODO: Facebook SignUp */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
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

            // Link para Entrar
            Text(
                text = "Já tens conta? Entrar",
                color = Color.White,
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}
