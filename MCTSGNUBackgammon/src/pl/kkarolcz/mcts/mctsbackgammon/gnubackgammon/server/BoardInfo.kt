package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

/**
 * Created by kkarolcz on 29.08.2017.
 */
class BoardInfo {
    lateinit var player1Name: String
    lateinit var player2Name: String
    var matchLength: Int = 0
    var player1Score: Int = 0
    var player2Score: Int = 0
    var player1Bar: Int = 0
    val whiteCheckers: Array<Int> = Array(24) { 0 }
    val blackCheckers: Array<Int> = Array(24) { 0 }
    var player2Bar: Int = 0
    var playerTurn: Int = 0
    var player1Dice1: Int = 0
    var player1Dice2: Int = 0
    var player2Dice1: Int = 0
    var player2Dice2: Int = 0
    var doublingCube: Int = 0
    var player1MayDouble: Boolean = false
    var player2MayDouble: Boolean = false
    var wasDoubled: Boolean = false
    lateinit var colour: Colour
    var direction: Int = 0
    var home: Int = 0 // obsolete
    var bar: Int = 0 // obsolete
    var player1OnHome: Int = 0
    var player2OnHome: Int = 0
    var player1OnBar: Int = 0
    var player2OnBar: Int = 0
    var canMove: Int = 0
    var forcedMove: Int = 0 // don't use
    var didCrawford: Int = 0
    var redoubles: Int = 0

    enum class Colour {
        WHITE, BLACK;

        companion object {
            fun fromInt(value: Int) = when (value) {
                -1 -> BLACK
                1 -> WHITE
                else -> throw IllegalArgumentException("Wrong colour code")
            }
        }
    }
}