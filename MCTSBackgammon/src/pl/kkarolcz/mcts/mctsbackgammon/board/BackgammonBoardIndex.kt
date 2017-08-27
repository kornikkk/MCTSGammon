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

        fun of(index: Int): BackgammonBoardIndex = when (index) {
            in MIN_INDEX..MAX_INDEX -> BackgammonBoardIndex(index)
            else -> throw IllegalArgumentException("Index")
        }

        fun bar() = BackgammonBoardIndex(BAR_INDEX)

        fun bearOff() = BackgammonBoardIndex(BEAR_OFF_INDEX)
    }


    fun toInt(): Int = index

    fun isBar(): Boolean = index == BAR_INDEX

    fun isBearOff(): Boolean = index == BEAR_OFF_INDEX

    fun toOpponentsIndex(): BackgammonBoardIndex = when (index) {
        BAR_INDEX -> bar()
        else -> BackgammonBoardIndex(MAX_INDEX + 1 - index)
    }

    fun shift(dice: Dice): BackgammonBoardIndex? = when (index) {
        BAR_INDEX -> of(MAX_INDEX + 1 - dice.toInt())
        else -> {
            val shiftedIndex = index - dice.toInt()
            when (shiftedIndex) {
                in MIN_INDEX..MAX_INDEX -> of(shiftedIndex)
                else -> null
            }
        }
    }

    fun shiftForBearOff(dice: Dice): BackgammonBoardIndex? = when (index - dice.toInt()) {
        in (HOME_BOARD_START_INDEX + 1) downTo MIN_INDEX -> null
        else -> bearOff()
    }

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