package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.FullMovesSearch
import pl.kkarolcz.utils.randomElement
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 * Created by kkarolcz on 24.02.2018.
 */
class BackgammonAllMovesProvider(private val board: Board) : BackgammonMovesProvider {
    private var dice: Dice? = null
    private val moves = MovesForDice()

    companion object {
        private val nonDoublingSearchExecutor = Executors.newSingleThreadExecutor()
    }

    override fun hasUntriedMoves(): Boolean = !moves.isEmpty()

    override fun pollNextRandomUntriedMove(): FullMove {
        if (moves.isEmpty()) throw IllegalStateException("No moves available. Check hasUntriedMovesBefore call")

        return moves.pollRandomMove()
    }

    override fun findMovesForPlayer(player: Player) {
        when (dice) {
            null -> {
                val future = nonDoublingSearchExecutor.submit {
                    Dice.NON_DOUBLING_PERMUTATIONS.forEach { dice -> findMoves(dice, player) }
                }

                Dice.DOUBLING_PERMUTATIONS.parallelStream().forEach { dice -> findMoves(dice, player) }

                future.get()
            }
            else -> findMoves(dice!!, player)
        }
    }

    private fun findMoves(dice: Dice, player: Player) {
        moves.put(dice, FullMovesSearch.findAll(board, player, dice))
    }

    /**
     * Clears the moves, sets a new dice
     * @param newDice if null all possible dice permutations will be used for moves search
     */
    override fun resetDice(newDice: Dice?) {
        dice = newDice
        moves.clear()
    }

    /**
     * Discards all moves for other dice and leaves only the provided one
     * @param dice dice which won't be discarded
     */
    override fun discardOtherDice(dice: Dice) {
        moves.discardOtherDice(dice)
    }

    class MovesForDice {
        private val map: MutableMap<Dice, MutableList<FullMove>> = ConcurrentHashMap()

        fun isEmpty() = map.isEmpty()

        fun discardOtherDice(dice: Dice) {
            map.keys.removeIf { key -> key != dice }
        }

        fun clear() {
            map.clear()
        }

        fun put(dice: Dice, moves: MutableList<FullMove>) {
            map[dice] = moves
        }

        fun pollRandomMove(): FullMove {
            val randomDice = map.keys.randomElement()
            val movesForRandomDice = map[randomDice] ?: throw IllegalArgumentException("Value cannot be null")

            val randomMove = movesForRandomDice.randomElement()
            movesForRandomDice.remove(randomMove)

            if (movesForRandomDice.isEmpty()) {
                map.remove(randomDice)
            }

            return randomMove
        }

    }
}
