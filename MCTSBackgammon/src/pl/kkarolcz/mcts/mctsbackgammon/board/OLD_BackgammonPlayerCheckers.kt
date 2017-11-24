package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.OLD_SingleBackgammonMove
import pl.kkarolcz.utils.ByteMath
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
@Deprecated("REMOVE")
class OLD_BackgammonPlayerCheckers : Cloneable {
    private val checkers: ByteArray
    var bearOffCheckers: Byte = 0

    companion object {
        private val SIZE: Int = OLD_BackgammonBoard.SIZE
    }

    private constructor(other: OLD_BackgammonPlayerCheckers) {
        this.checkers = other.checkers.copyOf()
    }

    /**
     * @throws IllegalArgumentException if the board is of the wrong size
     */
    constructor(checkers: ByteArray, bearOffCheckers: Byte) {
        when (checkers.size) {
            SIZE -> {
                this.checkers = checkers
                this.bearOffCheckers = bearOffCheckers
            }
            else -> throw IllegalArgumentException("Wrong size of the board")
        }

    }

    public override fun clone(): OLD_BackgammonPlayerCheckers {
        return OLD_BackgammonPlayerCheckers(this)
    }


    /**
     * Notice that board is indexed like in Backgammon!!! from 1 to 24
     */
    operator fun get(index: OLD_BackgammonBoardIndex) = checkers[index.toInt()]

    /**
     * @Unsafe
     * Unsafe! Only for performance purposes
     */
    operator fun get(index: Int) = checkers[index]

    fun barEmpty(): Boolean = checkers[OLD_BackgammonBoardIndex.BAR_INDEX] == ByteMath.ZERO_BYTE

    fun anyLeft(): Boolean = checkers.any { checkersOnPoint -> checkersOnPoint > 0 }

    fun allInHomeBoard(): Boolean {
        // For performance purposes
        for (i in OLD_BackgammonBoardIndex.HOME_BOARD_START_INDEX + 1..OLD_BackgammonBoardIndex.MAX_INDEX) {
            if (checkers[i] > 0) {
                return false
            }
        }
        return true
    }

    fun firstForBearingOff(dice: Dice): OLD_BackgammonBoardIndex? {
        // For performance purposes
        for (i in OLD_BackgammonBoardIndex.HOME_BOARD_START_INDEX downTo OLD_BackgammonBoardIndex.MIN_INDEX) {
            if (OLD_BackgammonBoardIndex.shiftForBearOff(i, dice) == OLD_BackgammonBoardIndex.NO_INDEX)
                continue

            if (checkers[i] > 0)
                return OLD_BackgammonBoardIndex.of(i)
        }
        return null
    }

    fun doMove(move: OLD_SingleBackgammonMove) {
        val oldIndex = move.oldCheckerIndex.toInt()
        val newIndex = move.newCheckerIndex.toInt()

        if (checkers[oldIndex] == ByteMath.ZERO_BYTE) {
            throw IllegalStateException("Cannot move checkers from empty point")
        }

        checkers[oldIndex]--
        if (newIndex == OLD_BackgammonBoardIndex.BEAR_OFF_INDEX) {
            if (!allInHomeBoard()) {
                throw IllegalStateException("Cannot bear off if not all checkers are in the home board")
            }
            ++bearOffCheckers
        } else
            checkers[newIndex]++
    }

    fun undoMove(move: OLD_SingleBackgammonMove) {
        val oldIndex = move.oldCheckerIndex.toInt()
        val newIndex = move.newCheckerIndex.toInt()

        if (newIndex != -1 && checkers[newIndex] == ByteMath.ZERO_BYTE) { // skip check for bear off move
            throw IllegalStateException("Cannot undo move from empty point")
        }

        checkers[oldIndex]++
        if (newIndex == OLD_BackgammonBoardIndex.BEAR_OFF_INDEX) {
            if (bearOffCheckers == ByteMath.ZERO_BYTE) {
                throw IllegalStateException("Cannot undo bear off if there are no bear off checkers")
            }
            --bearOffCheckers
        } else
            checkers[newIndex]--
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OLD_BackgammonPlayerCheckers

        if (!Arrays.equals(checkers, other.checkers)) return false
        if (bearOffCheckers != other.bearOffCheckers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(checkers)
        result = 31 * result + bearOffCheckers
        return result
    }
}