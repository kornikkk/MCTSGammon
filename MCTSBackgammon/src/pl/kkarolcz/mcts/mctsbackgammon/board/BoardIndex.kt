package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Die

/**
 * @throws IllegalArgumentException if index if out of 1..24 range
 */
class BoardIndex {

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
        fun shiftFromBar(index: Byte, die: Die): Byte {
            return (BAR_INDEX - die.toByte()).toByte()
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
        fun shift(index: Byte, die: Die): Byte {
            val shiftedIndex = (index - die.toByte()).toByte()
            if (shiftedIndex in MIN_INDEX..MAX_INDEX)
                return shiftedIndex

            return NO_INDEX
        }

        fun shiftForBearOff(index: Byte, die: Die): Byte {
            val newIndex = index - die.toByte()
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
//        private fun internalOf(index: Int): BoardIndex? = when (index) {
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
//    fun toOpponentsIndex(): BoardIndex = OLD_BackgammonBoardIndex(toOpponentsIndex(index))
//
//    fun shift(dice: Die): BoardIndex? = internalOf(shift(index, dice))
//
//    fun shiftForBearOff(dice: Die): BoardIndex? = internalOf(shiftForBearOff(index, dice))
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as BoardIndex
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