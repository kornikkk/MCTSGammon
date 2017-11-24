package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.OLD_BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.OLD_BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.utils.ByteMath

/**
 * Created by kkarolcz on 19.11.2017.
 */

@Deprecated("REMOVE")
fun possibleMoves(board: OLD_BackgammonBoard, player: BackgammonPlayer, dice: Dice): List<OLD_SingleBackgammonMove> {
    val playerCheckers = board.getPlayerCheckers(player)

    if (!playerCheckers.anyLeft()) {
        return emptyList()
    }

    if (!playerCheckers.barEmpty()) {
        val moveFromBar = moveFromBar(board, player, dice)
        if (moveFromBar != null) {
            return listOf(moveFromBar)
        }
        return emptyList()
    }

    if (board.getPlayerCheckers(player).allInHomeBoard()) {
        val normalHomeBoardMoves = normalMoves(board, player, dice, OLD_BackgammonBoardIndex.HOME_BOARD_START_INDEX)
        val bearOffMove = bearOffMove(board, player, dice)
        if (bearOffMove != null) {
            return normalHomeBoardMoves + bearOffMove
        }
    }

    return normalMoves(board, player, dice)
}

@Deprecated("REMOVE")
private fun moveFromBar(board: OLD_BackgammonBoard, player: BackgammonPlayer, dice: Dice): OLD_SingleBackgammonMove? {
    val newIndex = OLD_BackgammonBoardIndex.shift(OLD_BackgammonBoardIndex.BAR_INDEX, dice)
    if (newIndex != OLD_BackgammonBoardIndex.NO_INDEX) {
        val opponentCheckers = board.getPlayerCheckers(player.opponent())
        if (opponentCheckers[OLD_BackgammonBoardIndex.toOpponentsIndex(newIndex)] <= 1)
            return OLD_SingleBackgammonMove(OLD_BackgammonBoardIndex.bar(), OLD_BackgammonBoardIndex.of(newIndex))
    }
    return null
}

@Deprecated("REMOVE")
private fun bearOffMove(board: OLD_BackgammonBoard, player: BackgammonPlayer, dice: Dice): OLD_SingleBackgammonMove? {
    val oldIndex = board.getPlayerCheckers(player).firstForBearingOff(dice)
    if (oldIndex != null) {
        val newIndex = oldIndex.shiftForBearOff(dice)
        if (newIndex != null) {
            return OLD_SingleBackgammonMove(oldIndex, newIndex)
        }
    }
    return null
}

@Deprecated("REMOVE")
private fun normalMoves(board: OLD_BackgammonBoard, player: BackgammonPlayer, dice: Dice,
                        startIndex: Int = OLD_BackgammonBoardIndex.MAX_INDEX): List<OLD_SingleBackgammonMove> {

    val playerCheckers = board.getPlayerCheckers(player)
    val opponentCheckers = board.getPlayerCheckers(player.opponent())

    val moves = mutableListOf<OLD_SingleBackgammonMove>()
    for (oldIndex in OLD_BackgammonBoardIndex.MIN_INDEX..startIndex) {
        // Test for any checkers on point
        val checkersOnPoint = playerCheckers[oldIndex]
        if (checkersOnPoint == ByteMath.ZERO_BYTE)
            continue

        // Check if move is even possible
        val newIndex = OLD_BackgammonBoardIndex.shift(oldIndex, dice)
        if (newIndex == OLD_BackgammonBoardIndex.NO_INDEX)
            continue

        // Check if it's possible to do the move on empty point on single opponent's checker
        val opponentsCheckers = opponentCheckers[OLD_BackgammonBoardIndex.toOpponentsIndex(newIndex)]
        if (opponentsCheckers <= 1) {
            moves.add(OLD_SingleBackgammonMove(OLD_BackgammonBoardIndex.of(oldIndex), OLD_BackgammonBoardIndex.of(newIndex)))
        }
    }
    return moves
}

