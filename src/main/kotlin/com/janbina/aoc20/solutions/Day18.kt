package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import java.util.*

fun main() {
    val input = Input.getDayInputLines(18)
    Day18(input).also {
        printAndTest(it.part1(), 4297397455886)
        printAndTest(it.part2(), 93000656194428)
    }
}

class Day18(inputLines: List<String>) {

    private val input = inputLines.map { it.tokenize().toList() }

    fun part1(): Long {
        return input.map {
            it.constructAst { 1 }.evaluate()
        }.sum()
    }

    fun part2(): Long {
        return input.map {
            it.constructAst { token ->
                when (token) {
                    Token.OpPlus -> 3
                    Token.OpMult -> 2
                    else -> 1
                }
            }.evaluate()
        }.sum()
    }

    private fun String.tokenize(): Sequence<Token> = sequence {
        val str = this@tokenize
        var index = 0

        while (index < length) {
            when (str[index]) {
                '(' -> yield(Token.LPar)
                ')' -> yield(Token.RPar)
                '+' -> yield(Token.OpPlus)
                '*' -> yield(Token.OpMult)
                in '0'..'9' -> {
                    val num = str.substring(index).takeWhile { it.isDigit() }
                    yield(Token.Num(num.toLong()))
                    index += num.length - 1
                }
            }
            index++
        }
    }

    private fun ArrayDeque<AstNode>.addOperatorNode(token: Token) {
        // all our operators have two arguments
        addLast(AstNode(token, removeLast(), removeLast()))
    }

    private fun List<Token>.constructAst(precedenceFunc: (Token) -> Int): AstNode {
        val ops = ArrayDeque<Token>()
        val nodes = ArrayDeque<AstNode>()

        forEach { token ->
            when (token) {
                is Token.Num -> nodes.addLast(AstNode(token))
                Token.LPar -> ops.addLast(token)
                Token.RPar -> {
                    while (ops.isNotEmpty() && ops.last != Token.LPar) {
                        nodes.addOperatorNode(ops.removeLast())
                    }
                    ops.removeLast()
                }
                Token.OpPlus, Token.OpMult -> {
                    while (ops.isNotEmpty() && precedenceFunc(ops.last) >= precedenceFunc(token) && ops.last != Token.LPar) {
                        nodes.addOperatorNode(ops.removeLast())
                    }
                    ops.addLast(token)
                }
            }
        }

        if (ops.contains(Token.LPar)) error("Expression contains unclosed parenthesis")

        ops.descendingIterator().forEach {
            nodes.addOperatorNode(it)
        }

        require(nodes.size == 1)
        return nodes.first
    }

    sealed class Token {
        object OpPlus : Token()
        object OpMult : Token()
        object LPar : Token()
        object RPar : Token()
        data class Num(val value: Long) : Token()
    }

    data class AstNode(
        val token: Token,
        val left: AstNode? = null,
        val right: AstNode? = null,
    ) {
        fun evaluate(): Long = when (token) {
            is Token.Num -> token.value
            Token.OpPlus -> requireNotNull(left).evaluate() + requireNotNull(right).evaluate()
            Token.OpMult -> requireNotNull(left).evaluate() * requireNotNull(right).evaluate()
            else -> error("Invalid token: $token")
        }
    }
}
