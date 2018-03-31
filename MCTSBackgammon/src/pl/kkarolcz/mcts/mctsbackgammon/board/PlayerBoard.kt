package pl.kkarolcz.mcts.mctsbackgammon.board

import com.carrotsearch.hppc.ByteByteHashMap
import com.carrotsearch.hppc.ByteByteMap
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.settings.TestSettings
import pl.kkarolcz.utils.ByteMath.ZERO_BYTE
import pl.kkarolcz.utils.clearBit
import pl.kkarolcz.utils.setBit
import pl.kkarolcz.utils.setBitsFromMostSignificant
import java.lang.Integer.bitCount
import java.lang.Integer.numberOfLeadingZeros

/**
 * Created by kkarolcz on 19.11.2017.
 */

class PlayerBoard : Cloneable {
    private val towers: ByteByteMap
    private var towersMask: Int // Bit mask of all towers
    private val nonHomeTowersMask: Int get() = towersMask ushr 6 shl 6 // Remove 6 least significant bits (home board)
    private val homeTowersMask: Int get() = towersMask shl 26 ushr 26 // Preserve 6 least significant bits (home board)
    private var _barCheckers: Byte
    private var _bearOffCheckers: Byte

    val barCheckers: Byte get() = _barCheckers

    val numberOfNonHomeTowers: Int get() = bitCount(nonHomeTowersMask)
    val firstNonHomeTowerIndex: Byte get() = (31 - numberOfLeadingZeros(nonHomeTowersMask)).toByte()

    val canBearOff: Boolean get() = nonHomeTowersMask == 0 && _barCheckers == ZERO_BYTE

    val bearOffCheckers: Byte get() = _bearOffCheckers

    val anyLeftOnBoard: Boolean get() = _barCheckers != ZERO_BYTE || !towers.isEmpty

    constructor() {
        this.towers = ByteByteHashMap(15)
        this.towersMask = 0x000000000000000000000000 //All 24 board points. Some of them will be always 0
        this._barCheckers = 0
        this._bearOffCheckers = 0
    }

    private constructor(other: PlayerBoard) {
        this.towers = ByteByteHashMap(other.towers)
        this.towersMask = other.towersMask
        this._barCheckers = other._barCheckers
        this._bearOffCheckers = other._bearOffCheckers
    }

    public override fun clone() = PlayerBoard(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerBoard

        if (towers != other.towers) return false
        if (_barCheckers != other._barCheckers) return false
        if (_bearOffCheckers != other._bearOffCheckers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = towers.hashCode()
        result = 31 * result + _barCheckers
        result = 31 * result + _bearOffCheckers
        return result
    }

    fun put(index: Byte, checkersOnPoint: Byte) {
        if (towers.containsKey(index))
            throw IllegalStateException("Checkers on point already set")

        when (index) {
            BAR_INDEX -> _barCheckers = checkersOnPoint
            BEAR_OFF_INDEX -> _bearOffCheckers = checkersOnPoint
            else -> {
                if (checkersOnPoint > 0) {
                    towersMask = setBit(towersMask, index - 1)
                    towers.put(index, checkersOnPoint)
                }
            }
        }
    }

    fun get(index: Byte): Byte {
        return when (index) {
            BAR_INDEX -> _barCheckers
            BEAR_OFF_INDEX -> _bearOffCheckers
            else -> towers.getOrDefault(index, 0)
        }
    }

    fun maxIndexTower(): Tower? {
        val found = towers.maxBy { cursor -> cursor.key } ?: return null
        return Tower(found.key, found.value)
    }

    fun towerIterator() = when (TestSettings.sortBoard) {
        true -> object : Iterator<Tower> {
            private val keysIterator = towers.keys().toArray().toList()
                    .sortedWith(Comparator(Byte::compareTo))
                    .reversed()
                    .iterator()

            override fun hasNext() = keysIterator.hasNext()

            override fun next(): Tower {
                val key = keysIterator.next()
                return Tower(key, towers[key])
            }
        }
        else -> object : Iterator<Tower> {
            private val mapIterator = towers.iterator()

            override fun hasNext() = mapIterator.hasNext()

            override fun next(): Tower {
                val cursor = mapIterator.next()
                return Tower(cursor.key, cursor.value)
            }
        }
    }

    fun homeTowersIndices(): Collection<Byte> = towersIndices(homeTowersMask)

    fun nonHomeTowersIndices(): Collection<Byte> = towersIndices(nonHomeTowersMask)

    fun isOccupied(index: Byte): Boolean = get(index) > 0

    fun isNotOccupiedOrCanBeHit(index: Byte): Boolean = get(index) <= 1

    fun move(move: SingleMove) {
        val checkers = get(move.oldIndex)
        if (checkers == 0.toByte())
            throw IllegalStateException("No checkers to move")
        remove(move.oldIndex)
        add(move.newIndex)
    }

    private fun remove(index: Byte) {
        when (index) {
            BAR_INDEX -> _barCheckers--
            BEAR_OFF_INDEX -> _bearOffCheckers--
            else -> {
                val checkers = towers.getOrDefault(index, 0)
                if (checkers > 1) // Decrement if 2 or more. 2 -> 1, 3 -> 2, ...
                    towers.addTo(index, -1)
                else { // Remove if only 1
                    towersMask = clearBit(towersMask, index - 1)
                    towers.remove(index)
                }
            }
        }
    }

    private fun add(index: Byte) {
        when (index) {
            BAR_INDEX -> _barCheckers++
            BEAR_OFF_INDEX -> _bearOffCheckers++
            else -> {
                if (towers.containsKey(index))
                    towers.addTo(index, 1)
                else {
                    towersMask = setBit(towersMask, index - 1)
                    towers.put(index, 1)
                }
            }
        }
    }

    private fun towersIndices(towersMask: Int): Collection<Byte> = setBitsFromMostSignificant(towersMask)
            .map { i -> (i + 1).toByte() }
            .toList()

    class Tower(val index: Byte, val checkers: Byte)

}
