package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputText(6)
    Day06(input).also {
        printAndTest(it.part1(), 6549)
        printAndTest(it.part2(), 3466)
    }
}

class Day06(input: String) {

    private val groups = input.split("\n\n").map {
        it.split("\n").filter(String::isNotBlank).map(String::toSet)
    }

    fun part1(): Int {
        return groups.sumBy {
            it.reduce { a, b -> a.union(b) }.size
        }
    }

    fun part2(): Int {
        return groups.sumBy {
            it.reduce { a, b -> a.intersect(b) }.size
        }
    }
}
