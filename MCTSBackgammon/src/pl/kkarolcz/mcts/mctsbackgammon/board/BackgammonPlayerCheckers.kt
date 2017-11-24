package pl.kkarolcz.mcts.mctsbackgammon.board

import com.carrotsearch.hppc.ByteByteHashMap
import com.carrotsearch.hppc.ByteByteMap
import com.carrotsearch.hppc.ByteHashSet
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.HOME_BOARD_START_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMove
import pl.kkarolcz.utils.ByteMath.ZERO_BYTE

/**
 * Created by kkarolcz on 19.11.2017.
 */

class BackgammonPlayerCheckers : Cloneable {
    private val towers: ByteByteMap
    val nonHomeTowers: ByteHashSet
    private var _barCheckers: Byte
    private var _bearOffCheckers: Byte

    val barCheckers: Byte get() = _barCheckers

    val bearOffCheckers: Byte get() = _bearOffCheckers

    val canBearOff: Boolean get() = nonHomeTowers.size() == 0 && _barCheckers == ZERO_BYTE

    val anyLeftOnBoard: Boolean get() = _barCheckers != ZERO_BYTE || !towers.isEmpty

    constructor() {
        this.towers = ByteByteHashMap()
        this.nonHomeTowers = ByteHashSet()
        this._barCheckers = 0
        this._bearOffCheckers = 0
    }

    private constructor(other: BackgammonPlayerCheckers) {
        this.towers = ByteByteHashMap(other.towers)
        this.nonHomeTowers = ByteHashSet(other.nonHomeTowers)
        this._barCheckers = other._barCheckers
        this._bearOffCheckers = other._bearOffCheckers
    }

    public override fun clone() = BackgammonPlayerCheckers(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonPlayerCheckers

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
                    if (index > HOME_BOARD_START_INDEX)
                        nonHomeTowers.add(index)
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

    fun towerIterator() = object : Iterator<Tower> {
        private val mapIterator = towers.iterator()
        override fun hasNext() = mapIterator.hasNext()

        override fun next(): Tower {
            val cursor = mapIterator.next()
            return Tower(cursor.key, cursor.value)
        }
    }

    fun isOccupied(index: Byte): Boolean = get(index) > 0

    fun isNotOccupiedOrCanBeHit(index: Byte): Boolean = get(index) <= 1

    fun move(move: BackgammonMove) {
        val checkers = get(move.oldIndex)
        if (checkers == 0.toByte()) throw IllegalStateException("No checkers to move")
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
                    if (index > HOME_BOARD_START_INDEX)
                        nonHomeTowers.remove(index)
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
                    if (index > HOME_BOARD_START_INDEX)
                        nonHomeTowers.add(index)
                    towers.put(index, 1)
                }
            }
        }
    }

    class Tower(val index: Byte, val checkers: Byte)

}