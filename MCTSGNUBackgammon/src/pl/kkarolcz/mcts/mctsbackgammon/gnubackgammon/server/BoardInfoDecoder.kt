package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo.Player
import pl.kkarolcz.utils.ByteMath

/**
 * Created by kkarolcz on 29.08.2017.
 */
class BoardInfoDecoder private constructor() {
    companion object {
        fun decode(encodedBoard: String): BoardInfo {
            val decodedBoardInfo = BoardInfo()
            val encoded = encodedBoard.split(":")
            encoded[0] // That should be just board
            decodedBoardInfo.player1Name = encoded[1]
            decodedBoardInfo.player2Name = encoded[2]
            decodedBoardInfo.matchLength = encoded[3].toInt()
            decodedBoardInfo.playerScore = encoded[4].toInt()
            decodedBoardInfo.opponentScore = encoded[5].toInt()
            decodedBoardInfo.bar1 = encoded[6].toByte()

            decodeCheckers(encoded, decodedBoardInfo)

            decodedBoardInfo.bar2 = encoded[31].toByte()
            decodedBoardInfo.playerTurn = decodePlayerTurn(encoded[32].toInt())
            decodedBoardInfo.playerDice1 = encoded[33].toInt()
            decodedBoardInfo.playerDice2 = encoded[34].toInt()
            decodedBoardInfo.opponentDice1 = encoded[35].toInt()
            decodedBoardInfo.opponentDice2 = encoded[36].toInt()
            decodedBoardInfo.doublingCube = encoded[37].toInt()
            decodedBoardInfo.playerMayDouble = encoded[38].toInt() == 1
            decodedBoardInfo.opponentMayDouble = encoded[39].toInt() == 1
            decodedBoardInfo.wasDoubled = encoded[40].toInt() == 1
            decodedBoardInfo.colour = decodeColour(encoded[41].toInt())
            decodedBoardInfo.direction = encoded[42].toInt()
            decodedBoardInfo.home = encoded[43].toInt()
            decodedBoardInfo.bar = encoded[44].toInt()
            decodedBoardInfo.playerOnHome = encoded[45].toByte()
            decodedBoardInfo.opponentOnHome = encoded[46].toByte()
            decodedBoardInfo.playerOnBar = encoded[47].toByte()
            decodedBoardInfo.opponentOnBar = encoded[48].toByte()
            decodedBoardInfo.canMove = encoded[49].toInt()
            decodedBoardInfo.forcedMove = encoded[50].toInt()
            decodedBoardInfo.didCrawford = encoded[51].toInt()
            decodedBoardInfo.redoubles = encoded[52].toInt()

            return decodedBoardInfo
        }

        private fun decodeCheckers(encoded: List<String>, boardInfo: BoardInfo) {
            (7..30).forEachIndexed { indexOnBoard, indexInEncoded ->
                val encodedCheckersOnPoint = encoded[indexInEncoded].toByte()
                when (getPlayerPieces(encodedCheckersOnPoint)) {
                    Player.O -> {
                        boardInfo.piecesO[indexOnBoard] = ByteMath.abs(encodedCheckersOnPoint)
                    }
                    Player.X -> {
                        boardInfo.piecesX[indexOnBoard] = ByteMath.abs(encodedCheckersOnPoint)
                    }
                // Skip null Array is already filled with zeroes
                }
            }
        }

        private fun getPlayerPieces(encodedCheckersOnPoint: Byte): Player? = when {
            encodedCheckersOnPoint > 0 -> Player.O
            encodedCheckersOnPoint < 0 -> Player.X
            else -> null
        }

        private fun decodePlayerTurn(turn: Int): Player? = when (turn) {
            -1 -> Player.X
            1 -> Player.O
            else -> null
        }

        private fun decodeColour(colour: Int) = when (colour) {
            -1 -> Player.X
            1 -> Player.O
            else -> throw IllegalArgumentException("Not possible colour")
        }
    }
}