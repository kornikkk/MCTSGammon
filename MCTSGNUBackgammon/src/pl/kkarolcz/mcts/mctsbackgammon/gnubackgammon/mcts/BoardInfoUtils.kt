package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.server.BoardInfo

/**
 * Created by kkarolcz on 29.08.2017.
 */
fun BoardInfo.convertToBackgammonState(): BackgammonState {
    // players seem to be mixed up
    val opponentsBoard = when (colour) {
        BoardInfo.Player.O -> convertToPlayerCheckers(piecesO, bar2, opponentOnHome, direction == 1)
        BoardInfo.Player.X -> convertToPlayerCheckers(piecesX, bar2, opponentOnHome, direction == 1)
    }
    // Opposite colour for opponent
    val mctsBoard = when (colour.opponent()) {
        BoardInfo.Player.O -> convertToPlayerCheckers(piecesO, bar1, playerOnHome, direction == -1)
        BoardInfo.Player.X -> convertToPlayerCheckers(piecesX, bar1, playerOnHome, direction == -1)
    }
    val board = Board(mctsBoard, opponentsBoard)
    val currentPlayer = getCurrentPlayer()
    return BackgammonState(board, currentPlayer.opponent(), getBackgammonDice(currentPlayer))
}

private fun convertToPlayerCheckers(checkersArray: ByteArray, onBarCheckers: Byte, onHomeCheckers: Byte, reversed: Boolean): PlayerBoard {
    val playerCheckers = PlayerBoard()

    playerCheckers.put(BAR_INDEX, Math.abs(onBarCheckers.toInt()).toByte())
    playerCheckers.put(BEAR_OFF_INDEX, onHomeCheckers)

    for (i in 1 until Board.SIZE) {
        when (reversed) {
            false -> playerCheckers.put(i.toByte(), checkersArray[i - 1])
            true -> playerCheckers.put((Board.SIZE - i).toByte(), checkersArray[i - 1])
        }
    }

    return playerCheckers
}


private fun BoardInfo.getCurrentPlayer(): Player = when (playerTurn) {
    null -> getWinnerPlayer().opponent() // End of the game
    colour -> Player.MCTS
    else -> Player.OPPONENT
}

private fun BoardInfo.getWinnerPlayer(): Player {
    return when {
        playerScore > opponentScore -> Player.MCTS
        playerScore < opponentScore -> Player.OPPONENT
        else -> throw IllegalStateException("Cannot get winner when there's draw")
    }
}

private fun BoardInfo.getBackgammonDice(currentPlayer: Player): Dice? =
        when (currentPlayer) {
            Player.MCTS -> {
                when {
                    playerDice1 > 0 && playerDice2 > 0 -> dicesOf(playerDice1, playerDice2)
                    else -> null
                }
            }
            Player.OPPONENT -> {
                when {
                    opponentDice1 > 0 && opponentDice2 > 0 -> dicesOf(opponentDice1, opponentDice2)
                    else -> null
                }
            }
        }

private fun dicesOf(value1: Int, value2: Int) = Dice(value1.toByte(), value2.toByte())
