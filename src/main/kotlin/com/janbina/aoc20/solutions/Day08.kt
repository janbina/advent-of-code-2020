package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(8)
    Day08(input).also {
        printAndTest(it.part1(), 1818)
        printAndTest(it.part2(), 631)
    }
}

class Day08(input: List<String>) {

    private val instructions = input.map {
        val s = it.split(" ")
        Instruction(s[0], s[1].toInt())
    }

    fun part1(): Int {
        return runProgram(instructions).acc
    }

    fun part2(): Int {
        instructions.forEachIndexed { i, inst ->
            when (inst.name) {
                "jmp" -> instructions.toMutableList().also { it[i] = Instruction("nop", inst.value) }
                "nop" -> instructions.toMutableList().also { it[i] = Instruction("jmp", inst.value) }
                else -> null
            }?.let {
                val res = runProgram(it)
                if (res.ip == instructions.size) {
                    return res.acc
                }
            }
        }
        error("No result found")
    }

    private fun runProgram(instructions: List<Instruction>): ProgramResult {
        val exec = mutableSetOf<Int>()
        var acc = 0
        var ip = 0

        while (true) {
            if (ip !in instructions.indices || ip in exec) {
                return ProgramResult(ip, acc)
            }
            exec.add(ip)
            val i = instructions[ip]
            when (i.name) {
                "jmp" -> ip += i.value
                "nop" -> ip++
                "acc" -> {
                    acc += i.value
                    ip++
                }
                else -> error("Invalid instruction")
            }
        }
    }

    private data class ProgramResult(
        val ip: Int,
        val acc: Int,
    )

    private data class Instruction(
        val name: String,
        val value: Int,
    )
}
