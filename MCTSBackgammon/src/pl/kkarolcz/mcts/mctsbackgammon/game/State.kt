package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSState
import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Die
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.possibleMoves
import pl.kkarolcz.utils.permutations

/**
 * Created by kkarolcz on 24.08.2017.
 */
class State : MCTSState<FullMove> {

    override val result: Result?
        get() {
            val player1WinningResult = winningResult(Player.PLAYER_ONE)
            if (player1WinningResult != null) {
                return player1WinningResult
            }
            val player2WinningResult = winningResult(Player.PLAYER_TWO)
            if (player2WinningResult != null) {
                return player2WinningResult
            }
            return null
        }

    override var previousPlayerId: Int

    private val currentPlayer: Player
        get() = Player.fromInt(previousPlayerId).opponent()
    private val board: Board
    private var dice: Dice?

    constructor(board: Board, previousPlayerId: Player, dice: Dice?) {
        this.board = board
        this.previousPlayerId = previousPlayerId.toInt()
        this.dice = dice
    }

    private constructor(other: State) {
        this.board = other.board.clone()
        this.previousPlayerId = other.previousPlayerId
        this.dice = other.dice
    }

    override fun clone(): State {
        return State(this)
    }

    override fun beforeSwitchPlayer() {
        dice = null//Dice.throwDices()
    }

    override fun findPossibleMoves(): MutableList<FullMove> {
        val finishedMovesSequences = mutableListOf<FullMove>()
        if (result != null) {
            return finishedMovesSequences // Skip finding moves if any player has already won
        }

        val currentDices = dice
        if (currentDices != null)
            findPossibleMovesForDices(finishedMovesSequences, currentDices)
        else {
            Dice.POSSIBLE_COMBINATIONS.forEach { combination ->
                findPossibleMovesForDices(finishedMovesSequences, combination)
            }
        }

        return finishedMovesSequences
    }

    private fun findPossibleMovesForDices(finishedFullMoves: MutableList<FullMove>, dice: Dice) {
        when (dice.doubling) {
            true -> sequenceOf(dice.valuesOLD.asSequence())
            false -> permutations(dice.valuesOLD)
        }.forEach { singleDices ->
            possibleMoveSequences(finishedFullMoves, emptyList(), board, singleDices.toMutableList())

        }
    }

    override fun doMoveImpl(move: FullMove) {
        for (currentMove in move.moves)
            board.doMove(currentPlayer, currentMove)
    }

    private fun possibleMoveSequences(finishedFullMoves: MutableList<FullMove>,
                                      movesSequence: List<SingleMove>,
                                      board: Board,
                                      nextDice: MutableList<Die>) {

        if (nextDice.isEmpty()) {
            finishedFullMoves.add(FullMove.create(movesSequence))
        } else {
            val dice = nextDice.removeAt(0)
            val nextMoves = possibleMoves(board, currentPlayer, dice)
            for (move in nextMoves) {
                board.doMove(currentPlayer, move)
                val nextDicesCopy = ArrayList(nextDice)
                possibleMoveSequences(finishedFullMoves, movesSequence + move, board, nextDicesCopy)
                board.undoLastMove()
            }
            if (movesSequence.isNotEmpty() && nextMoves.isEmpty()) {
                finishedFullMoves.add(FullMove.create(movesSequence))
            }
        }
    }


    private fun winningResult(player: Player): Result? =
            when (board.getPlayerCheckers(player).anyLeftOnBoard) {
                false -> Result(mapOf(
                        Pair(player.toInt(), Result.PlayerResult.WIN),
                        Pair(player.opponent().toInt(), Result.PlayerResult.LOSE)
                ))
                true -> null
            }

    override fun toString(): String {
        val currentPlayer = "Current player: $currentPlayer"
        val result = this.result ?: return currentPlayer
        return currentPlayer + ", Winner: ${Player.fromInt(result.winner())}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (previousPlayerId != other.previousPlayerId) return false
        if (board != other.board) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 0 + previousPlayerId
        result = 31 * result + board.hashCode()
        return result
    }

}