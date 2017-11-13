package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * Created by kkarolcz on 26.08.2017.
 */

/**
 * @throws IllegalArgumentException if index if out of 1..24 range
 */
class BackgammonBoardIndex private constructor(private val index: Int) {

    companion object {
        internal val NO_INDEX = Int.MIN_VALUE
        internal val BEAR_OFF_INDEX = -1
        internal val BAR_INDEX = 0
        internal val MIN_INDEX = 1
        internal val MAX_INDEX = 24
        internal val HOME_BOARD_START_INDEX = 6

        /**
         * Indices in range 6..1
         */
        internal val HOME_BOARD_INDICES = IntRange(MIN_INDEX, HOME_BOARD_START_INDEX)
                .reversed()
                .map(::BackgammonBoardIndex)

        fun of(index: Int): BackgammonBoardIndex {
            if (index == BAR_INDEX || index == BEAR_OFF_INDEX || index in MIN_INDEX..MAX_INDEX)
                return BackgammonBoardIndex(index)
            throw IllegalArgumentException("Index")
        }

        fun bar() = BackgammonBoardIndex(BAR_INDEX)

        fun bearingOff() = BackgammonBoardIndex(BEAR_OFF_INDEX)

        /**
         * Unsafe method with better performance. Normally use the non-static, public method
         */
        internal fun shift(index: Int, dice: Dice): Int {
            if (index == BAR_INDEX)
                return MAX_INDEX + 1 - dice.toInt()

            val shiftedIndex = index - dice.toInt()
            if (shiftedIndex in MIN_INDEX..MAX_INDEX)
                return shiftedIndex

            return NO_INDEX
        }

        /**
         * Unsafe method with better performance. Normally use the non-static, public method
         */
        internal fun shiftForBearOff(index: Int, dice: Dice): Int {
            val newIndex = index - dice.toInt()
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

        private fun internalOf(index: Int): BackgammonBoardIndex? = when (index) {
            NO_INDEX -> null
            else -> BackgammonBoardIndex(index)
        }
    }


    fun toInt(): Int = index

    fun isBar(): Boolean = index == BAR_INDEX

    fun isBearOff(): Boolean = index == BEAR_OFF_INDEX

    fun toOpponentsIndex(): BackgammonBoardIndex = BackgammonBoardIndex(toOpponentsIndex(index))

    fun shift(dice: Dice): BackgammonBoardIndex? = internalOf(shift(index, dice))

    fun shiftForBearOff(dice: Dice): BackgammonBoardIndex? = internalOf(shiftForBearOff(index, dice))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonBoardIndex

        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        return index
    }

}