package day19

data class TranslationMatch(
    val translation: Translation,
    val matchingBeacons: List<Pair<Beacon, Beacon>>
)
