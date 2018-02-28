package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.shift
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.shiftForBearOff
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.shiftFromBar
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Die

/**
 * Created by kkarolcz on 19.11.2017.
 */


fun possibleMoves(board: Board, player: Player, die: Die): List<SingleMove> {
    val playerCheckers = board.getPlayerBoard(player)
    val opponentCheckers = board.getPlayerBoard(player.opponent())

    if (!playerCheckers.anyLeftOnBoard) {
        return emptyList()
    }

    if (playerCheckers.barCheckers > 0) {
        val moveFromBar = moveFromBar(opponentCheckers, die)
        if (moveFromBar != null) {
            return listOf(moveFromBar)
        }
        return emptyList()
    }

    return normalMoves(playerCheckers, opponentCheckers, die, playerCheckers.canBearOff)
}

private fun moveFromBar(opponentBoard: PlayerBoard, die: Die): SingleMove? {
    val newIndex = shiftFromBar(BAR_INDEX, die)
    if (newIndex == NO_INDEX)
        return null

    if (opponentBoard.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex)))
        return SingleMove.create(BAR_INDEX, newIndex)

    return null
}

private fun normalMoves(playerBoard: PlayerBoard, opponentBoard: PlayerBoard, die: Die,
                        searchForBearOff: Boolean = false): List<SingleMove> {

    val moves = mutableListOf<SingleMove>()
    var continueSearchForBearOff = searchForBearOff
    for (tower in playerBoard.towerIterator()) {
        /** BEAR OFF MOVES */
        if (continueSearchForBearOff) {
            val bearOffNewIndex = shiftForBearOff(tower.index, die)
            if (bearOffNewIndex != NO_INDEX) {
                moves.add(SingleMove.create(tower.index, bearOffNewIndex))
                continueSearchForBearOff = false
            }
        }

        /** NORMAL MOVES */
        val newIndex = shift(tower.index, die)

        // Check if normal move is possible
        if (newIndex == NO_INDEX)
            continue

        if (opponentBoard.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex))) {
            moves.add(SingleMove.create(tower.index, newIndex))
        }
    }

    return moves
}