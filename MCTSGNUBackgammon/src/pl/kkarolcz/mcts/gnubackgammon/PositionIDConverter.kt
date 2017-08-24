package pl.kkarolcz.mcts.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.BackgammonPlayer
import java.util.*

/**
 * Created by kkarolcz on 22.07.2017.
 */
object PositionIDConverter {

    fun convert(positionId: String): BackgammonBoard {
        val backgammonBoard = BackgammonBoard()
        val decodedPositionId = Base64.getDecoder().decode(positionId)

        var playerIndex = 0
        var boardIndex = 0
        decodedPositionId.map { b -> b.unsigned() }.forEach { element ->
            var shiftedElement = element
            for (k in 0..7) {
                if (shiftedElement and 0x1 != 0) {
                    val playerCheckersOnBoard = backgammonBoard.getPlayerCheckers(playerFromIndex(playerIndex))
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

        return backgammonBoard
    }

    private fun playerFromIndex(playerIndex: Int) = BackgammonPlayer.fromInt(playerIndex)

    private fun Byte.unsigned(): Int = this + 128 xor 0b10000000

}