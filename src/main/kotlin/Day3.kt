package com.sheu

import java.nio.file.Path
import kotlin.io.path.readText

fun main() {
    val fileName = "day3.txt"
    val lines = Regex("mul\\(\\d+,\\d+\\)")
        .findAll(Path.of(fileName).readText())
        .map { Regex("\\d+").findAll(it.value).map { d -> d.value.toLong() }.reduce { a, b -> a * b} }
        .sum()


    println("Total number of trees: $lines")

    val lines1 = Regex("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)")
        .findAll(Path.of(fileName).readText()).map { it.value }
     val mult  = lines1.fold(listOf(Pair(0L, true))) { acc, s ->
         if (s == "do()") acc + Pair(0L, true)
         else if (s == "don't()") acc + Pair(0L, false)
         else acc + Pair(
             Regex("\\d+").findAll(s).map { d -> d.value.toLong() }.reduce { a, b -> a * b },
             acc.last().second
         )

     }.filter { it.second }.sumOf { it.first }
    //.map { Regex("\\d+").findAll(it.value).map { d -> d.value.toLong() }.reduce { a, b -> a * b} }
        //.sum()
    println(lines1.toList())
    println("Total number of trees: ${mult}")
}