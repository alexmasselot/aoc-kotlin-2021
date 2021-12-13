package day12

import inputLines
import java.util.*

class Cave(val name: String) {
    val connected = mutableListOf<Cave>()
    val isLarge = name == name.uppercase(Locale.getDefault())
    val isEnd = name == "end"

    fun connect(cave: Cave) {
        connected.add(cave)
    }

    @Override
    override fun equals(o: Any?): Boolean =
        if (o is Cave) {
            o.name == name
        } else {
            false
        }
    override fun toString() = "$name: $connected"
}

class CaveSystem() {
    val caves = mutableMapOf<String, Cave>()

    fun getStart() = caves["start"]!!

    fun getOrCreateCave(name: String): Cave {
        if (caves[name] == null) {
            caves[name] = Cave(name)
        }
        return caves[name]!!
    }

    companion object {
        fun read(input: List<String>): CaveSystem {
            val system = CaveSystem()
            val re = """(\w+)\-(\w+)""".toRegex()
            input.forEach { line ->
                val (x, y) = re.matchEntire(line.trim())!!.destructured
                val caveX = system.getOrCreateCave(x)
                val caveY = system.getOrCreateCave(y)
                caveX.connect(caveY)
                caveY.connect(caveX)
            }
            return system
        }
    }
}

data class Path(val caves: List<Cave>) {
    val last = caves.lastOrNull()!!

    fun ends() = last.isEnd

    fun add(cave: Cave) = Path(caves.plus(cave))

    fun canAdd(cave: Cave):Boolean{
        if(cave.name == "start"){
            return false
        }
        if(cave.isLarge){
            return true
        }
        val hasDouble = caves
            .filter{!it.isLarge}
            .groupBy { it.name }.filter{(n, xs)-> xs.size>=2}.isNotEmpty()
        return if(hasDouble){
            !caves.contains(cave)
        }else{
            true
        }
    }


    fun isVisitingTwiceSmall()=
        caves.filter { !(it.isLarge || it.name == "start" || it.name == "end")}
            .groupBy { it.name }
            .filter{(n, caves) -> caves.size != 2}
            .isEmpty()

    override fun toString() = caves.map { it.name }.joinToString(",")
}


fun findPaths(system: CaveSystem): List<Path> {
    val acc = mutableListOf<Path>()
    fun handler(tmpPath: Path) {
        if (tmpPath.ends()) {
                acc.add(tmpPath)
        } else {
            val last = tmpPath.last
            last.connected.forEach { neighbor ->
                if (tmpPath.canAdd(neighbor)) {
                    handler(tmpPath.add(neighbor))
                }
            }
        }
    }
    handler(Path(listOf(system.getStart())))

    return acc.toList().distinct()
}


/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("12", false)

    val system = CaveSystem.read(input)

    val n = findPaths(system).size
    println(n)

}
