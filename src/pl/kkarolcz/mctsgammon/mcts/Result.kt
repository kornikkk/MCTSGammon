package pl.kkarolcz.mctsgammon.mcts

/**
 * Created by kkarolcz on 09.08.2017.
 */


class Result(private val playerResults: Map<Int, PlayerResult>) {

    operator fun get(playerId: Int) =
            playerResults[playerId] ?: throw IllegalStateException("Cannot get result of not existing player")

    enum class PlayerResult {
        WIN, LOSE, DRAW // TODO: Really draw?
    }
}