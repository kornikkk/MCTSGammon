package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.shift
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.shiftForBearOff
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.shiftFromBar
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * Created by kkarolcz on 19.11.2017.
 */


fun possibleMoves(board: BackgammonBoard, player: BackgammonPlayer, dice: Dice): List<BackgammonMove> {
    val playerCheckers = board.getPlayerCheckers(player)
    val opponentCheckers = board.getPlayerCheckers(player.opponent())

    if (!playerCheckers.anyLeftOnBoard) {
        return emptyList()
    }

    if (playerCheckers.barCheckers > 0) {
        val moveFromBar = moveFromBar(opponentCheckers, dice)
        if (moveFromBar != null) {
            return listOf(moveFromBar)
        }
        return emptyList()
    }

    return normalMoves(playerCheckers, opponentCheckers, dice, playerCheckers.canBearOff)
}

private fun moveFromBar(opponentCheckers: BackgammonPlayerCheckers, dice: Dice): BackgammonMove? {
    val newIndex = shiftFromBar(BAR_INDEX, dice)
    if (newIndex == NO_INDEX)
        return null

    if (opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex)))
        return BackgammonMove(BAR_INDEX, newIndex)

    return null
}

private fun normalMoves(playerCheckers: BackgammonPlayerCheckers, opponentCheckers: BackgammonPlayerCheckers, dice: Dice,
                        searchForBearOff: Boolean = false): List<BackgammonMove> {

    val moves = mutableListOf<BackgammonMove>()
    var continueSearchForBearOff = searchForBearOff
    for (tower in playerCheckers.towerIterator()) {
        /** BEAR OFF MOVES */
        if (continueSearchForBearOff) {
            val bearOffNewIndex = shiftForBearOff(tower.index, dice)
            if (bearOffNewIndex != NO_INDEX) {
                moves.add(BackgammonMove(tower.index, bearOffNewIndex))
                continueSearchForBearOff = false
            }
        }

        /** NORMAL MOVES */
        val newIndex = shift(tower.index, dice)

        // Check if normal move is possible
        if (newIndex == NO_INDEX)
            continue

        if (opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex))) {
            moves.add(BackgammonMove(tower.index, newIndex))
        }
    }

    return moves
}