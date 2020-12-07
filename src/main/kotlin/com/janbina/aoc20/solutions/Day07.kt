package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    val input = Input.getDayInputLines(7)

    measureTime {
        Day07(input).also {
            printAndTest(it.part1(), 112)
            printAndTest(it.part2(), 6260)
        }
    }.also { println("Time 1 = $it") }

    measureTime {
        Day07x(input).also {
            printAndTest(it.part1(), 112)
            printAndTest(it.part2(), 6260)
        }
    }.also { println("Time 2 = $it") }
}

class Day07(input: List<String>) {

    private val myBagName = "shiny gold"
    private val trees: Map<String, Bag>

    init {
        val mutTrees = mutableMapOf<String, Bag>()

        val bags = input.map {
            Bag.fromString(it)
        }.associate { it.name to it.contains }

        fun buildTree(name: String): Bag {
            return mutTrees.getOrPut(name) {
                val subtrees = bags[name]?.map {
                    val tree = buildTree(it.second.name)
                    it.first to tree
                } ?: emptyList()
                Bag(name, subtrees)
            }
        }

        bags.forEach { (name, _) ->
            buildTree(name)
        }

        trees = mutTrees
    }

    fun part1(): Int {
        val cache = mutableMapOf<String, Boolean>()

        fun Bag.canHold(what: String): Boolean {
            return cache.getOrPut(name) {
                contains.any { it.second.name == what || it.second.canHold(what) }
            }
        }

        return trees.count { (_, bag) ->
            bag.canHold(myBagName)
        }
    }

    fun part2(): Int {
        val cache = mutableMapOf<String, Int>()

        fun Bag.numContains(): Int {
            return cache.getOrPut(name) {
                contains.sumBy {
                    it.first * (it.second.numContains() + 1)
                }
            }
        }

        return trees[myBagName]?.numContains() ?: error("There's no bag named \"$myBagName\"")
    }

    data class Bag(
        val name: String,
        val contains: List<Pair<Int, Bag>>
    ) {
        companion object {
            private val linePattern = """(\w+ \w+) bags contain (.*)""".toRegex()
            private val itemPattern = """(\d+) (\w+ \w+) bags?""".toRegex()

            fun fromString(line: String): Bag {
                val (name, rest) = linePattern.matchEntire(line)!!.destructured
                val contains = itemPattern.findAll(rest).map { match ->
                    val (c, n) = match.destructured
                    c.toInt() to Bag(n, emptyList())
                }.toList()
                return Bag(name, contains)
            }
        }
    }
}

// easier and more concise solution, without all the caching that should take more time for larger input
// for provided input, the difference is like 55ms to 550ms
class Day07x(input: List<String>) {

    private val myBagName = "shiny gold"
    private val trees: List<Day07.Bag>

    init {
        val bags = input.map { Day07.Bag.fromString(it) }

        fun buildTree(name: String): Day07.Bag {
            val subtrees = bags.first { it.name == name }.contains.map {
                it.first to buildTree(it.second.name)
            }
            return Day07.Bag(name, subtrees)
        }

        trees = bags.map { buildTree(it.name) }
    }

    fun part1(): Int {
        return trees.count {
            it.canHold(myBagName)
        }
    }

    fun part2(): Int {
        return trees.first { it.name == myBagName }.numContains()
    }

    private fun Day07.Bag.canHold(what: String): Boolean {
        return contains.any { it.second.name == what || it.second.canHold(what) }
    }

    private fun Day07.Bag.numContains(): Int {
        return contains.sumBy {
            it.first * (it.second.numContains() + 1)
        }
    }
}
