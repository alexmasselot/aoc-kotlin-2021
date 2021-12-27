package day24

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class Register(
    val w: Int,
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun get(i: Int) =
        when (i) {
            0 -> w
            1 -> x
            2 -> y
            3 -> z
            else -> throw UnsupportedOperationException("Cannot get $i")
        }

    fun get(i: Char) =
        when (i) {
            'w' -> w
            'x' -> x
            'y' -> y
            'z' -> z
            else -> throw UnsupportedOperationException("Cannot get $i")
        }

    fun set(i: Int, v: Int) =
        when (i) {
            0 -> copy(w = v)
            1 -> copy(x = v)
            2 -> copy(y = v)
            3 -> copy(z = v)
            else -> throw UnsupportedOperationException("Cannot set $i with $v")
        }

    fun set(i: Char, v: Int) =
        when (i) {
            'w' -> copy(w = v)
            'x' -> copy(x = v)
            'y' -> copy(y = v)
            'z' -> copy(z = v)
            else -> throw UnsupportedOperationException("Cannot set $i with $v")
        }

}
