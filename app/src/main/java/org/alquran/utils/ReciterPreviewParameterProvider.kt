package org.alquran.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.alquran.audio.models.Reciter

class ReciterListPreviewParameterProvider : PreviewParameterProvider<List<Reciter>> {
    override val values = sequenceOf(ReciterPreviewParameterProvider().values.toList())
}

class ReciterPreviewParameterProvider : PreviewParameterProvider<Reciter> {
    override val values: Sequence<Reciter> = sequenceOf(
        Reciter(
            id = "abdul_basit_mujawwad",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/2.png",
            name = "Abdul Basit Mujawwad",
            subfolder = "https://download.quranicaudio.com/quran/abdulbaset_mujawwad",
        ),
        Reciter(
            id = "abdul_basit_murattal",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/2.png",
            name = "Abdul Basit Murattal",
            subfolder = "https://download.quranicaudio.com/qdc/abdul_baset/murattal",
        ),
        Reciter(
            id = "mishari_alafasy",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/8.png",
            name = "Mishary Alafasy",
            subfolder = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee",
        ),
        Reciter(
            id = "abdullah_matroud",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/33.png",
            name = "Abdullah Matroud",
            subfolder = "https://download.quranicaudio.com/quran/abdullah_matroud/reencode",
        ),
        Reciter(
            id = "sa3d_alghamidi",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/9.png",
            name = "Saad Al-Ghamadi",
            subfolder = "https://download.quranicaudio.com/quran/sa3d_al-ghaamidi/complete",
        ),
        Reciter(
            id = "sudais_murattal",
            image = "https://raw.githubusercontent.com/azizndao/reciters_images/main/4.png",
            name = "Abdurrahmaan As-Sudais",
            subfolder = "https://download.quranicaudio.com/quran/abdurrahmaan_as-sudays",
        ),
    )
}
