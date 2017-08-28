package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMovesSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleBackgammonMove
import pl.kkarolcz.utils.permutations

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonState : Cloneable {
    val board: BackgammonBoard
    val currentPlayer: BackgammonPlayer
    val dices: BackgammonDices

    constructor(board: BackgammonBoard, currentPlayer: BackgammonPlayer, dices: BackgammonDices) {
        this.board = board
        this.currentPlayer = currentPlayer
        this.dices = dices
    }

    private constructor(other: BackgammonState) {
        this.board = other.board.clone()
        this.currentPlayer = other.currentPlayer
        this.dices = other.dices
    }

    public override fun clone(): BackgammonState {
        return BackgammonState(this)
    }

    fun possibleMoves(): Sequence<BackgammonMovesSequence> {
        val finishedMovesSequences = mutableListOf<BackgammonMovesSequence>()
        when (dices.doubling) {
            true -> sequenceOf(dices.values.asSequence())
            false -> permutations(dices.values)
        }.forEach { singleDices ->
            possibleMoves(finishedMovesSequences, emptySequence(), board, singleDices.toMutableList())

        }
        return finishedMovesSequences.asSequence()
    }

    private fun possibleMoves(finishedMovesSequences: MutableList<BackgammonMovesSequence>,
                              movesSequence: Sequence<SingleBackgammonMove>,
                              board: BackgammonBoard,
                              nextDices: MutableList<Dice>) {

        when (nextDices.isEmpty()) {
            true -> finishedMovesSequences.add(BackgammonMovesSequence(movesSequence))
            else -> {
                val dice = nextDices.removeAt(0)
                val nextMoves = SingleBackgammonMove.possibleMoves(board, currentPlayer, dice)
                nextMoves.forEach { move ->
                    board.doMove(currentPlayer, move)
                    val nextDicesCopy = ArrayList(nextDices)
                    possibleMoves(finishedMovesSequences, movesSequence + move, board, nextDicesCopy)
                    board.undoMove(currentPlayer, move)
                }
            }
        }
    }

}