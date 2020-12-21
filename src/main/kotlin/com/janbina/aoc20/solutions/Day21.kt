package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest

fun main() {
    val input = Input.getDayInputLines(21)
    Day21(input).also {
        printAndTest(it.part1(), 1685)
        printAndTest(it.part2(), "ntft,nhx,kfxr,xmhsbd,rrjb,xzhxj,chbtp,cqvc")
    }
}

class Day21(inputLines: List<String>) {

    private val food = inputLines.map { it.toFood() }
    private val allIngredients = food.map { it.ingredients }.flatten().toSet()
    private val allAllergens = food.map { it.allergens }.flatten().toSet()

    fun part1(): Int {
        val allsToIngrs = allAllergens.associateWith { allIngredients }.toMutableMap()

        food.forEach { f ->
            f.allergens.forEach {
                allsToIngrs[it] = allsToIngrs.getOrElse(it) { emptySet() }.intersect(f.ingredients)
            }
        }

        val hasAllergens = allsToIngrs.values.flatten().toSet()
        val allergenFree = allIngredients - hasAllergens

        return food.sumBy { f ->
            f.ingredients.count { it in allergenFree }
        }
    }

    fun part2(): String {
        val allsToIngrs = allAllergens.associateWith { allIngredients }.toMutableMap()

        food.forEach { f ->
            f.allergens.forEach {
                allsToIngrs[it] = allsToIngrs.getOrElse(it) { emptySet() }.intersect(f.ingredients)
            }
        }

        while (allsToIngrs.any { it.value.size != 1 }) {
            val decided = allsToIngrs.filter { it.value.size == 1 }.values.flatten().toSet()
            allsToIngrs.filter { it.value.size > 1 }.forEach { (key, set) ->
                allsToIngrs[key] = set - decided
            }
        }


        return allsToIngrs.toSortedMap().values.joinToString(",") { it.first() }
    }

    private fun String.toFood(): Food {
        val ingredients = this.substringBefore('(').trim()
            .split(" ").filter { it.isNotBlank() }.toSet()
        val allergens = this.substringAfter("(contains ").substringBefore(')').trim()
            .split(", ").filter { it.isNotBlank() }.toSet()
        return Food(ingredients, allergens)
    }

    private data class Food(
        val ingredients: Set<String>,
        val allergens: Set<String>,
    )
}
