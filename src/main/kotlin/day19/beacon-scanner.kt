package day19

import inputLines


fun findExplanationPath(scanners: List<Scanner>, minCount: Int): List<RotationTranslationMatch> {
    return scanners.flatMap { scanner1 ->
        scanners.filter { it.id != scanner1.id }.mapNotNull { scanner2 ->
            scanner1.findMatchingRotated(scanner2, minCount)
        }
    }
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("19", true)
    val scanners = Scanner.buildScanners(inputLines("19", false))

    val transfos = findExplanationPath(scanners, 12)
    println(transfos.joinToString("\n"))

    val graph = ExplanationPathNode.build(transfos)
    println(graph.toString(""))

    val uniqueBeaconPosition = graph.reduceBeacons()
    println(uniqueBeaconPosition.size)

    val scannerPositions = graph.scannerPositions()
    val maxManhattan = scannerPositions.flatMap{p1->
        scannerPositions.map{p2->p1.to(p2).manhattan()}
    }.maxOrNull()
    println(maxManhattan)

}


