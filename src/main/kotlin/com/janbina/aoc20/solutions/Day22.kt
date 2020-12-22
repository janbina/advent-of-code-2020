package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(22)
    Day22(input).also {
        printAndTest(it.part1(), 32401)
        printAndTest(it.part2(), 31436)
    }
}

class Day22(inputLines: List<String>) {

    private val player1 = inputLines.drop(1).takeWhile { it.isNotBlank() }.map { it.toInt() }
    private val player2 = inputLines.dropWhile { it != "Player 2:" }.drop(1).map { it.toInt() }

    fun part1(): Int {
        val res =  playGame(player1, player2).let { it.first + it.second }
        return res.mapIndexed { index, i -> i * (res.size - index) }.sum()
    }

    fun part2(): Int {
        val res = playRecursiveGame(player1, player2).let { it.first + it.second }
        return res.mapIndexed { index, i -> i * (res.size - index) }.sum()
    }

    private fun playGame(deck1Init: List<Int>, deck2Init: List<Int>): Pair<List<Int>, List<Int>> {
        val deck1 = ArrayDeque(deck1Init)
        val deck2 = ArrayDeque(deck2Init)

        while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
            val a = deck1.removeFirst()
            val b = deck2.removeFirst()
            if (a > b) {
                deck1.addAll(listOf(a, b))
            } else {
                deck2.addAll(listOf(b, a))
            }
        }

        return deck1.toList() to deck2.toList()
    }

    private fun playRecursiveGame(deck1Init: List<Int>, deck2Init: List<Int>): Pair<List<Int>, List<Int>> {
        val deck1 = ArrayDeque(deck1Init)
        val deck2 = ArrayDeque(deck2Init)
        val seenConfigs = mutableSetOf<String>()

        while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
            val config = deck1.joinToString() + "|" + deck2.joinToString()
            if (seenConfigs.contains(config)) {
                return listOf(1) to emptyList()
            }
            seenConfigs.add(config)
            val a = deck1.removeFirst()
            val b = deck2.removeFirst()
            if (deck1.size >= a && deck2.size >= b) {
                val res = playRecursiveGame(deck1.take(a), deck2.take(b))
                if (res.first.isNotEmpty()) {
                    deck1.addAll(listOf(a, b))
                } else {
                    deck2.addAll(listOf(b, a))
                }
            } else if (a > b) {
                deck1.addAll(listOf(a, b))
            } else {
                deck2.addAll(listOf(b, a))
            }
        }

        return deck1.toList() to deck2.toList()
    }
}
