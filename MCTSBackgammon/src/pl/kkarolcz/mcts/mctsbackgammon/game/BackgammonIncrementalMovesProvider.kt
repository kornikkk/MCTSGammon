package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.FullMovesSearch
import pl.kkarolcz.utils.randomElement

/**
 * Created by kkarolcz on 24.02.2018.
 */
class BackgammonIncrementalMovesProvider(private val board: Board) : BackgammonMovesProvider {

    private val initialDice = mutableListOf<Dice>()
    private val remainingDice = mutableListOf<Dice>()
    private val untriedDice = mutableListOf<Dice>()

    private val moves = MovesForDice()

    private var player: Player? = null

    override fun hasUntriedMoves(): Boolean = remainingDice.isNotEmpty()

    override fun pollNextRandomUntriedMove(): FullMove {
        val dice = nextRemainingDice()
        if (!wasTried(dice)) {
            updateMovesForDice(dice)
        }

        val randomMove = moves.pollRandomMove(dice)
        if (!moves.hasMoves(dice)) {
            remainingDice.remove(dice)
        }

        return randomMove
    }

    override fun findMovesForPlayer(player: Player) {
        this.player = player
        reset()
        //Moves will be found on poll
    }

    /**
     * Discards all moves for other dice and leaves only the provided one
     * @param dice dice which won't be discarded
     */
    override fun discardOtherDice(dice: Dice) {
        initialDice.removeIf { it != dice }
        remainingDice.removeIf { it != dice }
        untriedDice.removeIf { it != dice }

        if (initialDice.isEmpty()) {
            initialDice.add(dice)
            remainingDice.addAll(initialDice)
            untriedDice.addAll(initialDice)
        }

        moves.removeMovesWithoutDice(dice)
    }

    /**
     * Clears the moves and set a new dice
     * @param newDice if null all possible dice will be used for moves search
     */
    override fun resetDice(newDice: Dice?) {
        initialDice.clear()

        when (newDice) {
            null -> initialDice.addAll(shuffledDiceCombinations())
            else -> initialDice.add(newDice)
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
        if (player == null) throw IllegalStateException("Player not set")
        untriedDice.remove(dice)
        moves.put(dice, FullMovesSearch.findAll(board, player!!, dice))
    }

    private fun nextRemainingDice(): Dice = remainingDice.randomElement()

    private fun wasTried(dice: Dice): Boolean = !untriedDice.contains(dice)

    private fun shuffledDiceCombinations(): List<Dice> {
        val combinations = Dice.PERMUTATIONS.toMutableList()
        combinations.shuffle()
        return combinations
    }

    class MovesForDice {
        private val map = mutableMapOf<Dice, MutableList<FullMove>>()

        fun removeMovesWithoutDice(dice: Dice) {
            map.keys.removeIf { key -> key != dice }
        }

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
