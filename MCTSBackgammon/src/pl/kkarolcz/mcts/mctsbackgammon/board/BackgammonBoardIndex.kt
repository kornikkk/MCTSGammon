package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * @throws IllegalArgumentException if index if out of 1..24 range
 */
class BackgammonBoardIndex {

    companion object {
        const val NO_INDEX: Byte = -1
        const val BEAR_OFF_INDEX: Byte = 0
        const val BAR_INDEX: Byte = 25
        const val HOME_BOARD_START_INDEX: Byte = 6

        const val MIN_INDEX: Byte = 1
        const val MAX_INDEX: Byte = 24

        fun isOnBoard(index: Byte) = index in MIN_INDEX..MAX_INDEX

        fun isOnHomeBoard(index: Byte) = index in MIN_INDEX..HOME_BOARD_START_INDEX

        fun toOpponentsIndex(index: Byte): Byte {
            if (index == BAR_INDEX)
                return BAR_INDEX
            return (MAX_INDEX + 1 - index).toByte()
        }

//        fun shiftFromBar(diceValue: Byte): Byte {
//            return (BAR_INDEX - diceValue).toByte()
//        }

        //TODO: Remove?
        fun shiftFromBar(index: Byte, dice: Dice): Byte {
            return (BAR_INDEX - dice.toByte()).toByte()
        }


        fun shift(index: Byte, dice: Byte): Byte {
            val shiftedIndex = (index - dice).toByte()
            if (shiftedIndex in MIN_INDEX..MAX_INDEX)
                return shiftedIndex

            return NO_INDEX
        }

        fun shiftForBearOff(index: Byte, dice: Byte): Byte {
            val newIndex = index - dice
            if (newIndex < MIN_INDEX)
                return BEAR_OFF_INDEX
            return NO_INDEX
        }

        //TODO: Remove?
        fun shift(index: Byte, dice: Dice): Byte {
            val shiftedIndex = (index - dice.toByte()).toByte()
            if (shiftedIndex in MIN_INDEX..MAX_INDEX)
                return shiftedIndex

            return NO_INDEX
        }

        fun shiftForBearOff(index: Byte, dice: Dice): Byte {
            val newIndex = index - dice.toByte()
            if (newIndex < MIN_INDEX)
                return BEAR_OFF_INDEX
            return NO_INDEX
        }

    }

//
//        /**
//         * Unsafe method with better performance. Normally use the non-static, public method
//         */
//        internal fun toOpponentsIndex(index: Int): Int {
//            if (index == BAR_INDEX)
//                return BAR_INDEX
//            return MAX_INDEX + 1 - index
//        }
//
//        private fun internalOf(index: Int): BackgammonBoardIndex? = when (index) {
//            NO_INDEX -> null
//            else -> OLD_BackgammonBoardIndex(index)
//        }
//    }
//
//
//    fun toInt(): Int = index
//
//    fun isBar(): Boolean = index == BAR_INDEX
//
//    fun isBearOff(): Boolean = index == BEAR_OFF_INDEX
//
//    fun toOpponentsIndex(): BackgammonBoardIndex = OLD_BackgammonBoardIndex(toOpponentsIndex(index))
//
//    fun shift(dice: Dice): BackgammonBoardIndex? = internalOf(shift(index, dice))
//
//    fun shiftForBearOff(dice: Dice): BackgammonBoardIndex? = internalOf(shiftForBearOff(index, dice))
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as BackgammonBoardIndex
//
//        if (index != other.index) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return index
//    }

}