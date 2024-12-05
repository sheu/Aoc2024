package com.sheu

import java.io.File

fun main() {

    val filePath = "day5.txt"

    val (orders, updates)   = readAndSplitFile(filePath)
    println("Orders: $orders")

    val validUpdates  = updates.filter { checkSequence(it, orders) }.mapNotNull { it.findMiddleItem() }.sum()
    println("Valid updates: $validUpdates")
    val invalidUpdate = updates.filter { !checkSequence(it, orders) }.map { orderSequence(it,orders) }
        println("Invalid updates sequences: $invalidUpdate")
    val result = invalidUpdate.mapNotNull { it.findMiddleItem() }.sum()

    println("Invalid Updates: $result")
}

private fun <E> List<E>.findMiddleItem(): E? {
    val size = this.size
    if (size == 0) return null // Return null for empty list

    val middleIndex = size / 2
    return if (size % 2 == 0) {
        // For even-sized lists, return the lower of the two middle numbers
        this[middleIndex - 1]
    } else {
        // For odd-sized lists, return the middle number
        this[middleIndex]
    }
}


fun checkSequence(integers: List<Long>, pairs: List<Pair<Long, Long>>): Boolean {
    for ((first, second) in pairs) {
        val firstIndex = integers.indexOf(first)
        val secondIndex = integers.indexOf(second)

        if (firstIndex != -1 && secondIndex != -1 && firstIndex > secondIndex) {
            return false
        }
    }
    return true
}
fun readAndSplitFile(filePath: String): Pair<List<Pair<Long, Long>>, List<List<Long>>> {
    val file = File(filePath)
    val lines = file.readLines()
    val splitIndex = lines.indexOfFirst { it.isBlank() }

    if (splitIndex == -1) {
        // If there's no blank line, return all lines in the first list
        return Pair(emptyList(), emptyList())
    } else {
        val firstList = lines
            .subList(0, splitIndex)
            .map { s -> s.split("|").map { o -> o.trim().toLong() }.zipWithNext().first()  }
        val secondList = lines.subList(splitIndex + 1, lines.size).map { s -> s.split(",").map { i -> i.trim().toLong() } }
        return Pair(firstList, secondList)
    }
}

fun orderSequence(integers: List<Long>, pairs: List<Pair<Long, Long>>): List<Long> {
    val graph = mutableMapOf<Long, MutableSet<Long>>()
    val inDegree = integers.associateWith { 0L }.toMutableMap()

    // Build the graph for only those pairs where both elements are in integers
    pairs.filter { (first, second) -> first in integers && second in integers }.forEach { (first, second) ->
        graph.getOrPut(first) { mutableSetOf() }.add(second)
        inDegree[second] = inDegree[second]!! + 1
    }

    // If there are no valid constraints, return the original list
    if (graph.isEmpty()) {
        return integers
    }

    // Topological sorting for elements that have constraints
    val queue = ArrayDeque<Long>()
    queue.addAll(inDegree.filter { (node, degree) -> degree == 0L && node in graph.keys }.keys)

    val sorted = mutableListOf<Long>()
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        sorted.add(node)

        graph[node]?.forEach { neighbor ->
            inDegree[neighbor] = inDegree[neighbor]!! - 1
            if (inDegree[neighbor] == 0L) {
                queue.add(neighbor)
            }
        }
    }

    // Check if all numbers in integers with constraints are sorted
    if (!integers.all { it !in graph.keys || it in sorted }) {
        throw IllegalArgumentException("The given pairs contain a cycle or are incomplete.")
    }

    // Sort numbers including those without direct constraints
    val result = mutableListOf<Long>()
    var sortedIndex = 0
    for (num in integers) {
        if (num in sorted) {
            result.add(sorted[sortedIndex++])
        } else {
            // Find where to insert num relative to sorted numbers
            val insertIndex = sorted.indexOfFirst { it > num }
            result.add(if (insertIndex == -1) sorted.size else insertIndex, num)
        }
    }

    return result
}