package com.example.Peerly

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.Peerly.data.TutorRepository
import com.example.Peerly.screens.AgendarSessaoScreen
import com.example.Peerly.screens.ChatScreen
import com.example.Peerly.screens.CriarContaScreen
import com.example.Peerly.screens.HomeScreen
import com.example.Peerly.screens.InfoTutorScreen
import com.example.Peerly.screens.LoginScreen
import com.example.Peerly.screens.NextSessionScreen
import com.example.Peerly.screens.ReviewSessionScreen
import com.example.Peerly.screens.SearchTutorsScreen
import com.example.Peerly.screens.SplashScreen
import com.example.Peerly.screens.UserScreen
import com.example.Peerly.screens.WelcomeScreen
import com.example.Peerly.session.UserSession
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            MyApplicationPeerly4Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()
                    val tutorRepo = TutorRepository()

                    NavHost(
                        navController = nav,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onFinish = {
                                    val isLogged = UserSession.currentUser != null
                                    nav.navigate(if (isLogged) "welcome" else "login") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("login")       { LoginScreen(nav) }
                        composable("welcome")     { WelcomeScreen(nav) }
                        composable("criar_conta") { CriarContaScreen(nav) }
                        composable("home")        { HomeScreen(nav) }
                        composable("user")        { UserScreen(nav) }

                        composable("search_tutors") {
                            SearchTutorsScreen(nav)
                        }

                        composable(
                            route = "info_tutor/{id}/{name}" +
                                    "?subject={subject}&desc={desc}&rating={rating}&reviews={reviews}",
                            arguments = listOf(
                                navArgument("id")      { type = NavType.StringType; defaultValue = "" },
                                navArgument("name")    { type = NavType.StringType; defaultValue = "" },
                                navArgument("subject") { type = NavType.StringType; defaultValue = "Design" },
                                navArgument("desc")    { type = NavType.StringType; defaultValue = "Tutor(a) na Peerly." },
                                navArgument("rating")  { type = NavType.FloatType;  defaultValue = 4.9f },
                                navArgument("reviews") { type = NavType.IntType;    defaultValue = 100 }
                            )
                        ) { backStack ->
                            val id      = backStack.arguments?.getString("id").orEmpty()
                            val name    = backStack.arguments?.getString("name")
                            val subject = backStack.arguments?.getString("subject") ?: "Design"
                            val desc    = backStack.arguments?.getString("desc") ?: "Tutor(a) na Peerly."
                            val rating  = backStack.arguments?.getFloat("rating") ?: 4.9f
                            val reviews = backStack.arguments?.getInt("reviews") ?: 100

                            InfoTutorScreen(
                                navController = nav,
                                tutorId = id,
                                tutorName = name,
                                tutorSubject = subject,
                                tutorDesc = desc,
                                tutorRating = rating,
                                tutorReviews = reviews,
                                onUpload = { tutorId, file ->
                                    tutorRepo.uploadAvatar(tutorId, file)
                                }
                            )
                        }

                        composable(
                            route = "agendar_sessao?tutorId={tutorId}&tutorName={tutorName}&tutorSubject={tutorSubject}",
                            arguments = listOf(
                                navArgument("tutorId")      { type = NavType.StringType; defaultValue = "" },
                                navArgument("tutorName")    { type = NavType.StringType; defaultValue = "Tutor(a)" },
                                navArgument("tutorSubject") { type = NavType.StringType; defaultValue = "Design" }
                            )
                        ) { backStack ->
                            val tid   = backStack.arguments?.getString("tutorId")?.ifBlank { null }
                            val tName = backStack.arguments?.getString("tutorName")
                            val tSubj = backStack.arguments?.getString("tutorSubject")

                            AgendarSessaoScreen(
                                navController = nav,
                                tutorId = tid,
                                tutorName = tName,
                                tutorSubject = tSubj
                            )
                        }

                        composable("proxima_sessao") {
                            NextSessionScreen(navController = nav)
                        }

                        composable(
                            route = "chat/{sessionId}?tutorName={tutorName}&avatarUrl={avatarUrl}",
                            arguments = listOf(
                                navArgument("sessionId") { type = NavType.StringType },
                                navArgument("tutorName") {
                                    type = NavType.StringType
                                    defaultValue = "Tutor(a)"
                                },
                                navArgument("avatarUrl") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) { backStack ->
                            val sessionId = backStack.arguments?.getString("sessionId")!!
                            val tutorName = backStack.arguments?.getString("tutorName")
                            val avatarUrl = backStack.arguments?.getString("avatarUrl")

                            ChatScreen(
                                nav,
                                sessionId,
                                tutorName,
                                avatarUrl
                            )
                        }

                        composable(
                            route = "review/{sessionId}",
                            arguments = listOf(
                                navArgument("sessionId") { type = NavType.StringType }
                            )
                        ) { backStack ->
                            val sessionId = backStack.arguments?.getString("sessionId")!!
                            ReviewSessionScreen(
                                navController = nav,
                                sessionId = sessionId
                            )
                        }
                    }
                }
            }
        }
    }
}
