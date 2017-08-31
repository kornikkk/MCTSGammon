package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo

/**
 * Created by kkarolcz on 29.08.2017.
 */
fun BoardInfo.convertToBackgammonState(): BackgammonState {
    val player1Checkers = when (colour) {
        BoardInfo.Colour.BLACK -> convertToPlayerCheckers(whiteCheckers, player1Bar, player1OnHome, direction == -1)
        BoardInfo.Colour.WHITE -> convertToPlayerCheckers(blackCheckers, player1Bar, player1OnHome, direction == -1)
    }
    // Opposite colour for opponent
    val player2Checkers = when (colour) {
        BoardInfo.Colour.BLACK -> convertToPlayerCheckers(blackCheckers, player2Bar, player2OnHome, direction == 1)
        BoardInfo.Colour.WHITE -> convertToPlayerCheckers(whiteCheckers, player2Bar, player2OnHome, direction == 1)
    }
    val board = BackgammonBoard(player1Checkers, player2Checkers)
    val currentPlayer = getCurrentPlayer()
    return BackgammonState(board, currentPlayer.opponent(), getBackgammonDices(currentPlayer))
}

private fun convertToPlayerCheckers(checkersArray: Array<Int>, onBarCheckers: Int, onHomeCheckers: Int, reversed: Boolean):
        BackgammonPlayerCheckers {

    val checkersWithBarArray = IntArray(BackgammonBoard.SIZE)

    checkersWithBarArray[0] = Math.abs(onBarCheckers)
    for (i in 1 until BackgammonBoard.SIZE) {
        when (reversed) {
            false -> checkersWithBarArray[i] = checkersArray[i - 1]
            true -> checkersWithBarArray[BackgammonBoard.SIZE - i] = checkersArray[i - 1]
        }
    }


    return BackgammonPlayerCheckers(checkersWithBarArray, onHomeCheckers)
}


private fun BoardInfo.getCurrentPlayer(): BackgammonPlayer {
    val colourTurn = getColourTurn()
    if (colourTurn == colour)
        return BackgammonPlayer.PLAYER_ONE
    if (colourTurn != colour)
        return BackgammonPlayer.PLAYER_TWO
    return getWinnerPlayer().opponent()
}

private fun BoardInfo.getWinnerPlayer(): BackgammonPlayer {
    return when {
        player1Score > player2Score -> BackgammonPlayer.PLAYER_ONE
        player1Score < player2Score -> BackgammonPlayer.PLAYER_TWO
        else -> throw IllegalStateException("Cannot get winner when there's draw")
    }
}

private fun BoardInfo.getColourTurn(): BoardInfo.Colour? = when (playerTurn) {
    -1 -> BoardInfo.Colour.WHITE
    1 -> BoardInfo.Colour.BLACK
    else -> null
}

private fun BoardInfo.getBackgammonDices(currentPlayer: BackgammonPlayer): BackgammonDices? =
        when (currentPlayer) {
            BackgammonPlayer.PLAYER_ONE -> {
                when {
                    player1Dice1 > 0 && player1Dice2 > 0 -> BackgammonDices(Dice(player1Dice1), Dice(player1Dice2))
                    else -> null
                }
            }
            BackgammonPlayer.PLAYER_TWO -> {
                when {
                    player2Dice1 > 0 && player2Dice2 > 0 -> BackgammonDices(Dice(player2Dice1), Dice(player2Dice2))
                    else -> null
                }
            }
        }
