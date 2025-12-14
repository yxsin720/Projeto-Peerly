package com.example.Peerly.data

import androidx.annotation.DrawableRes
import com.example.Peerly.R

data class StaticTutor(
    val id: String,
    val name: String,
    val subject: String,
    @DrawableRes val avatarRes: Int
)

object StaticTutors {
    val list = listOf(
        StaticTutor(
            id = "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
            name = "Pedro Almeida",
            subject = "Matemática",
            avatarRes = R.drawable.pedro
        ),
        StaticTutor(
            id = "2d23944a-bc00-11f0-a9b0-c4efbbb92864",
            name = "Erica Santos",
            subject = "Programação",
            avatarRes = R.drawable.rita
        ),
        StaticTutor(
            id = "baddd584-b456-11f0-95be-c4efbbb92864",
            name = "Ana Rita",
            subject = "Design",
            avatarRes = R.drawable.rita
        ),

    )
}
