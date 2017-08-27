package pl.kkarolcz.mcts.mctsbackgammon

/**
 * Created by kkarolcz on 24.08.2017.
 */
class SingleBackgammonMove internal constructor(val oldCheckerIndex: BackgammonBoardIndex,
                                                val newCheckerIndex: BackgammonBoardIndex) {

    companion object {
        fun possibleMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): Iterable<SingleBackgammonMove> {
            val playerCheckers = board.getPlayerCheckers(player)

            return when (playerCheckers.anyLeft()) {
                true -> {
                    return when (playerCheckers.barEmpty()) {
                        true -> when (board.getPlayerCheckers(player).allInHomeBoard()) {
                            true -> normalMoves(board, player, dice, BackgammonBoardIndex.HOME_BOARD_START_INDEX) +
                                    bearOffMoves(board, player, dice)
                            false -> normalMoves(board, player, dice)
                        }
                        false -> moveFromBar(board, player, dice)?.let { arrayListOf(it) } ?: emptyList()
                    }
                }
                false -> emptyList()
            }
        }

        private fun moveFromBar(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): SingleBackgammonMove? =
                BackgammonBoardIndex.bar().shift(dice)?.let { endIndex ->
                    when (board.getPlayerCheckers(player.opponent())[endIndex.toOpponentsIndex()]) {
                        in 0..1 -> SingleBackgammonMove(BackgammonBoardIndex.bar(), endIndex)
                        else -> null
                    }
                }


        private fun bearOffMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): SingleBackgammonMove =
                board.getPlayerCheckers(player).firstOnHomeBoard()
                        ?.let { oldIndex ->
                            SingleBackgammonMove(oldIndex, oldIndex.shiftForBearOff(dice) ?:
                                    throw IllegalStateException("For all checkers in home board at least one move should be possible"))
                        }
                        ?: throw IllegalStateException("For all checkers in home board any checker should be found")

        private fun normalMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice,
                                startIndex: Int = BackgammonBoardIndex.MAX_INDEX): Iterable<SingleBackgammonMove> {

            val playerCheckers = board.getPlayerCheckers(player)
            val opponentCheckers = board.getPlayerCheckers(player.opponent())

            return (startIndex downTo BackgammonBoardIndex.MIN_INDEX)
                    .map { BackgammonBoardIndex.of(it) }
                    .filter { playerCheckers[it] > 0 } // Skip points without player's checkers
                    .mapNotNull { it.shift(dice)?.let { newIndex -> Pair(it, newIndex) } } // Map with an index after move
                    .map { (oldIndex, newIndex) -> Triple(oldIndex, newIndex, opponentCheckers[newIndex.toOpponentsIndex()]) }
                    .mapNotNull { (oldIndex, newIndex, opponentCheckersOnPoint) ->
                        when (opponentCheckersOnPoint) {
                            in 0..1 -> SingleBackgammonMove(oldIndex, newIndex) // Empty point or hit on opponent
                            else -> null
                        }
                    }
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleBackgammonMove

        if (oldCheckerIndex != other.oldCheckerIndex) return false
        if (newCheckerIndex != other.newCheckerIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = oldCheckerIndex.hashCode()
        result = 31 * result + newCheckerIndex.hashCode()
        return result
    }


}