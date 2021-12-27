package day24


typealias AluFunction = (Register, Int, Int) -> Register
typealias RegisterFunction = (Register) -> Register


/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */


/**
x <- z % 26
z <- z / p1
x <- x + p2
if(x != w){
x = 1
y = w + p3
z <- z * 26 + y
}else{
x <- 0
y <- 0
}

 */
fun fPuzzle(p1: Int, p2: Int, p3: Int): RegisterFunction = { reg ->
    val w0 = reg.w
    val z0 = reg.z

    val x1 = z0 % 26 + p2
    val z1 = z0 / p1

    if (x1 != w0) {
        val y0 = w0 + p3
        Register(w0, 1, y0, z1 * 26 + y0)
    } else {
        Register(w0, 0, 0, z1)
    }
}

data class Functions(
    val fs: List<RegisterFunction>
) {
    fun apply(reg: Register) =
        fs.fold(reg) { acc, f -> f(acc) }

    companion object {
        val add: AluFunction = { r: Register, iReg1: Int, op2: Int -> r.set(iReg1, r.get(iReg1) + op2) }
        val mul: AluFunction = { r: Register, iReg1: Int, op2: Int -> r.set(iReg1, r.get(iReg1) * op2) }
        val div: AluFunction = { r: Register, iReg1: Int, op2: Int -> r.set(iReg1, r.get(iReg1) / op2) }
        val mod: AluFunction = { r: Register, iReg1: Int, op2: Int -> r.set(iReg1, r.get(iReg1) % op2) }
        val eql: AluFunction =
            { r: Register, iReg1: Int, op2: Int -> r.set(iReg1, if (r.get(iReg1) == op2) 1 else 0) }

        fun parse(input: List<String>) = Functions(input.map { parseOne(it) })

        private fun groupInputLines(input: List<String>)= input.fold(emptyList<List<String>>())
        { acc, l ->
            if (l == "inp w") acc.plusElement(emptyList()) else {
                val last = acc.last()
                acc.dropLast(1).plusElement(last.plus(l))
            }
        }
        fun parsePuzzle(input: List<String>): List<Functions> =
           groupInputLines(input).map { parse(it) }

        fun parseDirects(input: List<String>) =
            groupInputLines(input).map {l ->
                val (p1, p2, p3) = listOf(3, 4, 14).map{ l[it].split(" ")[2].toInt()}
                fPuzzle(p1, p2, p3)
            }


        fun parseOne(str: String): RegisterFunction {
            val tmp = str.split(" ")
            val (fname, op1, op2) = tmp
            val op2Value = !op2.matches("[wxyz]".toRegex())
            val f = when (fname) {
                "add" -> add
                "mul" -> mul
                "div" -> div
                "mod" -> mod
                "eql" -> eql
                else -> throw UnsupportedOperationException("Does not know function $fname")
            }

            if (op2Value) {
                return { r: Register ->
                    f(r, op1.first().code - 'w'.code, op2.toInt())
                }
            } else {
                return { r: Register ->
                    f(r, op1.first().code - 'w'.code, r.get(op2.first().code - 'w'.code))
                }
            }
        }
    }
}
