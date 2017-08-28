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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dice

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }
}