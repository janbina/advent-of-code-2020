package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.*

fun main() {
    val input = Input.getDayInputLines(11)
    Day11(input).also {
        printAndTest(it.part1(), 2359)
        printAndTest(it.part2(), 2131)
    }
}

class Day11(inputLines: List<String>) {

    private val inputArr = inputLines.map {
        it.toCharArray().toTypedArray()
    }.toTypedArray()

    fun part1(): Int {
        return execute(
            inputArr,
            ::adjacentOccupied
        ) { value, occupied ->
            when {
                value == 'L' && occupied == 0 -> '#'
                value == '#' && occupied >= 4 -> 'L'
                else -> null
            }
        }
    }

    fun part2(): Int {
        return execute(
            inputArr,
            ::adjacentOccupied2
        ) { value, occupied ->
            when {
                value == 'L' && occupied == 0 -> '#'
                value == '#' && occupied >= 5 -> 'L'
                else -> null
            }
        }
    }

    private fun execute(
        array: Array<Array<Char>>,
        occupiedFunc: (Array<Array<Char>>, Point2D) -> Int,
        rule: (Char?, Int) -> Char?
    ): Int {
        var old = array
        while (true) {
            val new = old.applyRule(occupiedFunc, rule)
            if (old.contentDeepEquals(new)) {
                break
            }
            old = new
        }
        return old.flatMap { it.toList() }.count { it == '#' }
    }

    private fun Array<Array<Char>>.applyRule(
        occupiedFunc: (Array<Array<Char>>, Point2D) -> Int,
        rule: (Char?, Int) -> Char?
    ): Array<Array<Char>> {
        val copy = deepCopy()
        positions().forEach { pos ->
            val value = getOrNull(pos)
            val occupied = occupiedFunc(this, pos)
            rule(value, occupied)?.also { copy[pos] = it }
        }
        return copy
    }

    private fun adjacentOccupied(arr: Array<Array<Char>>, pos: Point2D): Int {
        return pos.adjacent().map {
            arr.getOrNull(it)
        }.count { it == '#' }
    }

    private fun adjacentOccupied2(arr: Array<Array<Char>>, pos: Point2D): Int {
        return Move.all().map { move ->
            pos.keepMoving(move).map { arr.getOrNull(it) }.first { it != '.' }
        }.count { it == '#' }
    }
}
