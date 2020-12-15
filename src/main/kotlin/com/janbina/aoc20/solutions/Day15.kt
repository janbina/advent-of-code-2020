package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(15)
    Day15(input).also {
        printAndTest(it.part1(), 319)
        printAndTest(it.part2(), 2424)
    }
}

class Day15(inputLines: List<String>) {

    private val nums = inputLines.first().split(",").map { it.toInt() }

    fun part1(): Int {
        return playGame(nums).take(2020).last()
    }

    fun part2(): Int {
        return playGame(nums).take(30000000).last()
    }

    private fun playGame(init: List<Int>): Sequence<Int> = sequence {
        yieldAll(init)
        val map = init.dropLast(1).withIndex().associate { it.value to it.index }.toMutableMap()
        var last = init.last()
        var i = init.lastIndex

        while (true) {
            val prev = map[last]
            map[last] = i

            val new = if (prev != null) {
                i - prev
            } else {
                0
            }

            yield(new)
            last = new
            i++
        }
    }
}
