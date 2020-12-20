package com.janbina.aoc20.solutions

import com.janbina.aoc20.utils.Input
import com.janbina.aoc20.utils.printAndTest
import com.janbina.aoc20.utils.product
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    val input = Input.getDayInputText(20)
    measureTime {
        Day20(input).also {
            printAndTest(it.part1(), 20913499394191)
            printAndTest(it.part2(), 2209)
        }
    }.also {
        println(it)
    }
}

class Day20(input: String) {

    private val tiles = input.split("\n\n").map { it.toTile().allConfigs() }.flatten().also { tiles ->
        tiles.forEach { it.findAdjacent(tiles) }
    }
    private val monster = listOf(
        "..................#.",
        "#....##....##....###",
        ".#..#..#..#..#..#...",
    )

    fun part1(): Long {
        return tiles.filter { it.adjacentCount == 2 }.map { it.id }.distinct().product()
    }

    fun part2(): Any {
        val imgSize = sqrt(tiles.size.toFloat() / 8).toInt()
        val img = Array(imgSize) { Array<Tile?>(imgSize) { null } }

        // find top left corner - tile that has two adjacent, right and bottom
        img[0][0] = tiles.first { it.adjacentCount == 2 && it.right != null && it.bottom != null }

        // fill first row by matching right border of the previous tile
        for (i in 1 until imgSize) {
            val prev = img[0][i-1]!!
            img[0][i] = tiles.first { current ->
                current.id == prev.right!!.id && current.leftBorder contentEquals prev.rightBorder
            }
        }
        // fill all other rows by matching bottom border of tiles in previous row
        for (j in 1 until imgSize) {
            for (i in 0 until imgSize) {
                val prev = img[j-1][i]!!
                img[j][i] = tiles.first { current ->
                    current.id == prev.bottom!!.id && current.topBorder contentEquals prev.bottomBorder
                }
            }
        }

        val borderlessTileSize = tiles.first().topBorder.size - 2
        val realImgSize = imgSize * borderlessTileSize
        val realImg = Array(realImgSize) { BooleanArray(realImgSize) }

        // create the real image
        for (i in 0 until imgSize) {
            for (j in 0 until imgSize) {
                img[i][j]!!.borderLess.forEachIndexed { i2, booleans ->
                    booleans.forEachIndexed { j2, b ->
                        realImg[i * borderlessTileSize + i2][j * borderlessTileSize + j2] = b
                    }
                }
            }
        }

        val totalHashes = realImg.sumBy { it.count { it } }
        val monsterHashes = monster.sumBy { it.count { it == '#' } }
        val monsterCount = realImg.allConfigs().map { it.countMonsters() }.first { it > 0 }

        return totalHashes - monsterCount * monsterHashes
    }

    private fun String.toTile(): Tile {
        val lines = this.split("\n").filter { it.isNotBlank() }
        val id = lines.first().filter { it.isDigit() }.toInt()
        val data = Array(lines.size - 1) { y ->
            BooleanArray(lines[1].length) { x -> lines[y+1][x] == '#' }
        }
        return Tile(id, data)
    }

    private data class Tile(
        val id: Int,
        val data: Array<BooleanArray>,
    ) {
        var left: Tile? = null
        var top: Tile? = null
        var right: Tile? = null
        var bottom: Tile? = null

        val adjacent get() = listOf(left, top, right, bottom)
        val adjacentCount get() = adjacent.count { it != null }

        val topBorder = data.first()
        val bottomBorder = data.last()
        val leftBorder = data.map { it.first() }.toBooleanArray()
        val rightBorder = data.map { it.last() }.toBooleanArray()

        val borderLess get() = data.sliceArray(1 until data.lastIndex).map { it.sliceArray(1 until it.lastIndex) }
    }

    private fun Tile.allConfigs(): List<Tile> = this.data.allConfigs().map { Tile(this.id, it) }

    private fun Tile.findAdjacent(tiles: List<Tile>) {
        for (tile in tiles) {
            if (this.id == tile.id) continue
            if (this.leftBorder contentEquals tile.rightBorder) this.left = tile
            if (this.topBorder contentEquals tile.bottomBorder) this.top = tile
            if (this.rightBorder contentEquals tile.leftBorder) this.right = tile
            if (this.bottomBorder contentEquals tile.topBorder) this.bottom = tile
        }
    }

    private fun Array<BooleanArray>.countMonsters(): Int {
        var monsters = 0
        for (i in indices) {
            for (j in get(i).indices) {
                if (hasMonsterOnIndex(i, j)) {
                    monsters++
                }
            }
        }
        return monsters
    }

    private fun Array<BooleanArray>.hasMonsterOnIndex(i: Int, j: Int): Boolean {
        if (i + monster.lastIndex > this.lastIndex || j + monster.first().length - 1 > this.first().lastIndex) {
            return false
        }
        for (i2 in monster.indices) {
            for (j2 in monster[i2].indices) {
                if (monster[i2][j2] == '#' && !this[i + i2][j + j2]) {
                    return false
                }
            }
        }

        return true
    }
}

private fun Array<BooleanArray>.flipped(): Array<BooleanArray> {
    return this.reversedArray()
}

private fun Array<BooleanArray>.rotated(): Array<BooleanArray> {
    return Array(this.first().size) { a ->
        BooleanArray(this.size) { b ->
            this[this.lastIndex-b][a]
        }
    }
}

private fun Array<BooleanArray>.rotated(times: Int): Array<BooleanArray> {
    var x = this
    repeat(times) { x = x.rotated() }
    return x
}

private fun Array<BooleanArray>.allConfigs(): List<Array<BooleanArray>> {
    return listOf(
        this, this.rotated(1), this.rotated(2), this.rotated(3),
        this.flipped(), this.flipped().rotated(1), this.flipped().rotated(2), this.flipped().rotated(3),
    )
}
