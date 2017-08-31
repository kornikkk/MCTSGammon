package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

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
                            true -> {
                                val normalMoves = normalMoves(board, player, dice, BackgammonBoardIndex.HOME_BOARD_START_INDEX)
                                val bearOffMoves = bearOffMoves(board, player, dice)
                                return when (bearOffMoves) {
                                    null -> normalMoves
                                    else -> normalMoves + bearOffMoves
                                }
                            }
                            false
                            -> normalMoves(board, player, dice)
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


        private fun bearOffMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): SingleBackgammonMove? =
                board.getPlayerCheckers(player).firstForBearingOff(dice)
                        ?.let { oldIndex ->
                            oldIndex.shiftForBearOff(dice)?.let { SingleBackgammonMove(oldIndex, it) }
                        }

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