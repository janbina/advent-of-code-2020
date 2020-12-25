package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.Point2D
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(24)
    Day24(input).also {
        printAndTest(it.part1(), 300)
        printAndTest(it.part2(), 3466)
    }
}

class Day24(inputLines: List<String>) {

    private val input = inputLines.map { it.toDirections() }

    fun part1(): Any {
        return initialConfiguration().count { it.value }
    }

    fun part2(): Any {
        var blacks = initialConfiguration()

        repeat(100) {
            val candidates = blacks.filterValues { it }.keys.map { it.hexAdjacent() + it }.flatten().toSet()
            val newBlacks = mutableMapOf<Point2D, Boolean>()
            candidates.forEach { candidate ->
                val adjacentBlack = candidate.hexAdjacent().count { blacks[it] == true }
                val isBlack = blacks[candidate] == true
                newBlacks[candidate] = (isBlack && adjacentBlack in 1..2) || (!isBlack && adjacentBlack == 2)
            }
            blacks = newBlacks
        }

        return blacks.count { it.value }
    }

    private fun initialConfiguration(): Map<Point2D, Boolean> {
        val blacks = mutableMapOf<Point2D, Boolean>()
        input.forEach {
            val tile = Point2D(0, 0).move(it)
            blacks[tile] = blacks[tile]?.not() ?: true
        }
        return blacks
    }

    // Using axial coordinates here: https://www.redblobgames.com/grids/hexagons/#coordinates-axial
    private fun Point2D.move(moves: List<Direction>): Point2D {
        return moves.fold(this) { point, direction ->
            when (direction) {
                Direction.E -> point.copy(x = point.x + 1)
                Direction.W -> point.copy(x = point.x - 1)
                Direction.SE -> point.copy(y = point.y + 1)
                Direction.SW -> point.copy(x = point.x - 1, y = point.y + 1)
                Direction.NE -> point.copy(x = point.x + 1, y = point.y - 1)
                Direction.NW -> point.copy(y = point.y - 1)
            }
        }
    }

    private fun Point2D.hexAdjacent(): List<Point2D> {
        return Direction.values().map { this.move(listOf(it)) }
    }

    private fun String.toDirections(): List<Direction> {
        val list = mutableListOf<Direction>()
        var i = 0

        while (i < length) {
            val numChars = if (this[i] in "sn") 2 else 1
            val str = this.drop(i).take(numChars)
            list.add(Direction.values().first { it.str == str })
            i += numChars
        }

        return list
    }

    private enum class Direction(val str: String) {
        E("e"), W("w"),
        SE("se"), SW("sw"),
        NE("ne"), NW("nw"),
    }
}
