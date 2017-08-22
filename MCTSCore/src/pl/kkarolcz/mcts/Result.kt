package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 09.08.2017.
 */


class Result(private val playerResults: Map<Int, PlayerResult>) {

    operator fun get(playerId: Int) =
            playerResults[playerId] ?: throw IllegalStateException("Cannot get result of not existing player")

    fun winner(): Int = playerResults
            .filter { (_, result) -> result == PlayerResult.WIN }
            .map { (playerId, _) -> playerId }
            .first()

    override fun toString() = playerResults
            .map { (playerId, result) -> "Player ID: $playerId, Result: $result" }
            .joinToString("\n")

    enum class PlayerResult {
        WIN, LOSE
    }
}