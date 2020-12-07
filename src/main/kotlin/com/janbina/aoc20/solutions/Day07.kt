package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(7)
    Day07(input).also {
        printAndTest(it.part1(), 112)
        printAndTest(it.part2(), 6260)
    }
}

class Day07(input: List<String>) {

    private val myBagName = "shiny gold"
    private val trees: Map<String, Bag>

    init {
        val mutTrees = mutableMapOf<String, Bag>()

        val bags = input.map {
            createBag(it)
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

    private fun createBag(line: String): Bag {
        val name = line.substringBefore(" bag")
        val rest = line.substringAfter("contain ")
        val contains = if (rest.contains("no other")) {
            emptyList()
        } else {
            rest.split(",")
                .map {
                    val numName = it.substringBefore(" bag").trim()
                    val num = numName.takeWhile { it.isDigit() }.toInt()
                    val nm = numName.substringAfter(" ")
                    num to Bag(nm, emptyList())
                }
        }
        return Bag(name, contains)
    }

    private data class Bag(
        val name: String,
        val contains: List<Pair<Int, Bag>>
    )
}
