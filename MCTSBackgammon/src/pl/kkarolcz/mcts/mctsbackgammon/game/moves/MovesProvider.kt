package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMovesProvider
import pl.kkarolcz.mcts.MCTSTraceableMove
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.utils.randomElement
import java.util.*

/**
 * Created by kkarolcz on 24.02.2018.
 */
class MovesProvider(private val board: Board, private var player: Player) : MCTSMovesProvider<FullMove, Dice> {

    private val initialDice = mutableListOf<Dice>()

    private val remainingDice = mutableListOf<Dice>()
    private val untriedDice = mutableListOf<Dice>()

    private val moves = MovesForDice()

    override fun hasUntriedMoves(): Boolean = remainingDice.isNotEmpty() || moves.isNotEmpty()

    override fun nextRandomUntriedMove(): MCTSTraceableMove<FullMove, Dice> {
        val dice = nextRemainingDice()
        if (!wasTried(dice)) {
            updateMovesForDice(dice)
        }

        val randomMove = moves.pollRandomMove(dice)
        if (!moves.hasMoves(dice)) {
            remainingDice.remove(dice)
        }

        return MCTSTraceableMove(randomMove, dice)
    }

    override fun reset(player: Player) {
        this.player = player
        reset()
    }

    /** Reset before setting new dice
     * @see MovesProvider.reset
     */
    fun setDice(dice: Dice?) {
        initialDice.clear()

        when (dice) {
            null -> initialDice.addAll(shuffledDiceCombinations())
            else -> initialDice.add(dice)
        }

        reset()
    }

    private fun reset() {
        remainingDice.clear()
        untriedDice.clear()

        remainingDice.addAll(initialDice)
        untriedDice.addAll(initialDice)

        moves.clear()
    }

    private fun updateMovesForDice(dice: Dice) {
        untriedDice.remove(dice)
        moves.put(dice, FullMovesSearch.findAll(board, player, dice))
    }

    private fun nextRemainingDice(): Dice = remainingDice.randomElement()

    private fun wasTried(dice: Dice): Boolean = !untriedDice.contains(dice)

    private fun shuffledDiceCombinations(): List<Dice> {
        val combinations = Dice.PERMUTATIONS.toMutableList()
        Collections.shuffle(combinations)
        return combinations
    }

    class MovesForDice {
        private val map = mutableMapOf<Dice, MutableList<FullMove>>()

        fun isNotEmpty(): Boolean = map.isNotEmpty()

        fun hasMoves(dice: Dice): Boolean = map[dice]?.isNotEmpty() ?: false

        fun clear() {
            map.clear()
        }

        fun put(dice: Dice, moves: MutableList<FullMove>) {
            map[dice] = moves
        }

        fun pollRandomMove(dice: Dice): FullMove {
            val movesForDice = map[dice] ?: throw IllegalArgumentException("Dice not present: $dice")

            val randomMove = movesForDice.randomElement()
            movesForDice.remove(randomMove)

            if (movesForDice.isEmpty()) {
                map.remove(dice)
            }

            return randomMove
        }

    }
}
