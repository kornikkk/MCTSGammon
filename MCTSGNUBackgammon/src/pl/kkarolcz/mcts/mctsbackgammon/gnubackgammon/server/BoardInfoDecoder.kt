package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

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
            decodedBoardInfo.player1Score = encoded[4].toInt()
            decodedBoardInfo.player2Score = encoded[5].toInt()
            decodedBoardInfo.player1Bar = encoded[6].toInt()

            decodeCheckers(encoded, decodedBoardInfo)

            decodedBoardInfo.player2Bar = encoded[31].toInt()
            decodedBoardInfo.playerTurn = encoded[32].toInt()
            decodedBoardInfo.player1Dice1 = encoded[33].toInt()
            decodedBoardInfo.player1Dice2 = encoded[34].toInt()
            decodedBoardInfo.player2Dice1 = encoded[35].toInt()
            decodedBoardInfo.player2Dice2 = encoded[36].toInt()
            decodedBoardInfo.doublingCube = encoded[37].toInt()
            decodedBoardInfo.player1MayDouble = encoded[38].toInt() == 1
            decodedBoardInfo.player2MayDouble = encoded[39].toInt() == 1
            decodedBoardInfo.wasDoubled = encoded[40].toInt() == 1
            decodedBoardInfo.colour = BoardInfo.Colour.fromInt(encoded[41].toInt())
            decodedBoardInfo.direction = encoded[42].toInt()
            decodedBoardInfo.home = encoded[43].toInt()
            decodedBoardInfo.bar = encoded[44].toInt()
            decodedBoardInfo.player1OnHome = encoded[45].toInt()
            decodedBoardInfo.player2OnHome = encoded[46].toInt()
            decodedBoardInfo.player1OnBar = encoded[47].toInt()
            decodedBoardInfo.player2OnBar = encoded[48].toInt()
            decodedBoardInfo.canMove = encoded[49].toInt()
            decodedBoardInfo.forcedMove = encoded[50].toInt()
            decodedBoardInfo.didCrawford = encoded[51].toInt()
            decodedBoardInfo.redoubles = encoded[52].toInt()

            return decodedBoardInfo
        }

        private fun decodeCheckers(encoded: List<String>, boardInfo: BoardInfo) {
            (7..30).forEachIndexed { indexOnBoard, indexInEncoded ->
                val encodedCheckersOnPoint = encoded[indexInEncoded].toInt()
                when (getPlayerPieces(encodedCheckersOnPoint)) {
                    PlayerPieces.O -> {
                        boardInfo.whiteCheckers[indexOnBoard] = Math.abs(encodedCheckersOnPoint)
                    }
                    PlayerPieces.X -> {
                        boardInfo.blackCheckers[indexOnBoard] = Math.abs(encodedCheckersOnPoint)
                    }
                    PlayerPieces.BOTH -> {
                        // Skip. Array is already filled with zeroes
                    }
                }
            }
        }

        private fun getPlayerPieces(encodedCheckersOnPoint: Int): PlayerPieces {
            if (encodedCheckersOnPoint > 0)
                return PlayerPieces.O
            else if (encodedCheckersOnPoint < 0)
                return PlayerPieces.X
            return PlayerPieces.BOTH
        }

        private enum class PlayerPieces {
            O, X, BOTH
        }
    }
}