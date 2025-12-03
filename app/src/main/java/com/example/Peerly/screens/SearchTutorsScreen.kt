package com.example.Peerly.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.Peerly.R
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme


private val searchSeedTutors = listOf(
    Tutor(
        id = "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
        name = "Pedro Almeida",
        subject = "MATEMÁTICA",
        description = "Professor dedicado e paciente...",
        rating = 4.9,
        reviews = 128,
        imageResId = R.drawable.pedroalmeida
    ),
    Tutor(
        id = "2d23944a-bc00-11f0-a9b0-c4efbbb92864",
        name = "Erica Santos",
        subject = "PROGRAMAÇÃO",
        description = "Engenheira de software...",
        rating = 4.8,
        reviews = 96,
        imageResId = R.drawable.ericasantos
    ),
    Tutor(
        id = "baddd584-b456-11f0-95be-c4efbbb92864",
        name = "Rita Fernandes",
        subject = "DESIGN",
        description = "Designer gráfica...",
        rating = 4.9,
        reviews = 100,
        imageResId = R.drawable.ritafernandes
    ),
    Tutor(
        id = "bade1da4-b456-11f0-95be-c4efbbb92864",
        name = "João Silva",
        subject = "INGLÊS",
        description = "Apaixonado por línguas...",
        rating = 4.7,
        reviews = 81,
        imageResId = R.drawable.joaosilva
    )
)



@Composable
fun SearchTutorsScreen(navController: NavController) {

    val ctx = LocalContext.current

    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(0) }

    val photoMap = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(Unit) {
        searchSeedTutors.forEach { t ->
            readTutorPhoto(ctx, t.id)?.let { saved ->
                photoMap[t.id] = saved
            }
        }
    }

    val categories = listOf("Todas", "Matemática", "Programação", "Design", "Inglês")

    val filtered = searchSeedTutors.filter { tutor ->
        val categoryOk =
            selectedCategory == 0 || tutor.subject.contains(categories[selectedCategory], ignoreCase = true)

        val textOk =
            query.isBlank() ||
                    tutor.name.lowercase().contains(query.lowercase()) ||
                    tutor.subject.lowercase().contains(query.lowercase())

        categoryOk && textOk
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Procurar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(22.dp))


        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color(0xFF5C54ED)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = null,
                    tint = Color(0xFF5C54ED)
                )
            },
            placeholder = {
                Text(
                    "Procurar tutor, disciplina...",
                    color = Color(0xFF5C54ED).copy(alpha = 0.6f)
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
                cursorColor = Color(0xFF5C54ED)
            )
        )

        Spacer(Modifier.height(18.dp))


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.size) { index ->
                val selected = selectedCategory == index
                val bg = if (selected) Color.White else Color(0xFF7A6BFF)
                val fg = if (selected) Color(0xFF141414) else Color.White

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bg)
                        .clickable { selectedCategory = index }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(categories[index], color = fg, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(20.dp))


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filtered) { tutor ->
                TutorResultCard(
                    tutor = tutor,
                    photoUrlOrUri = photoMap[tutor.id],
                    onDetails = {
                        navController.navigate(
                            "info_tutor/${Uri.encode(tutor.id)}/${Uri.encode(tutor.name)}"
                                    + "?subject=${Uri.encode(tutor.subject)}"
                                    + "&desc=${Uri.encode(tutor.description)}"
                                    + "&rating=${tutor.rating}"
                                    + "&reviews=${tutor.reviews}"
                        )
                    }
                )
            }
        }
    }
}



@Composable
private fun TutorResultCard(
    tutor: Tutor,
    photoUrlOrUri: String?,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            ) {
                if (photoUrlOrUri != null) {
                    AsyncImage(
                        model = photoUrlOrUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(tutor.imageResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }


            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {

                Text(
                    tutor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF141414)
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    tutor.subject,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7A6BFF)
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        "${tutor.rating}  ·  ${tutor.reviews} avaliações",
                        color = Color(0xFF777777),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF5C54ED))
                    .clickable { onDetails() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Detalhes", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
private fun PreviewSearch() {
    MyApplicationPeerly4Theme {
        SearchTutorsScreen(navController = rememberNavController())
    }
}
