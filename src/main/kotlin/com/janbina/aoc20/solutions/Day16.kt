package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import com.janbina.aoc20.utils.product

fun main() {
    val input = Input.getDayInputLines(16)
    Day16(input).also {
        printAndTest(it.part1(), 21071)
        printAndTest(it.part2(), 3429967441937)
    }
}

class Day16(inputLines: List<String>) {

    private val rules = inputLines.takeWhile { it.isNotBlank() }.map { createRule(it) }
    private val myTicket = inputLines
        .dropWhile { !it.contains("your ticket") }[1]
        .split(",")
        .map { it.toInt() }
    private val nearbyTickets = inputLines
        .dropWhile { !it.contains("nearby tickets") }
        .drop(1)
        .map { it.split(",").map(String::toInt) }

    fun part1(): Int {
        return nearbyTickets.map { it.getInvalidFields(rules) }.flatten().sum()
    }

    fun part2(): Long {
        val couldBe = Array(myTicket.size) {
            rules.map { it.name }.toMutableSet()
        }

        nearbyTickets.filter { !it.isInvalid(rules) }.forEach { ticket ->
            ticket.forEachIndexed { index, value ->
                rules.forEach { rule ->
                    if (!rule.contains(value)) {
                        couldBe[index].remove(rule.name)
                    }
                }
            }
        }

        val nameToIndex = mutableMapOf<String, Int>()

        while (nameToIndex.size < rules.size) {
            couldBe.forEachIndexed { index, set ->
                set.minus(nameToIndex.keys).let {
                    if (it.size == 1) {
                        nameToIndex[it.first()] = index
                    }
                }
            }
        }

        return nameToIndex.filterKeys { it.startsWith("departure") }
            .map { myTicket[it.value] }
            .product()
    }

    private fun List<Int>.getInvalidFields(rules: List<Rule>): List<Int> {
        return this.filter { value -> rules.all { !it.contains(value) } }
    }

    private fun List<Int>.isInvalid(rules: List<Rule>): Boolean {
        return this.getInvalidFields(rules).isNotEmpty()
    }

    private fun createRule(line: String): Rule {
        val name = line.substringBefore(":")
        val ranges = rangeRegex.findAll(line).map {
            val (from, to) = it.value.split("-")
            from.toInt()..to.toInt()
        }
        return Rule(name, ranges.toList())
    }

    private data class Rule(
        val name: String,
        val ranges: List<IntRange>,
    ) {
        fun contains(value: Int): Boolean {
            return ranges.any { it.contains(value) }
        }
    }

    companion object {
        private val rangeRegex = """(\d+-\d+)""".toRegex()
    }
}
