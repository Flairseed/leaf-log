package com.example.leaflog.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.leaflog.core.presentation.home.HomeScreen
import com.example.leaflog.feature_authentication.presentation.login.LoginScreen
import com.example.leaflog.feature_authentication.presentation.register.RegisterScreen
import com.example.leaflog.feature_journal.presentation.journal_details.JournalDetailsScreen
import com.example.leaflog.feature_journal.presentation.set_journal.SetJournalScreen
import com.example.leaflog.feature_log.presentation.log_details.LogDetailsScreen
import com.example.leaflog.feature_log.presentation.logs.LogsScreen
import com.example.leaflog.feature_log.presentation.set_log.GetDataScreen
import com.example.leaflog.feature_log.presentation.set_log.SetLogScreen
import com.example.leaflog.feature_log.presentation.set_log.SetLogViewModel
import com.example.leaflog.feature_online_post.presentation.post_details.PostDetailsScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun Navigation() {
    val navController = rememberNavController()

    var startDest by remember {
        mutableStateOf(Routes.Login.name)
    }
    
    NavHost(
        navController = navController,
        startDestination = startDest,
    ) {
        composable(
            route = Routes.Login.name
        ) {
            LoginScreen(
                goToRegisterScreen = {
                    startDest = Routes.Register.name
                    navController.navigateWithPopUpTo(Routes.Register.name, true)
                },
                goToHomeScreen = {
                    startDest = Routes.Home.name
                    navController.navigateWithPopUpTo(Routes.Home.name)
                }
            )
        }

        composable(
            route = Routes.Register.name
        ) {
            RegisterScreen(
                goToLoginScreen = {
                    startDest = Routes.Login.name
                    navController.navigateWithPopUpTo(Routes.Login.name, true)
                },
                goToHomeScreen = {
                    startDest = Routes.Home.name
                    navController.navigateWithPopUpTo(Routes.Home.name)
                }
            )
        }

        composable(route = Routes.Home.name) {
            HomeScreen(
                onJournalClicked = {
                    val title = URLEncoder.encode(it.title, "utf-8")
                    val description = URLEncoder.encode(it.description, "utf-8")
                    val picture = URLEncoder.encode(it.picture, "utf-8")
                    navController
                        .navigate("${Routes.JournalDetails.name}/${it.id}/${title}/${description}/${picture}")
                },
                onPostClicked = {
                    val encodedTitle = URLEncoder.encode(it.title, "utf-8")
                    val encodedDescription = URLEncoder.encode(it.description, "utf-8")
                    val encodedPicture = URLEncoder.encode(it.picture, "utf-8")

                    navController.navigate(
                        "${Routes.PostDetails.name}/$encodedTitle/$encodedDescription/$encodedPicture/${it.height}/${it.water}/${it.temperature}/${it.relativeHumidity}/${it.lightLevel}/${it.created.time}"
                    )
                },
                onFABClicked = {
                    navController.navigate(Routes.PostJournal.name)
                },
                goToLoginScreen = {
                    startDest = Routes.Login.name
                    navController.navigateWithPopUpTo(Routes.Login.name)
                }
            )
        }
        composable(
            route = "${Routes.JournalDetails.name}/{journalId}/{title}/{description}/{picture}",
            arguments = listOf(
                navArgument("journalId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("picture") { type = NavType.StringType }
            ),
            enterTransition = { NavigationAnimation.slideUpEnter },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideDownExit },
            popEnterTransition = { EnterTransition.None }
        ) {
            JournalDetailsScreen(
                journalId = it.arguments?.getInt("journalId") ?: 0,
                title = URLDecoder.decode(it.arguments?.getString("title"), "utf-8"),
                description = URLDecoder.decode(it.arguments?.getString("description"), "utf-8"),
                picture = URLDecoder.decode(it.arguments?.getString("picture"), "utf-8"),
                goBack = {
                    navController.popBackStack()
                },
                onEdit = { id ->
                    navController.navigate("${Routes.UpdateJournal.name}/$id")
                },
                goToLogs = { journalId ->
                    navController.navigate("${Routes.Logs.name}/$journalId")
                }
            )
        }
        composable(
            route = Routes.PostJournal.name,
            enterTransition = { NavigationAnimation.slideUpEnter },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideDownExit },
            popEnterTransition = { EnterTransition.None }
        ) {
            SetJournalScreen(
                goBack = {
                    navController.popBackStack()
                },
                onPost = {
                    navController.navigateWithPopUpTo(Routes.Home.name)
                }
            )
        }
        composable(
            route = "${Routes.UpdateJournal.name}/{journalId}",
            arguments = listOf(navArgument("journalId") { type = NavType.IntType })
        ) {
            SetJournalScreen(
                postId = it.arguments?.getInt("journalId"),
                goBack = {
                    navController.popBackStack()
                },
                onPost = {
                    navController.navigateWithPopUpTo(Routes.Home.name)
                },
                onDelete = {
                    navController.navigateWithPopUpTo(Routes.Home.name)
                }
            )
        }
        composable(
            route = "${Routes.PostLog.name}/{journalId}",
            arguments = listOf(
                navArgument("journalId") { type = NavType.IntType },
            ),
            enterTransition = { NavigationAnimation.slideUpEnter },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideDownExit },
            popEnterTransition = { EnterTransition.None }
        ) {
            val journalId = it.arguments?.getInt("journalId") ?: 0
            val viewModel = viewModel(it) {
                SetLogViewModel(
                    journalId = journalId,
                    logId = null
                )
            }
            SetLogScreen(
                viewModel = viewModel,
                onGetData = {
                    navController.navigate(Routes.GetData.name)
                },
                onPost = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("${Routes.Logs.name}/$journalId")
                },
                onUpdate = {},
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.GetData.name,
            enterTransition = { NavigationAnimation.slideIn },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideOut},
            popEnterTransition = { EnterTransition.None }
        ) {
            val parent = remember {
                navController.previousBackStackEntry!!
            }
            val viewModel: SetLogViewModel = viewModel(parent)
            GetDataScreen(
                viewModel = viewModel,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "${Routes.Logs.name}/{journalId}",
            arguments = listOf(navArgument("journalId"){ type = NavType.IntType }),
            enterTransition = { NavigationAnimation.slideIn },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideOut },
            popEnterTransition = { EnterTransition.None }
        ) {
            val journalId = it.arguments?.getInt("journalId") ?: 0
            LogsScreen(
                journalId = journalId,
                goBack = {
                    navController.popBackStack()
                },
                onJournalClicked = { log ->
                    navController.navigate("${Routes.LogDetails.name}/$journalId/${log.id}")
                },
                onFABClicked = {
                    navController.navigate("${Routes.PostLog.name}/$journalId")
                }
            )
        }
        composable(
            route = "${Routes.LogDetails.name}/{journalId}/{logId}",
            arguments = listOf(
                navArgument("journalId"){ type = NavType.IntType },
                navArgument("logId"){ type = NavType.IntType }
            ),
            enterTransition = { NavigationAnimation.slideUpEnter },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideDownExit },
            popEnterTransition = { EnterTransition.None }
        ) {
            val journalId = it.arguments?.getInt("journalId") ?: 0
            val logId = it.arguments?.getInt("logId") ?: 0
            LogDetailsScreen(
                logId = logId,
                goBack = {
                    navController.popBackStack()
                },
                onDelete = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("${Routes.Logs.name}/$journalId")
                },
                goToUpdateScreen = {
                    navController.navigate("${Routes.UpdateLog.name}/$journalId/$logId")
                }
            )
        }
        composable(
            route = "${Routes.UpdateLog.name}/{journalId}/{logId}",
            arguments = listOf(
                navArgument("journalId") { type = NavType.IntType },
                navArgument("logId") { type = NavType.IntType }
            )
        ) {
            val journalId = it.arguments?.getInt("journalId") ?: 0
            val logId = it.arguments?.getInt("logId") ?: 0
            val viewModel = viewModel(it) {
                SetLogViewModel(
                    journalId = journalId,
                    logId = logId
                )
            }
            SetLogScreen(
                viewModel = viewModel,
                onGetData = {
                    navController.navigate(Routes.GetData.name)
                },
                onPost = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("${Routes.Logs.name}/$journalId")
                },
                onUpdate = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("${Routes.Logs.name}/$journalId")
                    navController.navigate("${Routes.LogDetails.name}/$journalId/$logId")
                },
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "${Routes.PostDetails.name}/{title}/{description}/{picture}/{height}/{water}/{temperature}/{relativeHumidity}/{lightLevel}/{created}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("picture") { type = NavType.StringType },
                navArgument("height") { type = NavType.FloatType },
                navArgument("water") { type = NavType.IntType },
                navArgument("temperature") { nullable = true },
                navArgument("relativeHumidity") { nullable = true },
                navArgument("lightLevel") { nullable = true },
                navArgument("created") { type = NavType.LongType },
            ),
            enterTransition = { NavigationAnimation.slideUpEnter },
            exitTransition = { ExitTransition.None },
            popExitTransition = { NavigationAnimation.slideDownExit },
            popEnterTransition = { EnterTransition.None }
        ) {
            val title = URLDecoder.decode(it.arguments?.getString("title") ?: "", "utf-8")
            val description = URLDecoder.decode(it.arguments?.getString("description") ?: "", "utf-8")
            val picture = URLDecoder.decode(it.arguments?.getString("picture") ?: "", "utf-8")
            val height = it.arguments?.getFloat("height") ?: 0f
            val water = it.arguments?.getInt("water") ?: 0
            val temperature = it.arguments?.getString("temperature")?.toInt()
            val relativeHumidity = it.arguments?.getString("relativeHumidity")?.toInt()
            val lightLevel = it.arguments?.getString("lightLevel")?.toFloat()
            val created = it.arguments?.getLong("created") ?: 0L

            PostDetailsScreen(
                title = title,
                description = description,
                picture = picture,
                height = height,
                water = water,
                temperature = temperature,
                relativeHumidity = relativeHumidity,
                lightLevel = lightLevel,
                created = created,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

enum class Routes {
    Login,
    Register,
    Home,
    JournalDetails,
    PostJournal,
    UpdateJournal,
    PostLog,
    GetData,
    Logs,
    LogDetails,
    UpdateLog,
    PostDetails
}

fun NavController.navigateWithPopUpTo(route: String, restore: Boolean = false) {
    navigate(route) {
        popUpTo(this@navigateWithPopUpTo.graph.findStartDestination().id) {
            saveState = true
            inclusive = true
        }
        launchSingleTop = true
        restoreState = restore
    }
}

object NavigationAnimation {
    val slideIn: EnterTransition = slideInHorizontally(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullWidth ->
        fullWidth
    }
    val slideInExit: ExitTransition = slideOutHorizontally(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullWidth ->
        -fullWidth
    }
    val slideOut: ExitTransition = slideOutHorizontally(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullWidth ->
        fullWidth
    }
    val slideOutEnter: EnterTransition = slideInHorizontally(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullWidth ->
        -fullWidth
    }

    val slideUpEnter: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullHeight ->
        fullHeight
    }

    val slideDownExit: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = 500
        )
    ) { fullHeight ->
        fullHeight
    }
}