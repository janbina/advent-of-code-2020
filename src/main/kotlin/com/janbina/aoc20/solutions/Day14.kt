package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import com.janbina.aoc20.utils.setBit
import com.janbina.aoc20.utils.unsetBit
import kotlin.math.pow

fun main() {
    val input = Input.getDayInputLines(14)
    Day14(input).also {
        printAndTest(it.part1(), 17765746710228)
        printAndTest(it.part2(), 4401465949086)
    }
}

class Day14(inputLines: List<String>) {

    private val input = inputLines.map { createInstruction(it) }

    fun part1(): Long {
        val mem = mutableMapOf<Long, Long>()
        var mask = ""

        input.forEach {
            when (it) {
                is Instruction.Mask -> mask = it.value
                is Instruction.Mem -> mem[it.addr] = it.value.applyMask1(mask)
            }
        }

        return mem.values.sum()
    }

    fun part2(): Long {
        val mem = mutableMapOf<Long, Long>()
        var mask = ""

        input.forEach {
            when (it) {
                is Instruction.Mask -> mask = it.value
                is Instruction.Mem -> {
                    it.addr.applyMask2(mask).forEach { maskedAddr ->
                        mem[maskedAddr] = it.value
                    }
                }
            }
        }

        return mem.values.sum()
    }

    private fun Long.applyMask1(mask: String): Long {
        var res = this
        mask.forEachIndexed { index, c ->
            val shift = mask.length - 1 - index
            res = when (c) {
                '0' -> res.unsetBit(shift)
                '1' -> res.setBit(shift)
                else -> res
            }
        }
        return res
    }

    private fun Long.applyMask2(mask: String): Sequence<Long> = sequence {
        var res = this@applyMask2
        val floating = mutableListOf<Int>()

        mask.forEachIndexed { index, c ->
            val shift = mask.length - 1 - index
            if (c == '1') {
                res = res.setBit(shift)
            } else if (c == 'X') {
                floating.add(shift)
            }
        }

        for (i in 0 until 2.0.pow(floating.size).toInt()) {
            var xres = res
            floating.forEachIndexed { index, shift ->
                xres = if (i and (1 shl index) > 0) {
                    xres.setBit(shift)
                } else {
                    xres.unsetBit(shift)
                }
            }
            yield(xres)
        }
    }

    private fun createInstruction(line: String): Instruction {
        val split = line.split(" = ")
        return if (split[0] == "mask") {
            Instruction.Mask(split[1])
        } else {
            Instruction.Mem(
                split[0].filter { it.isDigit() }.toLong(),
                split[1].toLong()
            )
        }
    }

    sealed class Instruction {
        data class Mem(
            val addr: Long,
            val value: Long,
        ) : Instruction()

        data class Mask(
            val value: String,
        ) : Instruction()
    }
}
