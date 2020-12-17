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

class Day17(
    private val inputLines: List<String>
) {

    fun part1(): Int {
        var map = mutableMapOf<Point3D, Boolean>()

        inputLines.forEachIndexed { x, line ->
            line.forEachIndexed { y, c ->
                if (c == '#') {
                    map[Point3D(x, y, 0)] = true
                }
            }
        }

        repeat(6) {
            val newMap = mutableMapOf<Point3D, Boolean>()

            map.keys.map { it.adjacent() + it }.flatten().distinct().forEach {
                val activeAdjacent = it.adjacent().map { map[it] }.count { it == true }
                if (map[it] == true && activeAdjacent in 2..3) {
                    newMap[it] = true
                } else if (map[it] != true && activeAdjacent == 3) {
                    newMap[it] = true
                }
            }
            map = newMap
        }

        return map.count { it.value }
    }

    fun part2(): Int {
        var map = mutableMapOf<Point4D, Boolean>()

        inputLines.forEachIndexed { x, line ->
            line.forEachIndexed { y, c ->
                if (c == '#') {
                    map[Point4D(x, y, 0, 0)] = true
                }
            }
        }

        repeat(6) {
            val newMap = mutableMapOf<Point4D, Boolean>()

            map.keys.map { it.adjacent() + it }.flatten().distinct().forEach {
                val activeAdjacent = it.adjacent().map { map[it] }.count { it == true }
                if (map[it] == true && activeAdjacent in 2..3) {
                    newMap[it] = true
                } else if (map[it] != true && activeAdjacent == 3) {
                    newMap[it] = true
                }
            }
            map = newMap
        }

        return map.count { it.value }
    }

    private data class Point3D(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {

        fun adjacent(): List<Point3D> {
            val list = ArrayList<Point3D>(26)

            for (i in x-1..x+1) {
                for (j in y-1..y+1) {
                    for (k in z-1..z+1) {
                        if (i != x || j != y || k != z) {
                            list.add(Point3D(i, j, k))
                        }
                    }
                }
            }

            return list
        }

    }

    private data class Point4D(
        val x: Int,
        val y: Int,
        val z: Int,
        val w: Int,
    ) {

        fun adjacent(): List<Point4D> {
            val list = ArrayList<Point4D>(80)

            for (i in x-1..x+1) {
                for (j in y-1..y+1) {
                    for (k in z-1..z+1) {
                        for (l in w-1..w+1) {
                            if (i != x || j != y || k != z || l != w) {
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
