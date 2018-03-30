package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove

class SequenceForPartialMove(val partialMove: SingleMove) {

    private val sequence = ArrayList<SingleMove>(3)

    val length get() = sequence.size

    operator fun get(index: Int): SingleMove? = sequence.getOrNull(index)

    fun trimmed(length: Int) = sequence.subList(0, length)

    fun add(move: SingleMove) {
        sequence.add(move)
    }

}