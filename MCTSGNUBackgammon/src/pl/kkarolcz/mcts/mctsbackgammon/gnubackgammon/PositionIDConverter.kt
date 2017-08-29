package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import java.util.*

/**
 * Created by kkarolcz on 22.07.2017.
 */
object PositionIDConverter {

    private val POSSIBLE_CHECKERS_COUNT = 15

    fun convert(positionId: String): BackgammonBoard {
        val player1CheckersArray = IntArray(BackgammonBoard.SIZE)
        var foundPlayer1Checkers = 0

        val player2CheckersArray = IntArray(BackgammonBoard.SIZE)
        val decodedPositionId = Base64.getDecoder().decode(positionId)
        var foundPlayer2Checkers = 0

        var playerIndex = 0
        var boardIndex = 0
        decodedPositionId.map { b -> b.unsigned() }.forEach { element ->
            var shiftedElement = element
            for (k in 0..7) {
                if (shiftedElement and 0x1 != 0) {
                    val playerCheckersOnBoard = when (playerIndex) {
                        0 -> {
                            ++foundPlayer1Checkers
                            player1CheckersArray
                        }
                        1 -> {
                            ++foundPlayer2Checkers
                            player2CheckersArray
                        }
                        else -> throw IllegalStateException("Wrong player index")
                    }

                    playerCheckersOnBoard[boardIndex] = playerCheckersOnBoard[boardIndex] + 1

                } else {
                    boardIndex += 1
                    if (boardIndex == 25) {
                        playerIndex += 1
                        boardIndex = 0
                    }
                }
                shiftedElement = shiftedElement shr 1
            }
        }

        return BackgammonBoard(
                BackgammonPlayerCheckers(player1CheckersArray, POSSIBLE_CHECKERS_COUNT - foundPlayer1Checkers),
                BackgammonPlayerCheckers(player2CheckersArray, POSSIBLE_CHECKERS_COUNT - foundPlayer2Checkers)
        )
    }

    private fun Byte.unsigned(): Int = this + 128 xor 0b10000000

}