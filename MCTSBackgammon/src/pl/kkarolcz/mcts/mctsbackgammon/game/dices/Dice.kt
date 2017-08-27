package pl.kkarolcz.mcts.mctsbackgammon.game.dices

class Dice(value: Int) {
    private val value: Int

    init {
        when (value) {
            in 1..6 -> this.value = value
            else -> throw IllegalArgumentException("Possible range is 1..6")
        }
    }

    fun toInt() = value
}