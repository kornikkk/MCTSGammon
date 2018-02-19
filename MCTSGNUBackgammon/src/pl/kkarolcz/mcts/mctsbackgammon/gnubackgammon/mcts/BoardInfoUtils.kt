package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.State
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo

/**
 * Created by kkarolcz on 29.08.2017.
 */
fun BoardInfo.convertToBackgammonState(): State {
    val player1Checkers = when (colour) {
        BoardInfo.Colour.BLACK -> convertToPlayerCheckers(whiteCheckers, player2Bar, player2OnHome, direction == -1)
        BoardInfo.Colour.WHITE -> convertToPlayerCheckers(blackCheckers, player2Bar, player2OnHome, direction == -1)
    }
    // Opposite colour for opponent
    val player2Checkers = when (colour) {
        BoardInfo.Colour.BLACK -> convertToPlayerCheckers(blackCheckers, player1Bar, player1OnHome, direction == 1)
        BoardInfo.Colour.WHITE -> convertToPlayerCheckers(whiteCheckers, player1Bar, player1OnHome, direction == 1)
    }
    val board = Board(player1Checkers, player2Checkers)
    val currentPlayer = getCurrentPlayer()
    return State(board, currentPlayer.opponent(), getBackgammonDices(currentPlayer))
}

private fun convertToPlayerCheckers(checkersArray: ByteArray, onBarCheckers: Byte, onHomeCheckers: Byte, reversed: Boolean):
        PlayerBoard {
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


private fun BoardInfo.getCurrentPlayer(): Player {
    val colourTurn = getColourTurn()
    if (colourTurn == colour)
        return Player.PLAYER_ONE
    if (colourTurn != colour)
        return Player.PLAYER_TWO
    return getWinnerPlayer().opponent()
}

private fun BoardInfo.getWinnerPlayer(): Player {
    return when {
        player1Score > player2Score -> Player.PLAYER_ONE
        player1Score < player2Score -> Player.PLAYER_TWO
        else -> throw IllegalStateException("Cannot get winner when there's draw")
    }
}

private fun BoardInfo.getColourTurn(): BoardInfo.Colour? = when (playerTurn) {
    -1 -> BoardInfo.Colour.WHITE
    1 -> BoardInfo.Colour.BLACK
    else -> null
}

private fun BoardInfo.getBackgammonDices(currentPlayer: Player): Dice? =
        when (currentPlayer) {
            Player.PLAYER_ONE -> {
                when {
                    player1Dice1 > 0 && player1Dice2 > 0 -> dicesOf(player1Dice1, player1Dice2)
                    else -> null
                }
            }
            Player.PLAYER_TWO -> {
                when {
                    player2Dice1 > 0 && player2Dice2 > 0 -> dicesOf(player2Dice1, player2Dice2)
                    else -> null
                }
            }
        }

private fun dicesOf(value1: Int, value2: Int) = Dice(value1.toByte(), value2.toByte())
