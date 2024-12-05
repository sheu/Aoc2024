package com.sheu

import java.io.File

fun main() {
    val filePath = "day4.txt"
    val grid = File(filePath).readLines().map { it.toCharArray() }
    val word = "XMAS"
    val directions = listOf(
        // Right
        Pair(0, 1),
        // Down
        Pair(1, 0),
        // Diagonal Down Right
        Pair(1, 1),
        // Diagonal Down Left
        Pair(1, -1),
        // Left (for reverse search)
        Pair(0, -1),
        // Up (for reverse search)
        Pair(-1, 0),
        // Diagonal Up Left (for reverse search)
        Pair(-1, -1),
        // Diagonal Up Right (for reverse search)
        Pair(-1, 1)
    )

    val foundPositions = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            for ((dx, dy) in directions) {
                if (checkWord(grid, i, j, dx, dy, word)) {
                    foundPositions.add(Pair(Pair(i, j), Pair(dx, dy)))
                }
            }
        }
    }

    if (foundPositions.isNotEmpty()) {
        println("Found 'XMAS' at positions: $foundPositions")
    } else {
        println("Could not find 'XMAS' in the grid.")
    }
    println("Total positions ${foundPositions.size}")
    part2()
}

fun checkWord(grid: List<CharArray>, startX: Int, startY: Int, dx: Int, dy: Int, word: String): Boolean {
    for (i in word.indices) {
        val x = startX + i * dx
        val y = startY + i * dy

        if (x < 0 || y < 0 || x >= grid.size || y >= grid[x].size || grid[x][y] != word[i]) {
            return false
        }
    }
    return true
}
fun part2() {
    val filePath = "day4.txt"
    val grid = File(filePath).readLines().map { it.toCharArray() }
    val word = "MAS"



    val result = checkForX(grid)

    if (result != null) {
        println("Found 'MAS' forming an 'X' at: ${result.size}")
    } else {
        println("Could not find 'MAS' forming an 'X' in the grid.")
    }
}
fun checkForX(grid: List<CharArray>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val foundX = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    for (i in 0 until grid.size - 2) {
        for (j in 0 until grid[i].size - 2) {
            // Extract a 3x3 subgrid
            val subGrid = grid.subList(i, i + 3).map { it.copyOfRange(j, j + 3) }

            val result = checkForXInSubGrid(subGrid)
            if (result != null) {
                // Convert subgrid coordinates back to original grid coordinates
                foundX.add (
                 Pair(Pair(i + result.first.first, j + result.first.second),
                    Pair(i + result.second.first, j + result.second.second))
                )
            }
        }
    }
    return foundX
}

fun checkForXInSubGrid(subGrid: List<CharArray>): Pair<Pair<Int, Int>, Pair<Int, Int>>? {
    val directions = listOf(
        // Diagonal Down Right
        Pair(1, 1),
        // Diagonal Down Left

        // Diagonal Up Right (for reverse search)
        Pair(-1, 1)
    )
    println("Checking ${subGrid.map { it.contentToString() }}")


        if (checkWord(subGrid, 0, 0, 1, 1, "MAS") || checkWord(subGrid, 0, 0, 1, 1, "SAM")) {

            println("Found first match in direction 1, 1")
            if (checkWord(subGrid, 0, 2, 1, -1, "MAS")|| checkWord(subGrid, 0, 2, 1, -1, "SAM")) {
                println("Found second match in direction 1, -1")
                return Pair(Pair(0, 0), Pair(0, 2))
            }
        }


    return null
}