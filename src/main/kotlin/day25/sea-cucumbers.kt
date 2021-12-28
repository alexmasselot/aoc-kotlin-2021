package day25

import day25.SeaCucumber.*
import inputLines
import utils.Matrix
import utils.ShiftDirection.*

enum class SeaCucumber(private val rep: String) {
    EAST(">"),
    SOUTH("v"),
    NONE(".");

    override fun toString() = rep
}

data class Population(
    val seaCucumbers: Matrix<SeaCucumber>
) {

    fun step(): Population = stepEast().stepSouth()

    fun stepEast(): Population {
        val canMove = seaCucumbers.combine(seaCucumbers.cshift(LEFT)) { cur, left ->
            cur == NONE && left == EAST
        }
        val moved = seaCucumbers.combine(canMove) { cur, cm ->
            if (cm) EAST else cur
        }
        return Population(moved.combine(canMove.cshift(RIGHT)) { cur, moved ->
            if (moved) NONE else cur
        })
    }

    fun stepSouth(): Population {
        val canMove = seaCucumbers.combine(seaCucumbers.cshift(DOWN)) { cur, up ->
            cur == NONE && up == SOUTH
        }
        val moved = seaCucumbers.combine(canMove) { cur, cm ->
            if (cm) SOUTH else cur
        }
        return Population(moved.combine(canMove.cshift(UP)) { cur, moved ->
            if (moved) NONE else cur
        })
    }

    override fun toString() = seaCucumbers.toString("")

    companion object {
        fun parse(input: List<String>) =
            Population(
                Matrix(input.map { line ->
                    line.toCharArray()
                        .map { c ->
                            when (c) {
                                '>' -> EAST
                                'v' -> SOUTH
                                '.' -> NONE
                                else -> throw UnsupportedOperationException("Cannot parse $c")
                            }
                        }
                })
            )
    }
}

fun part1(population: Population) {
    tailrec fun handler(pop: Population, nbStep: Int): Int {
        val next = pop.step()
        if (pop == next) {
            return nbStep
        }
        return handler(next, nbStep + 1)
    }
    val n = handler(population, 0)
    println(n+1)
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("25", false)
    val population = Population.parse(input)
    part1(population)
}
