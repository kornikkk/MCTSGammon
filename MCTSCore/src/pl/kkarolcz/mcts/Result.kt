package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 09.08.2017.
 */


class Result(private val playerResult: PlayerResult, private val opponentResult: PlayerResult) {

    operator fun get(player: Player) = when (player) {
        Player.MCTS -> playerResult
        Player.OPPONENT -> opponentResult
    }

    fun winner(): Player? = when {
        playerResult == PlayerResult.WIN -> Player.MCTS
        opponentResult == PlayerResult.WIN -> Player.OPPONENT
        else -> null
    }

    override fun toString() = "Player: $playerResult, Opponent: $opponentResult"

    enum class PlayerResult {
        WIN, LOSE
    }
}