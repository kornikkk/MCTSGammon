package pl.kkarolcz.mctsgammon.mcts

/**
 * Created by kkarolcz on 09.08.2017.
 */
class Result(numberOfPlayers: Int) {
    private val results: MutableMap<Int, Double> = mutableMapOf()

    init {
        for (i in 0..numberOfPlayers)
            results.put(i, 0.0)
    }

    operator fun get(playerId: Int): Double {
        val playerResult = results[playerId] ?: throw IllegalArgumentException("Cannot get result of not existing")
        return playerResult
    }
}