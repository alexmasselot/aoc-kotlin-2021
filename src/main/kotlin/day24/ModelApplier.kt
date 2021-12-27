package day24

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
object ModelApplier {
    fun apply(model: Input, functions: List<RegisterFunction>) {
        functions.fold(model to Register(0, 0, 0, 0)) { (mod, reg), fs ->
            val pop = mod.pop()
            val w = pop.first
            val remain = pop.second
            val newReg = fs(reg.set('w', w))
            println("$newReg\t$remain")
            remain to newReg
        }
    }

    fun investigate(f: RegisterFunction, target: Int): Set<Pair<Int, Int>> {
        return (-10000..10000).flatMap { z ->
            (1..9).map { w ->
                val reg = Register(w, 0, 0, z)
                (w to z) to f(reg)
            }
        }
            .filter { target == it.second.z }
            .map { it.first }
            .toSet()
    }
}
