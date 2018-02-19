package pl.kkarolcz.mcts.mctsbackgammon.game.dices

class Die(value: Byte) {
    private val value: Byte

    init {
        when (value) {
            in POSSIBLE_VALUES -> this.value = value
            else -> throw IllegalArgumentException("Possible range is 1..6")
        }
    }

    companion object {
        val POSSIBLE_VALUES: ByteArray = byteArrayOf(1, 2, 3, 4, 5, 6)
    }

    fun toByte(): Byte = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Die

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.toInt()
    }
}