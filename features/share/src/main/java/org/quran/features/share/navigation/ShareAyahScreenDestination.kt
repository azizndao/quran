package org.quran.features.share.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel
import org.quran.features.share.ShareAyahScreen
import org.quran.features.share.ShareAyahViewModel

private const val PARAM_SURAH_AYAH = "suraAyah"

fun NavController.navigateToShareAyah(verse: VerseKey) = navigate("quran/share/$verse")

internal fun shareAyahDestinationArgs(savedStateHandle: SavedStateHandle): VerseKey {
  return VerseKey.fromString(savedStateHandle[PARAM_SURAH_AYAH]!!)!!
}


fun NavGraphBuilder.shareAyahDestination(popBackStack: () -> Unit) {
  composable("quran/share/{$PARAM_SURAH_AYAH}") {
    val viewModel = getViewModel<ShareAyahViewModel>()
    ShareAyahScreen(
      uiState = viewModel.uiState,
      share = viewModel::share,
      popBackStack = popBackStack
    )
  }
}