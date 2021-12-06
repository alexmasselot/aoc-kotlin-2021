package day06

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class LanternFishCounter(
    val cycle: Int,
    val distribution: LongArray = LongArray(cycle + 2) { 0 }
) {
    fun count() = distribution.sum()

    fun increment(): LanternFishCounter {
        val zeros = distribution[0]
        val newDist = distribution.drop(1).plus(zeros).toLongArray()
        newDist[cycle - 1] += zeros
        return this.copy(distribution = newDist)
    }

    fun incrementFor(nbDays: Int) =
        (1..nbDays).fold(this) { acc, i -> acc.increment() }


    companion object {
        fun fromInitialAges(cycle: Int, str: String): LanternFishCounter {
            val acc = LongArray(cycle + 2) { 0L }
            val distrib = str.split(",").map { it.toInt() }
                .forEach { i -> acc[i] += 1L }
            return LanternFishCounter(cycle, acc)
        }
    }

    @Override
    override fun equals(o: Any?): Boolean =
        if (o is LanternFishCounter) {
            (cycle == o.cycle) && (distribution.toList() == o.distribution.toList())
        } else {
            false
        }
}
