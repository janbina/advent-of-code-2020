package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(17)
    Day17(input).also {
        printAndTest(it.part1(), 324)
        printAndTest(it.part2(), 1836)
    }
}

class Day17(inputLines: List<String>) {

    private val input = inputLines.mapIndexed { x, line ->
        line.mapIndexed { y, c ->
            if (c == '#') {
                Point4D(x, y, 0, 0)
            } else null
        }.filterNotNull()
    }.flatten().toSet()

    fun part1(): Int {
        return bootUp(Point4D::adjacent3D)
    }

    fun part2(): Int {
        return bootUp(Point4D::adjacent4D)
    }

    private fun bootUp(adjacentFunc: (Point4D) -> List<Point4D>): Int {
        var set = input

        repeat(6) {
            val newSet = mutableSetOf<Point4D>()

            set.map { adjacentFunc(it) + it }.flatten().distinct().forEach { candidate ->
                val activeAdjacent = adjacentFunc(candidate).count { set.contains(it) }
                if (set.contains(candidate) && activeAdjacent in 2..3) {
                    newSet.add(candidate)
                }
                if (set.contains(candidate).not() && activeAdjacent == 3) {
                    newSet.add(candidate)
                }
            }
            
            set = newSet
        }

        return set.size
    }

    private data class Point4D(val a: Int, val b: Int, val c: Int, val d: Int) {
        fun adjacent3D(): List<Point4D> {
            val list = ArrayList<Point4D>(80)
            for (i in a-1..a+1) {
                for (j in b-1..b+1) {
                    for (k in c-1..c+1) {
                        if (i != a || j != b || k != c) {
                            list.add(Point4D(i, j, k, 0))
                        }
                    }
                }
            }
            return list
        }

        fun adjacent4D(): List<Point4D> {
            val list = ArrayList<Point4D>(80)
            for (i in a-1..a+1) {
                for (j in b-1..b+1) {
                    for (k in c-1..c+1) {
                        for (l in d-1..d+1) {
                            if (i != a || j != b || k != c || l != d) {
                                list.add(Point4D(i, j, k, l))
                            }
                        }
                    }
                }
            }
            return list
        }
    }
}
