package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.*

fun main() {
    val input = Input.getDayInputLines(23)

    Day23(input).also {
        printAndTest(it.part1(), "62934785")
        printAndTest(it.part2(), 693659135400)
    }
}

class Day23(inputLines: List<String>) {

    private val input = inputLines.first().map { "$it".toInt() }

    fun part1(): String {
        val game = CupsGame(input)
        game.play(moves = 100)
        return game.getCurrentState(startAt = 1).drop(1).joinToString("")
    }

    fun part2(): Long {
        val game = CupsGame(input + ((input.max() + 1)..1_000_000))
        game.play(moves = 10_000_000)
        return game.getCurrentState(startAt = 1).drop(1).take(2).product()
    }

    private class CupsGame(initElems: List<Int>) {
        private val map = initElems.associateWith { Node(it, null) }
        private val max = initElems.max()
        private var current = map.getOrErr(initElems.first())

        init {
            initElems.forEachIndexed { index, i ->
                val next = if (index == initElems.lastIndex) initElems[0] else initElems[index + 1]
                map.getOrErr(i).next = map[next]
            }
        }

        fun getCurrentState(startAt: Int): Sequence<Int> {
            return map.getOrErr(startAt).sequence().take(map.size).map { it.value }
        }

        fun play(moves: Int) {
            repeat(moves) {
                executeMove(current)
                current = requireNotNull(current.next)
            }
        }

        private fun executeMove(current: Node) {
            val first = requireNotNull(current.next)
            val second = requireNotNull(first.next)
            val third = requireNotNull(second.next)
            current.next = third.next

            val insertionElem = listOf(
                current.value - 1,
                current.value - 2,
                current.value - 3,
                current.value - 4,
            ).map { if (it <= 0) it + max else it }
                .first { it != first.value && it != second.value && it != third.value }
            val insertionNode = map.getOrErr(insertionElem)

            third.next = insertionNode.next
            insertionNode.next = first
        }

        private fun Node.sequence() = sequence<Node> {
            var current: Node? = this@sequence
            while (current != null) {
                yield(current)
                current = current.next
            }
        }

        private data class Node(
            val value: Int,
            var next: Node?,
        )
    }
}
