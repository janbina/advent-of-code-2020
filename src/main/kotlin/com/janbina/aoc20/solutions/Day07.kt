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

    private val bags = input.map {
        createBag(it)
    }.associate { it.name to it.contains }

    private val trees = mutableMapOf<String, Bag>()
    private val roots = mutableSetOf<String>()

    init {
        fun buildTree(name: String): Bag {
            val t = trees[name]
            if (t != null) return t
            val subtrees = bags[name]?.map {
                val tx = buildTree(it.second.name)
                roots.remove(it.second.name)
                it.first to tx
            } ?: emptyList()
            val tree = Bag(name, subtrees)
            trees[name] = tree
            roots.add(name)
            return tree
        }

        bags.forEach { name, _ ->
            buildTree(name)
        }
    }

    fun part1(): Int {
        val cache = mutableMapOf<String, Boolean>()
        fun Bag.canHold(name: String): Boolean {
            val c = cache[this.name]
            if (c != null) return c
            val r = this.contains.any { it.second.name == name || it.second.canHold(name) }
            cache[this.name] = r
            return r
        }

        return trees.count { (_, bag) ->
            bag.canHold("shiny gold")
        }
    }

    fun part2(): Int {
        val cache = mutableMapOf<String, Int>()
        fun Bag.numContains(): Int {
            val c = cache[this.name]
            if (c != null) return c
            val r = this.contains.sumBy {
                it.first * (it.second.numContains() + 1)
            }
            cache[this.name] = r
            return r
        }

        return trees["shiny gold"]!!.numContains()
    }

    private fun createBag(line: String): Bag {
        val name = line.substringBefore(" bag")
        val rest = line.substringAfter("contain ")
        val contains = mutableListOf<Pair<Int, Bag>>()
        if (!rest.contains("no other")) {
            rest.split(",")
                .map { it.substringBefore(" bag").trim() }
                .forEach {
                    val num = it.takeWhile { it.isDigit() }.toInt()
                    val nm = it.dropWhile { it.isLetter().not() }
                    contains.add(num to Bag(nm, emptyList()))
                }
        }
        return Bag(name, contains)
    }

    private data class Bag(
        val name: String,
        val contains: List<Pair<Int, Bag>>
    )
}
