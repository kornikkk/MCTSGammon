package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import java.util.*

/**
 * Created by kkarolcz on 19.11.2017.
 */
abstract class AbstractFullMovesSearch(board: Board, currentPlayer: Player, protected val dice: Dice) {

    protected val playerCheckers = board.getPlayerBoard(currentPlayer)
    private val opponentCheckers = board.getPlayerBoard(currentPlayer.opponent())
    protected val fullMoves = mutableListOf<FullMove>()

    fun findAll(): MutableList<FullMove> {
        findAllImpl()
        if (fullMoves.isEmpty()) {
            fullMoves.add(FullMovesBuilder(dice).build())
        }
        return fullMoves
    }

    protected abstract fun findAllImpl()

    protected fun findPartialBarMove(diceValue: Byte): SingleMove? {
        val newIndex = BoardIndex.shift(BAR_INDEX, diceValue) // No need to check NO_INDEX

        if (opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex)))
            return SingleMove(BAR_INDEX, newIndex)
        return null
    }

    protected fun findStandardPartialMoveForTower(index: Byte, diceValue: Byte): SingleMove? {
        val newIndex = findMove(index, diceValue)
        if (newIndex != NO_INDEX)
            return SingleMove(index, newIndex)
        return null
    }

    protected fun findMove(index: Byte, dice: Byte): Byte {
        val newIndex = BoardIndex.shift(index, dice)
        if (newIndex != NO_INDEX && opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex))) {
            return newIndex
        }
        return NO_INDEX
    }

    protected fun firstForBearingOff(homeTowersIndices: Collection<Byte>, dieValue: Byte): SingleMove? {
        val indices: MutableSet<Byte> = sortedSetOf(Comparator.reverseOrder())
        indices.addAll(homeTowersIndices)

        var greaterThanDiceFound = false
        for (index in indices) {
            if (index == dieValue)
                return SingleMove(index, BoardIndex.BEAR_OFF_INDEX)

            if (index > dieValue) {
                greaterThanDiceFound = true
                continue
            }
            if (!greaterThanDiceFound && index < dieValue)
                return SingleMove(index, BoardIndex.BEAR_OFF_INDEX)

            break
        }
        return null
    }
}