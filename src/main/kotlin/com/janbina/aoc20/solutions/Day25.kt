package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(25)
    Day25(input).also {
        printAndTest(it.part1(), 4126980)
    }
}

class Day25(inputLines: List<String>) {

    private val pk1 = inputLines[0].toInt()
    private val pk2 = inputLines[1].toInt()

    fun part1(): Int {
        return transform(
            pk2,
            loopSize = transform(7, targetNumber = pk1).first
        ).second
    }

    private fun transform(
        subjectNumber: Int,
        loopSize: Int = Int.MAX_VALUE,
        targetNumber: Int = -1,
    ): Pair<Int, Int> {
        var x = 1L
        repeat(loopSize) {
            if (x.toInt() == targetNumber) {
                return it to targetNumber
            }
            x = x * subjectNumber % 20201227
        }
        return loopSize to x.toInt()
    }
}
