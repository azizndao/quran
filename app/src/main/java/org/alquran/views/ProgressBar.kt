package org.muslimapp.feature.quran.views

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CircularProgressLoader(
    loading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Crossfade(loading, modifier = modifier.fillMaxSize()) {
        if (it) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            content()
        }
    }
}

