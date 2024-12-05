package com.sheu

import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun main() {
    val fileName = "day2.txt"
    val lines = Path.of(fileName).readLines()
    val pairs  = lines.map { line -> line.split(" ").map { num -> num.trim().toLong() } }
    val pureSafe = pairs.count { it.isSafe()  }
    val maybeSafe = pairs.filter { !it.isSafe() }.count { it.isSafeByRemovingALevel() }
    println("Total number of safe paths: ${pureSafe + maybeSafe}")


}
private fun List<Long>.isSafe(): Boolean {
    return if( first() < last() ) zipWithNext().all { it.first < it.second && it.second - it.first <= 3 }
    else zipWithNext().all { it.first > it.second && it.first - it.second <= 3 }
}

private fun List<Long>.isSafeByRemovingALevel(): Boolean {
    return this.indices.map { index ->
        this.filterIndexed { i, _ -> i != index }
    }.any { it.isSafe() }
}