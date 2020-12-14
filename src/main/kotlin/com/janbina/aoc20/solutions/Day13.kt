package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(13)
    Day13(input).also {
        printAndTest(it.part1(), 2545)
        printAndTest(it.part2(), 266204454441577)
    }
}

class Day13(inputLines: List<String>) {

    private val earliest = inputLines.first().toInt()
    private val mapped = inputLines[1].split(",").withIndex().filter { it.value != "x" }
    private val ids = mapped.map { it.value.toInt() }
    private val offsets = mapped.map { it.index }

    fun part1(): Int {
        val departures = ids.map {
            if (earliest % it == 0) {
                earliest
            } else {
                earliest / it * it + it
            }
        }
        val i = departures.withIndex().minByOrNull { it.value }?.index ?: 0
        return ids[i] * (departures[i] - earliest)
    }

    fun part2(): Long {
        val n = ids.map { it.toLong() }.toLongArray()
        val a = n.zip(offsets).map { it.first - it.second }.toLongArray()
        return chineseRemainder(n, a)
    }

    // Chinese remainder algorithm taken from here: https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
    private fun chineseRemainder(n: LongArray, a: LongArray): Long {
        require(n.size == a.size)
        val prod = n.reduce { acc, l -> acc * l }
        var sum = 0L
        for (i in n.indices) {
            val p = prod / n[i]
            sum += a[i] * multInv(p, n[i]) * p
        }
        return sum % prod
    }

    /* returns x where (a * x) % b == 1 */
    private fun multInv(a: Long, b: Long): Long {
        if (b == 1L) return 1
        var aa = a
        var bb = b
        var x0 = 0L
        var x1 = 1L
        while (aa > 1) {
            val q = aa / bb
            var t = bb
            bb = aa % bb
            aa = t
            t = x0
            x0 = x1 - q * x0
            x1 = t
        }
        if (x1 < 0) x1 += b
        return x1
    }
}
