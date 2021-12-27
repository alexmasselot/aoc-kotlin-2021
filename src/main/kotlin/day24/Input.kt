package day24

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

data class Input(
    val value: Long,
    val mask: Long = (10.0).pow(floor(log10(value.toDouble()))).toLong()
) {

    fun pop() = (value / mask).toInt() to Input(value.mod(mask), mask / 10)

    fun dec(): Input {
        if (value == 1L) throw UnsupportedOperationException("Cannot dec() 1")
        var v = value
        do {
            v = v - 1
        } while (v.toString().contains("0"))

        if(v < mask){
            throw UnsupportedOperationException("Cannot dec() $value")
        }
        return Input(v, mask)
    }
}
