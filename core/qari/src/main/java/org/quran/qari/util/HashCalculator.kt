package org.quran.qari.util

import java.io.File

interface HashCalculator {
  fun calculateHash(file: File): String
}
