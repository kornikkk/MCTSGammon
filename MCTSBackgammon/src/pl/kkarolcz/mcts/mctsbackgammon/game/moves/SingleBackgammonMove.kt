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
        fun possibleMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): List<SingleBackgammonMove> {
            val playerCheckers = board.getPlayerCheckers(player)

            if (!playerCheckers.anyLeft()) {
                return emptyList()
            }

            if (!playerCheckers.barEmpty()) {
                val movesFromBar = moveFromBar(board, player, dice)
                if (movesFromBar != null) {
                    return arrayListOf(movesFromBar)
                }
                return emptyList()
            }

            val normalMoves = normalMoves(board, player, dice, BackgammonBoardIndex.HOME_BOARD_START_INDEX)
            if (board.getPlayerCheckers(player).allInHomeBoard()) {
                val bearOffMove = bearOffMove(board, player, dice)
                if (bearOffMove != null) {
                    return normalMoves + bearOffMove
                }
            }

            return normalMoves(board, player, dice)
        }

        private fun moveFromBar(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): SingleBackgammonMove? =
                BackgammonBoardIndex.bar().shift(dice)?.let { endIndex ->
                    when (board.getPlayerCheckers(player.opponent())[endIndex.toOpponentsIndex()]) {
                        in 0..1 -> SingleBackgammonMove(BackgammonBoardIndex.bar(), endIndex)
                        else -> null
                    }
                }


        private fun bearOffMove(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): SingleBackgammonMove? {
            val oldIndex = board.getPlayerCheckers(player).firstForBearingOff(dice)
            if (oldIndex != null) {
                val newIndex = oldIndex.shiftForBearOff(dice)
                if (newIndex != null) {
                    return SingleBackgammonMove(oldIndex, newIndex)
                }
            }
            return null
        }

        private fun normalMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice,
                                startIndex: Int = BackgammonBoardIndex.MAX_INDEX): List<SingleBackgammonMove> {

            val playerCheckers = board.getPlayerCheckers(player)
            val opponentCheckers = board.getPlayerCheckers(player.opponent())

            val moves = mutableListOf<SingleBackgammonMove>()
            for (oldIndex in BackgammonBoardIndex.MIN_INDEX..startIndex) {
                val checkersOnPoint = playerCheckers[oldIndex]
                if (checkersOnPoint < 1)
                    continue

                val newIndex = BackgammonBoardIndex.shift(oldIndex, dice)
                if (newIndex == BackgammonBoardIndex.NO_INDEX)
                    continue

                val opponentsCheckers = opponentCheckers[BackgammonBoardIndex.toOpponentsIndex(newIndex)]
                if (opponentsCheckers <= 1) {
                    moves.add(SingleBackgammonMove(BackgammonBoardIndex.of(oldIndex), BackgammonBoardIndex.of(newIndex)))
                }
            }
            return moves
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