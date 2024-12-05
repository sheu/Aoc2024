package com.sheu

import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun main() {
    val fileName = "day1.txt"
    val lines = Path.of(fileName).readLines()
    val pairs  = lines.map { line -> line.split(" ").filter { it.trim().isNotEmpty() }.toPair() }
    val first = pairs.map { pair -> pair.first.trim().toLong() }.sorted()
    val second = pairs.map { pair -> pair.second.trim().toLong() }.sorted()
    val zipped  = first.zip(second).map { abs( it.first - it.second) }.sum()

    println("Total distance: $zipped");

    val appearanceSum = first.sumOf { f -> f * second.count { it == f } }
    println("Total similarity score: $appearanceSum")

}

private fun <E> List<E>.toPair(): Pair<E, E> {
    return Pair(this[0], this[1])
}
