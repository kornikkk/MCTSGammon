package pl.kkarolcz.mctsgammon

import java.util.*

/**
 * Created by kkarolcz on 22.07.2017.
 */
object PositionIDConverter {

    fun convert(positionId: String): Array<Array<Int>> {
        val board: Array<Array<Int>> = Array(2) { Array(25) { 0 } }
        val decodedPositionId = Base64.getDecoder().decode(positionId)

        var playerIndex: Int = 0
        var boardIndex: Int = 0
        decodedPositionId.map { b -> b.unsigned() }.forEach { element ->
            var shiftedElement = element
            for (k in 0..7) {
                if (shiftedElement and 0x1 != 0) {
                    board[playerIndex][boardIndex] = board[playerIndex][boardIndex] + 1
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

        return board
    }

    private fun Byte.unsigned(): Int = this + 128 xor 0b10000000

}