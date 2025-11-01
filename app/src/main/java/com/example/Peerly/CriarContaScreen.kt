package com.example.Peerly

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
// --- IMPORT CORRIGIDO ---
import com.example.Peerly.ui.theme.PeerlyTheme

@Composable
fun CriarContaScreen(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                modifier = Modifier.height(40.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AccountTextField(value = nome, onValueChange = { nome = it }, label = "Nome completo", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(16.dp))
            AccountTextField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email)
            Spacer(modifier = Modifier.height(16.dp))
            AccountTextField(value = password, onValueChange = { password = it }, label = "Palavra-passe", icon = Icons.Default.Lock, isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            AccountTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar palavra-passe", icon = Icons.Default.Lock, isPassword = true)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: Lógica de criar conta */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A41B5))
            ) {
                Text("Criar conta", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
                Text(" OU ", color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            SocialButton(iconRes = R.drawable.icon_google, text = "Criar conta com Google") { /* TODO */ }
            Spacer(modifier = Modifier.height(16.dp))
            SocialButton(iconRes = R.drawable.icon_facebook, text = "Criar conta com Facebook") { /* TODO */ }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Já tens conta? Entrar",
                color = Color.White,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}

@Composable
private fun AccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFF4B39EF),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color(0xFF4B39EF),
            unfocusedLabelColor = Color.Gray,
            focusedLeadingIconColor = Color(0xFF4B39EF),
            unfocusedLeadingIconColor = Color.Gray
        )
    )
}

@Composable
private fun SocialButton(iconRes: Int, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        border = null
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Black, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun CriarContaScreenPreview() {
    // --- TEMA CORRIGIDO ---
    PeerlyTheme {
        CriarContaScreen(navController = rememberNavController())
    }
}
