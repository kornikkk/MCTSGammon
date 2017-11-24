package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * Created by kkarolcz on 26.08.2017.
 */

/**
 * @throws IllegalArgumentException if index if out of 1..24 range
 */
@Deprecated("REMOVE")
class OLD_BackgammonBoardIndex private constructor(private val index: Int) {

    companion object {
        internal val NO_INDEX = Int.MIN_VALUE
        internal val BEAR_OFF_INDEX = -1
        internal val BAR_INDEX = 0
        internal val MIN_INDEX = 1
        internal val MAX_INDEX = 24
        internal val HOME_BOARD_START_INDEX = 6

        fun of(index: Int): OLD_BackgammonBoardIndex {
            if (index == BAR_INDEX || index == BEAR_OFF_INDEX || index in MIN_INDEX..MAX_INDEX)
                return OLD_BackgammonBoardIndex(index)
            throw IllegalArgumentException("Index")
        }

        fun bar() = OLD_BackgammonBoardIndex(BAR_INDEX)

        fun bearingOff() = OLD_BackgammonBoardIndex(BEAR_OFF_INDEX)

        /**
         * Unsafe method with better performance. Normally use the non-static, public method
         */
        internal fun shift(index: Int, dice: Dice): Int {
            if (index == BAR_INDEX)
                return MAX_INDEX + 1 - dice.toByte()

            val shiftedIndex = index - dice.toByte()
            if (shiftedIndex in MIN_INDEX..MAX_INDEX)
                return shiftedIndex

            return NO_INDEX
        }

        /**
         * Unsafe method with better performance. Normally use the non-static, public method
         */
        internal fun shiftForBearOff(index: Int, dice: Dice): Int {
            val newIndex = index - dice.toByte()
            if (newIndex < MIN_INDEX)
                return BEAR_OFF_INDEX
            return NO_INDEX
        }

        /**
         * Unsafe method with better performance. Normally use the non-static, public method
         */
        internal fun toOpponentsIndex(index: Int): Int {
            if (index == BAR_INDEX)
                return BAR_INDEX
            return MAX_INDEX + 1 - index
        }

        private fun internalOf(index: Int): OLD_BackgammonBoardIndex? = when (index) {
            NO_INDEX -> null
            else -> OLD_BackgammonBoardIndex(index)
        }
    }


    fun toInt(): Int = index

    fun isBar(): Boolean = index == BAR_INDEX

    fun isBearOff(): Boolean = index == BEAR_OFF_INDEX

    fun toOpponentsIndex(): OLD_BackgammonBoardIndex = OLD_BackgammonBoardIndex(toOpponentsIndex(index))

    fun shift(dice: Dice): OLD_BackgammonBoardIndex? = internalOf(shift(index, dice))

    fun shiftForBearOff(dice: Dice): OLD_BackgammonBoardIndex? = internalOf(shiftForBearOff(index, dice))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OLD_BackgammonBoardIndex

        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        return index
    }

}