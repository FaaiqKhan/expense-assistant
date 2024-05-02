package com.practice.expenseAssistant.ui.splashScreen

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.utils.Screens

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    val path = ("android.resource://" + LocalContext.current.packageName) + "/" + R.raw.splash

    val isSessionExpired = viewModel.isSessionExpired.collectAsState()

    when (isSessionExpired.value) {
        null -> Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(color = colorResource(id = R.color.sky_blue)),
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(Uri.parse(path))
                        setOnPreparedListener {
                            it.isLooping = true
                            setZOrderOnTop(true)
                            it.start()
                        }
                    }
                },
            )
        }

        false -> LaunchedEffect(key1 = Screens.HOME.name) {
            navController.navigate(Screens.HOME.name) { popUpTo(0) }
        }

        true -> LaunchedEffect(key1 = Screens.LOGIN.name) {
            navController.navigate(Screens.LOGIN.name) { popUpTo(0) }
        }
    }
}