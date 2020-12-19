package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import kotlin.math.max

fun main() {
    val input = Input.getDayInputLines(19)
    Day19(input).also {
        printAndTest(it.part1(), 190)
        printAndTest(it.part2(), 311)
    }
}

class Day19(inputLines: List<String>) {

    private val rules = inputLines.takeWhile { it.isNotBlank() }.toRules()
    private val messages = inputLines.dropWhile { it.firstOrNull()?.isLetter() != true }

    fun part1(): Int {
        return messages.count { cyk(it, rules) }
    }

    fun part2(): Any {
        // add rules "8: 42 8" and "11: 42 11 31"
        val maxRuleId = max(rules.nonTerminal.maxOf { it.lhs }, rules.terminal.maxOf { it.lhs })
        val rulesP2 = Rules(
            rules.terminal,
            rules.nonTerminal + listOf(
                NonTerminalRule(8, listOf(42, 8)),
                NonTerminalRule(11, listOf(42, maxRuleId + 1)),
                NonTerminalRule(maxRuleId + 1, listOf(11, 31)),
            )
        )
        return messages.count { cyk(it, rulesP2) }
    }

    private fun cyk(msg: String, rules: Rules): Boolean {
        val n = msg.length
        val arr = Array(n + 1) { Array(n + 1) { BooleanArray(rules.size + 1) } }

        msg.forEachIndexed { index, c ->
            rules.terminal.forEach {
                if (it.rhs == c) {
                    arr[1][index+1][it.lhs] = true
                }
            }
        }

        for (l in 2..n) {
            for (s in 1..n-l+1) {
                for (p in 1..l-1) {
                    rules.nonTerminal.forEach { rule ->
                        if (arr[p][s][rule.rhs.first()] && arr[l-p][s+p][rule.rhs[1]]) {
                            arr[l][s][rule.lhs] = true
                        }
                    }
                }
            }
        }
        return arr[n][1][0]
    }

    private fun List<String>.toRules(): Rules {
        val terminal = mutableListOf<TerminalRule>()
        val nonTerminal = mutableListOf<NonTerminalRule>()

        forEach { line ->
            val (lhs, rhs) = line.split(":")
            if (rhs.contains("\"")) {
                terminal.add(
                    TerminalRule(lhs.toInt(), rhs.first { it.isLetter() })
                )
            } else {
                rhs.trim()
                    .split(" | ")
                    .map { it.trim().split(" ").map(String::toInt) }
                    .forEach {
                        nonTerminal.add(NonTerminalRule(lhs.toInt(), it))
                    }
            }
        }

        return Rules(terminal, nonTerminal).chomsky()
    }

    private fun Rules.chomsky(): Rules {
        val terminal = this.terminal.toMutableList()
        val nonTerminal = this.nonTerminal.filter { it.rhs.size == 2 }.toMutableList()

        // all bad rules we deal with are in form A -> B
        this.nonTerminal.filter { it.rhs.size == 1 }.forEach { rule ->
            this.nonTerminal.filter { it.lhs == rule.rhs.first() }.forEach {
                nonTerminal.add(NonTerminalRule(rule.lhs, it.rhs))
            }
            this.terminal.filter { it.lhs == rule.rhs.first() }.forEach {
                terminal.add(TerminalRule(rule.lhs, it.rhs))
            }
        }

        return Rules(terminal, nonTerminal)
    }

    private class Rules(
        val terminal: List<TerminalRule>,
        val nonTerminal: List<NonTerminalRule>,
    ) {
        val size = terminal.size + nonTerminal.size
    }

    private data class TerminalRule(
        val lhs: Int,
        val rhs: Char
    )

    private data class NonTerminalRule(
        val lhs: Int,
        val rhs: List<Int>
    )
}
