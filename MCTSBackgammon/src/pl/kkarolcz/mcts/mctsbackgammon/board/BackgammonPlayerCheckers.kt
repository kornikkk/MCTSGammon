package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleBackgammonMove
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonPlayerCheckers : Cloneable {
    private val checkers: IntArray
    var bearOffCheckers: Int = 0

    companion object {
        private val SIZE: Int = BackgammonBoard.SIZE
    }

    private constructor(other: BackgammonPlayerCheckers) {
        this.checkers = other.checkers.copyOf()
    }

    /**
     * @throws IllegalArgumentException if the board is of the wrong size
     */
    constructor(checkers: IntArray, bearOffCheckers: Int) {
        when (checkers.size) {
            SIZE -> {
                this.checkers = checkers
                this.bearOffCheckers = bearOffCheckers
            }
            else -> throw IllegalArgumentException("Wrong size of the board")
        }

    }

    public override fun clone(): BackgammonPlayerCheckers {
        return BackgammonPlayerCheckers(this)
    }


    /**
     * Notice that board is indexed like in Backgammon!!! from 1 to 24
     */
    operator fun get(index: BackgammonBoardIndex) = checkers[index.toInt()]

    /**
     * @Unsafe
     * Unsafe! Only for performance purposes
     */
    operator fun get(index: Int) = checkers[index]

    fun barEmpty(): Boolean = checkers[BackgammonBoardIndex.BAR_INDEX] == 0

    fun anyLeft(): Boolean = checkers.any { checkersOnPoint -> checkersOnPoint > 0 }

    fun allInHomeBoard(): Boolean {
        // For performance purposes
        for (i in BackgammonBoardIndex.HOME_BOARD_START_INDEX..BackgammonBoardIndex.MAX_INDEX) {
            if (checkers[i] > 0) {
                return false
            }
        }
        return true
    }

    fun firstForBearingOff(dice: Dice): BackgammonBoardIndex? {
        // For performance purposes
        for (i in BackgammonBoardIndex.HOME_BOARD_START_INDEX downTo BackgammonBoardIndex.MIN_INDEX) {
            if (BackgammonBoardIndex.shiftForBearOff(i, dice) == BackgammonBoardIndex.NO_INDEX)
                continue

            if (checkers[i] > 0)
                return BackgammonBoardIndex.of(i)
        }
        return null
    }

    fun doMove(move: SingleBackgammonMove) {
        val oldIndex = move.oldCheckerIndex.toInt()
        val newIndex = move.newCheckerIndex.toInt()

        if (checkers[oldIndex] == 0) {
            throw IllegalStateException("Cannot move checkers from empty point")
        }

        checkers[oldIndex] -= 1
        if (newIndex == BackgammonBoardIndex.BEAR_OFF_INDEX) {
            if (!allInHomeBoard()) {
                throw IllegalStateException("Cannot bear off if not all checkers are in the home board")
            }
            ++bearOffCheckers
        } else
            checkers[newIndex] += 1
    }

    fun undoMove(move: SingleBackgammonMove) {
        val oldIndex = move.oldCheckerIndex.toInt()
        val newIndex = move.newCheckerIndex.toInt()

        if (newIndex != -1 && checkers[newIndex] == 0) { // skip check for bear off move
            throw IllegalStateException("Cannot undo move from empty point")
        }

        checkers[oldIndex] += 1
        if (newIndex == BackgammonBoardIndex.BEAR_OFF_INDEX) {
            if (bearOffCheckers == 0) {
                throw IllegalStateException("Cannot undo bear off if there are no bear off checkers")
            }
            --bearOffCheckers
        } else
            checkers[newIndex] -= 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonPlayerCheckers

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