package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSState
import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDice
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMovesSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.possibleMoves
import pl.kkarolcz.utils.permutations

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonState : MCTSState<BackgammonMovesSequence> {

    override val result: Result?
        get() {
            val player1WinningResult = winningResult(BackgammonPlayer.PLAYER_ONE)
            if (player1WinningResult != null) {
                return player1WinningResult
            }
            val player2WinningResult = winningResult(BackgammonPlayer.PLAYER_TWO)
            if (player2WinningResult != null) {
                return player2WinningResult
            }
            return null
        }

    override var previousPlayerId: Int

    private val currentPlayer: BackgammonPlayer
        get() = BackgammonPlayer.fromInt(previousPlayerId).opponent()
    private val board: BackgammonBoard
    private var dice: BackgammonDice?

    constructor(board: BackgammonBoard, previousPlayerId: BackgammonPlayer, dice: BackgammonDice?) {
        this.board = board
        this.previousPlayerId = previousPlayerId.toInt()
        this.dice = dice
    }

    private constructor(other: BackgammonState) {
        this.board = other.board.clone()
        this.previousPlayerId = other.previousPlayerId
        this.dice = other.dice
    }

    override fun clone(): BackgammonState {
        return BackgammonState(this)
    }

    override fun beforeSwitchPlayer() {
        dice = null//BackgammonDice.throwDices()
    }

    override fun findPossibleMoves(): MutableList<BackgammonMovesSequence> {
        val finishedMovesSequences = mutableListOf<BackgammonMovesSequence>()
        if (result != null) {
            return finishedMovesSequences // Skip finding moves if any player has already won
        }

        val currentDices = dice
        if (currentDices != null)
            findPossibleMovesForDices(finishedMovesSequences, currentDices)
        else {
            BackgammonDice.POSSIBLE_COMBINATIONS.forEach { combination ->
                findPossibleMovesForDices(finishedMovesSequences, combination)
            }
        }

        return finishedMovesSequences
    }

    private fun findPossibleMovesForDices(finishedMovesSequences: MutableList<BackgammonMovesSequence>, dice: BackgammonDice) {
        when (dice.doubling) {
            true -> sequenceOf(dice.valuesOLD.asSequence())
            false -> permutations(dice.valuesOLD)
        }.forEach { singleDices ->
            possibleMoveSequences(finishedMovesSequences, emptyList(), board, singleDices.toMutableList())

        }
    }

    override fun doMoveImpl(move: BackgammonMovesSequence) {
        for (currentMove in move.moves)
            board.doMove(currentPlayer, currentMove)
    }

    private fun possibleMoveSequences(finishedMovesSequences: MutableList<BackgammonMovesSequence>,
                                      movesSequence: List<BackgammonMove>,
                                      board: BackgammonBoard,
                                      nextDices: MutableList<Dice>) {

        if (nextDices.isEmpty()) {
            finishedMovesSequences.add(BackgammonMovesSequence.create(movesSequence))
        } else {
            val dice = nextDices.removeAt(0)
            val nextMoves = possibleMoves(board, currentPlayer, dice)
            for (move in nextMoves) {
                board.doMove(currentPlayer, move)
                val nextDicesCopy = ArrayList(nextDices)
                possibleMoveSequences(finishedMovesSequences, movesSequence + move, board, nextDicesCopy)
                board.undoLastMove()
            }
            if (movesSequence.isNotEmpty() && nextMoves.isEmpty()) {
                finishedMovesSequences.add(BackgammonMovesSequence.create(movesSequence))
            }
        }
    }


    private fun winningResult(player: BackgammonPlayer): Result? =
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
        return currentPlayer + ", Winner: ${BackgammonPlayer.fromInt(result.winner())}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonState

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