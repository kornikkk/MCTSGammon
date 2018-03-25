package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.server

/**
 * Created by kkarolcz on 29.08.2017.
 */
class BoardInfo {
    lateinit var player1Name: String
    lateinit var player2Name: String
    var matchLength: Int = 0
    var playerScore: Int = 0
    var opponentScore: Int = 0
    var bar1: Byte = 0
    val piecesO: ByteArray = ByteArray(24) { 0 }
    val piecesX: ByteArray = ByteArray(24) { 0 }
    var bar2: Byte = 0
    var playerTurn: Player? = null
    var playerDice1: Int = 0
    var playerDice2: Int = 0
    var opponentDice1: Int = 0
    var opponentDice2: Int = 0
    var doublingCube: Int = 0
    var playerMayDouble: Boolean = false
    var opponentMayDouble: Boolean = false
    var wasDoubled: Boolean = false
    lateinit var colour: Player
    var direction: Int = 0
    var home: Int = 0 // obsolete
    var bar: Int = 0 // obsolete
    var playerOnHome: Byte = 0
    var opponentOnHome: Byte = 0
    var playerOnBar: Byte = 0
    var opponentOnBar: Byte = 0
    var canMove: Int = 0
    var forcedMove: Int = 0 // don't use
    var didCrawford: Int = 0
    var redoubles: Int = 0

    enum class Player {
        O, X;

        fun opponent() = when (this) {
            X -> O
            O -> X
        }
    }

}