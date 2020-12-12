package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import kotlin.math.absoluteValue

fun main() {
    val input = Input.getDayInputLines(12)
    Day12(input).also {
        printAndTest(it.part1(), 1838)
        printAndTest(it.part2(), 89936)
    }
}

class Day12(inputLines: List<String>) {

    private val input = inputLines.map {
        Instruction(it.first(), it.drop(1).toInt())
    }

    fun part1(): Int {
        var ship = Position()
        var facing = Position(east = 1)

        input.forEach {
            when (it.action) {
                'L' -> repeat(it.value / 90 % 4) { facing = facing.turnLeft() }
                'R' -> repeat(it.value / 90 % 4) { facing = facing.turnRight() }
                'N' -> ship += Position(north = it.value)
                'S' -> ship += Position(south = it.value)
                'W' -> ship += Position(west = it.value)
                'E' -> ship += Position(east = it.value)
                'F' -> ship += facing * it.value
            }
        }

        return ship.manhattanFrom0()
    }

    fun part2(): Int {
        var ship = Position()
        var waypoint = Position(north = 1, east = 10)

        input.forEach {
            when (it.action) {
                'L' -> repeat(it.value / 90 % 4) { waypoint = waypoint.turnLeft() }
                'R' -> repeat(it.value / 90 % 4) { waypoint = waypoint.turnRight() }
                'N' -> waypoint += Position(north = it.value)
                'S' -> waypoint += Position(south = it.value)
                'W' -> waypoint += Position(west = it.value)
                'E' -> waypoint += Position(east = it.value)
                'F' -> ship += waypoint * it.value
            }
        }

        return ship.manhattanFrom0()
    }

    data class Instruction(
        val action: Char,
        val value: Int,
    )

    data class Position(
        val north: Int = 0,
        val south: Int = 0,
        val west: Int = 0,
        val east: Int = 0,
    ) {
        fun manhattanFrom0() = (north - south).absoluteValue + (west - east).absoluteValue

        fun turnLeft() = Position(
            north = east,
            south = west,
            west = north,
            east = south,
        )

        fun turnRight() = Position(
            north = west,
            south = east,
            west = south,
            east = north,
        )

        operator fun plus(other: Position) = Position(
            north = north + other.north,
            south = south + other.south,
            west = west + other.west,
            east = east + other.east,
        )

        operator fun times(times: Int) = Position(
            north = north * times,
            south = south * times,
            west = west * times,
            east = east * times,
        )
    }
}
