package com.example.leaflog.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.leaflog.feature_journal.presentation.journal_details.JournalDetailsScreen
import com.example.leaflog.feature_journal.presentation.journals.JournalScreen
import com.example.leaflog.feature_journal.presentation.set_journal.SetJournalScreen
import com.example.leaflog.feature_log.presentation.log_details.LogDetailsScreen
import com.example.leaflog.feature_log.presentation.logs.LogsScreen
import com.example.leaflog.feature_log.presentation.set_log.GetDataScreen
import com.example.leaflog.feature_log.presentation.set_log.SetLogScreen
import com.example.leaflog.feature_log.presentation.set_log.SetLogViewModel
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun Navigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Routes.Journals.name,
    ) {
        composable(route = Routes.Journals.name) {
            JournalScreen(
                onJournalClicked = {
                    val title = URLEncoder.encode(it.title, "utf-8")
                    val description = URLEncoder.encode(it.description, "utf-8")
                    val picture = URLEncoder.encode(it.picture, "utf-8")
                    navController
                        .navigate("${Routes.JournalDetails.name}/${it.id}/${title}/${description}/${picture}")
                },
                onFABClicked = {
                    navController.navigate(Routes.PostJournal.name)
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
            )
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
        composable(route = Routes.PostJournal.name) {
            SetJournalScreen(
                goBack = {
                    navController.popBackStack()
                },
                onPost = {
                    navController.navigateWithPopUpTo(Routes.Journals.name)
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
                    navController.navigateWithPopUpTo(Routes.Journals.name)
                },
                onDelete = {
                    navController.navigateWithPopUpTo(Routes.Journals.name)
                }
            )
        }
        composable(
            route = "${Routes.PostLog.name}/{journalId}",
            arguments = listOf(
                navArgument("journalId") { type = NavType.IntType },
            )
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
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.GetData.name
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
            arguments = listOf(navArgument("journalId"){ type = NavType.IntType })
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
            )
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
                goBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

enum class Routes {
    Journals,
    JournalDetails,
    PostJournal,
    UpdateJournal,
    PostLog,
    GetData,
    Logs,
    LogDetails,
    UpdateLog
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