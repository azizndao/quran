package org.alquran.utils


fun getLanguageFlag(languageCode: String): String {
    return "https://flagcdn.com/w160/${LanguageFlatMap[languageCode] ?: languageCode}.png"
}

private val LanguageFlatMap = mapOf(
    "en" to "gb",
    "ar" to "sa",
    "ja" to "jp",
    "zh" to "cn",
    "sp" to "ab",
    "ha" to "ng",
    "hi" to "in",
    "te" to "in",
    "kk" to "kz",
    "ko" to "kp",
    "fa" to "in",
    "sq" to "al",
    "ku" to "kg",
    "am" to "et",
    "prs" to "af",
    "ce" to "ru",
    "zgh" to "ma",
    "sw" to "tz",
    "ta" to "lk",
    "ur" to "pk",
    "cs" to "cz",
    "dv" to "mv",
    "he" to "il",
    "ka" to "ge",
    "uk" to "ua",
    "yo" to "ng",
    "mrn" to "au",
    "lg" to "ug",
    "bm" to "ml"
);
