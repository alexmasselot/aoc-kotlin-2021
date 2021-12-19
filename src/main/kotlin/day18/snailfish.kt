package day18

import inputLines

sealed class SFNumber() {
    abstract var depth: Int
    abstract var isLeftist: Boolean
    var parent: SFNumberPair? = null
    abstract fun setParents(): Unit

    fun findExplosionTarget(): SFNumberPair? {
        fun handler(sf: SFNumber): SFNumberPair? {
            return when (sf) {
                is SFNumberRegular -> null
                is SFNumberPair -> if (sf.isRegularPair() && sf.depth >= 4) {
                    sf
                } else {
                    handler(sf.left) ?: handler(sf.right)
                }
            }
        }
        return handler(this)
    }

    fun findAndSplit(): Boolean {
        fun handler(sf: SFNumber): Boolean {
            return when (sf) {
                is SFNumberRegular -> {
                    if (sf.value > 9) {
                        val pair = sf.split()
                        if (sf.isLeftist) {
                            sf.parent!!.left = pair
                        } else {
                            sf.parent!!.right = pair
                        }
                        sf.parent!!.setParents()
                        true
                    } else {
                        false
                    }
                }
                is SFNumberPair ->
                    handler(sf.left) || handler(sf.right)
            }
        }
        return handler(this)
    }

    fun plus(other: SFNumber): SFNumberPair {
        isLeftist = true
        other.isLeftist = false
        val p = SFNumberPair(
            0,
            true,
            this,
            other
        )
        p.setParents()

        fun handlerSetDepth(sf: SFNumber, depth: Int) {
            sf.depth = depth
            if (sf is SFNumberPair) {
                handlerSetDepth(sf.left, depth + 1)
                handlerSetDepth(sf.right, depth + 1)
            }
        }
        handlerSetDepth(p, 0)
        return p
    }

    fun magnitude(): Int {
        fun handler(sf: SFNumber): Int {
            return when (sf) {
                is SFNumberRegular -> sf.value
                is SFNumberPair -> 3 * handler(sf.left) + 2 * handler(sf.right)
            }
        }
        return handler(this)
    }

    companion object {
        fun splitBracketBlock(str: String): Pair<String, String> {
            val add = mapOf('[' to 1, ']' to -1)

            if (!str.startsWith('[') || !str.endsWith(']')) {
                throw UnsupportedOperationException(str)
            }
            val strInner = str.drop(1).dropLast(1)
            val firstBlock = strInner.runningFold("" to 0) { acc, c ->
                acc.first + c to acc.second + add.getOrDefault(c, 0)
            }.takeWhile {
                !(it.first.endsWith(',') && (it.second == 0))
            }
                .map { it.first }
                .last()
            val remainder = strInner.drop(firstBlock.length + 1) // skip the comma and closing brackets

            return firstBlock to remainder
        }

        fun build(str: String): SFNumberPair {
            val reNumber = """(\d+)""".toRegex()

            fun handler(str: String, depth: Int, isLeftist: Boolean): SFNumber {
                return if (reNumber.matches(str)) {
                    val (x) = reNumber.find(str, 0)!!.destructured
                    SFNumberRegular(depth, isLeftist, x.toInt())
                } else {
                    val (x, y) = splitBracketBlock(str)
                    val p = SFNumberPair(depth, isLeftist, handler(x, depth + 1, true), handler(y, depth + 1, false))
                    p.setParents()
                    p
                }
            }
            return handler(str, 0, true) as SFNumberPair
        }
    }
}

data class SFNumberRegular(
    override var depth: Int,
    override var isLeftist: Boolean,
    var value: Int
) : SFNumber() {
    override fun setParents() {
    }

    fun split(): SFNumberPair {
        if (value <= 9) {
            throw UnsupportedOperationException(this.toString())
        }
        val pair = SFNumberPair(
            depth,
            isLeftist,
            SFNumberRegular(depth + 1, true, value / 2),
            SFNumberRegular(depth + 1, false, value - value / 2),
        )
        pair.setParents()
        return pair
    }

    override fun toString() = "$value"
}

data class SFNumberPair(
    override var depth: Int,
    override var isLeftist: Boolean,
    var left: SFNumber,
    var right: SFNumber
) : SFNumber() {

    fun isLeftRegular() = left is SFNumberRegular
    fun isRightRegular() = right is SFNumberRegular
    fun isRegularPair() = isLeftRegular() && isRightRegular()

    override fun setParents() {
        left.parent = this
        right.parent = this
    }

    fun reduce() {
        do {
            do {
                val x = explode()
            } while (x)
            val y = findAndSplit()
        } while (y)
    }


    fun explode(): Boolean {
        return findExplosionTarget()?.let { target ->
            val targetParent = target.parent!!
            targetParent.explodeLeft((target.left as SFNumberRegular).value, false, target.isLeftist)
            targetParent.explodeRight((target.right as SFNumberRegular).value, false, !target.isLeftist)

            if (target.isLeftist) {
                targetParent.left = SFNumberRegular(target.depth, true, 0)
            } else {
                targetParent.right = SFNumberRegular(target.depth, false, 0)
            }
            targetParent.setParents()
            true
        } ?: false
    }

    fun explodeLeft(value: Int, isComingFromTop: Boolean, isComingFromDownLeft: Boolean?) {
        if (isComingFromTop) {
            when (right) {
                is SFNumberRegular -> {
                    (right as SFNumberRegular).value += value
                    return
                }
                is SFNumberPair -> {
                    (right as SFNumberPair).explodeLeft(value, true, null)
                    return
                }
            }
        }

        if (isComingFromDownLeft!!) {
            parent?.explodeLeft(value, false, isLeftist)
            return
        } else {
            when (left) {
                is SFNumberRegular -> {
                    (left as SFNumberRegular).value += value
                    return
                }
                is SFNumberPair -> {
                    (left as SFNumberPair).explodeLeft(value, true, null)
                    return
                }
            }
        }
    }

    fun explodeRight(value: Int, isComingFromTop: Boolean, isComingFromDownRight: Boolean?) {
        if (isComingFromTop) {
            when (left) {
                is SFNumberRegular -> {
                    (left as SFNumberRegular).value += value
                    return
                }
                is SFNumberPair -> {
                    (left as SFNumberPair).explodeRight(value, true, null)
                    return
                }
            }
        }
        if (isComingFromDownRight!!) {
            parent?.explodeRight(value, false, !isLeftist)
            return
        } else {
            when (right) {
                is SFNumberRegular -> {
                    (right as SFNumberRegular).value += value
                    return
                }
                is SFNumberPair -> {
                    (right as SFNumberPair).explodeRight(value, true, null)
                    return
                }
            }
        }
    }


    override fun toString() = "[$left,$right]"
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

fun reduceList(input: List<String>): SFNumber {
    val sfs = input.map { SFNumber.build(it) }

    return sfs.drop(1).fold(sfs.first()) { acc, sf ->
        val acc1 = acc.plus(sf)
        acc1.reduce()
        acc1
    }
}

fun largestMagnitude(input: List<String>): Int? {
    val sfs = input.map { SFNumber.build(it) }

    return input.flatMap { l1 ->
        input.filter { it != l1 }
            .map { l2 ->
                val sf1 = SFNumber.build(l1)
                val sf2 = SFNumber.build(l2)
                val sf = sf1.plus(sf2)
                sf.reduce()
                sf.magnitude()
            }
    }
        .maxOrNull()

}

fun main() {
    val input = inputLines("18", false)

    var res = reduceList(input)

    println(res)
    println(res.magnitude())

    println(largestMagnitude(input))

}
