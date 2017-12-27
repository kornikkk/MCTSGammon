package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.isOnHomeBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices
import pl.kkarolcz.utils.ByteMath.ONE_BYTE
import pl.kkarolcz.utils.singletonOrEmptyList
import java.util.*

/**
 * Created by kkarolcz on 21.11.2017.
 */
class FullMovesSearchNonDoubling(board: BackgammonBoard, currentPlayer: BackgammonPlayer, dices: BackgammonDices)
    : AbstractFullMovesSearch(board, currentPlayer, dices) {

    private var moveFromBarFirstDice: BackgammonMove? = null
    private var moveFromBarSecondDice: BackgammonMove? = null

    private var partialPreBearOffMoveFirstDice: BackgammonMove? = null
    private var partialPreBearOffMoveSecondDice: BackgammonMove? = null

    private val partialMovesFirstDice = mutableListOf<BackgammonMove>()
    private val partialMovesSecondDice = mutableListOf<BackgammonMove>()

    private var anyFullSequenceFound = false

    override fun allMoves() {
        // Find bar moves only when there are any checkers which must be moved
        if (playerCheckers.barCheckers > 0) {
            findBarPartialMoves()

            // Find only bar sequences
            if (playerCheckers.barCheckers >= 2) {
                if (moveFromBarFirstDice != null || moveFromBarSecondDice != null)
                    fullMoves.add(BackgammonMovesSequence(moveFromBarFirstDice, moveFromBarSecondDice))
                return // Finish finding moves. No more possible moves
            }
        }

        // Find sequences of a bar checker and the preceding normal move
        if (playerCheckers.barCheckers == ONE_BYTE) {
            findStandardPartialMoves()

            findStandardFullMoves(singletonOrEmptyList(moveFromBarFirstDice), partialMovesSecondDice, dices.second, false)
            findStandardFullMoves(singletonOrEmptyList(moveFromBarSecondDice), partialMovesFirstDice, dices.first, false)

            if (fullMoves.isEmpty()) {
                if (moveFromBarFirstDice != null)
                    fullMoves.add(BackgammonMovesSequence(moveFromBarFirstDice))
                if (moveFromBarSecondDice != null)
                    fullMoves.add(BackgammonMovesSequence(moveFromBarSecondDice))
            }
        }
        // Find normal sequences
        else {
            findStandardPartialMoves()

            findStandardFullMoves(partialMovesFirstDice, partialMovesSecondDice, dices.second, playerCheckers.canBearOff)
            findStandardFullMoves(partialMovesSecondDice, partialMovesFirstDice, dices.first, playerCheckers.canBearOff)

            // Find full bear off moves
            if (playerCheckers.canBearOff) {
                findBearOffFullMoves()
            }
            // Find bear off sequence with the preceding normal move
            else if (findPreBearOffPartialMoves()) {
                findBearOffAfterPreBearOffFullMoves()
            }

            if (fullMoves.isEmpty()) {
                fullMoves.addAll(partialMovesFirstDice.map { move -> BackgammonMovesSequence(move) })
                fullMoves.addAll(partialMovesSecondDice.map { move -> BackgammonMovesSequence(move) })
            }
        }
    }

    private fun findBarPartialMoves() {
        moveFromBarFirstDice = partialBarMove(dices.first)
        moveFromBarSecondDice = partialBarMove(dices.second)
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            findStandardPartialMovesForTower(partialMovesFirstDice, tower.index, dices.first)
            findStandardPartialMovesForTower(partialMovesSecondDice, tower.index, dices.second)
        }
    }

    private fun findStandardPartialMovesForTower(partialMoves: MutableList<BackgammonMove>, index: Byte, diceValue: Byte) {
        val newIndex = findPartialMove(index, diceValue)
        if (newIndex != NO_INDEX)
            partialMoves.add(BackgammonMove(index, newIndex))
    }

    private fun findStandardFullMoves(firstPartialMoves: Collection<BackgammonMove>, secondPartialMoves: Collection<BackgammonMove>,
                                      secondDice: Byte, bearingOff: Boolean) {

        for (move1 in firstPartialMoves) {
            findSequentialFullMove(move1, secondDice, bearingOff)

            for (move2 in secondPartialMoves) {
                if (isMovePossibleAfterMove(move1, move2)) {
                    fullMoves.add(BackgammonMovesSequence(move1, move2))
                    anyFullSequenceFound = true
                }
            }
        }
    }

    private fun isMovePossibleAfterMove(previousMove: BackgammonMove, move: BackgammonMove): Boolean {
        if (previousMove.oldIndex == move.oldIndex) {
            return playerCheckers.get(previousMove.oldIndex) > 1
        }
        return true
    }

    private fun findSequentialFullMove(previousMove: BackgammonMove, dice: Byte, bearingOff: Boolean) {
        if (bearingOff) {// Check bear off move first
            val bearOffMove = findPartialBearOffMove(previousMove, dice)
            if (bearOffMove != null) {
                fullMoves.add(BackgammonMovesSequence(previousMove, bearOffMove))
                anyFullSequenceFound = true
                return
            }
        }

        val newIndex = findPartialMove(previousMove.newIndex, dice)
        if (newIndex != NO_INDEX) {
            fullMoves.add(BackgammonMovesSequence(previousMove, BackgammonMove(previousMove.newIndex, newIndex)))
            anyFullSequenceFound = true
        }
    }


    private fun findBearOffFullMoves() {
        val firstBearOffMove = findPartialBearOffMove(null, dices.first)
        if (firstBearOffMove != null) {
            val secondBearOffMove = findPartialBearOffMove(firstBearOffMove, dices.second)
            if (secondBearOffMove != null) {
                fullMoves.add(BackgammonMovesSequence(firstBearOffMove, secondBearOffMove))
                anyFullSequenceFound = true
            }
            if (!anyFullSequenceFound) {
                fullMoves.add(BackgammonMovesSequence(firstBearOffMove))
            }
        }
    }


    /**
     * If bear off move is not possible for the current board state it may still be possible if there is ONLY ONE non-home board checker.
     */
    private fun findPreBearOffPartialMoves(): Boolean {
        if (playerCheckers.numberOfNonHomeTowers == 1) { //Check if there's ONLY ONE non-home tower
            val nonHomeIndex = playerCheckers.firstNonHomeTowerIndex

            if (playerCheckers.get(nonHomeIndex) == 1.toByte()) { // Check if there's ONLY ONE checker in that tower
                val firstDiceNewIndex = findPartialMove(nonHomeIndex, dices.first)
                val secondDiceNewIndex = findPartialMove(nonHomeIndex, dices.second)

                if (firstDiceNewIndex != NO_INDEX && isOnHomeBoard(firstDiceNewIndex))
                    partialPreBearOffMoveFirstDice = BackgammonMove(nonHomeIndex, firstDiceNewIndex)
                if (secondDiceNewIndex != NO_INDEX && isOnHomeBoard(secondDiceNewIndex))
                    partialPreBearOffMoveSecondDice = BackgammonMove(nonHomeIndex, secondDiceNewIndex)

                return partialPreBearOffMoveFirstDice != null || partialPreBearOffMoveSecondDice != null
            }
        }
        return false
    }

    private fun findBearOffAfterPreBearOffFullMoves() {
        if (partialPreBearOffMoveFirstDice != null) {
            findBearOffAfterPreBearOffFullMove(partialPreBearOffMoveFirstDice!!, dices.second)
        }

        if (partialPreBearOffMoveSecondDice != null) {
            findBearOffAfterPreBearOffFullMove(partialPreBearOffMoveSecondDice!!, dices.first)
        }
    }

    private fun findBearOffAfterPreBearOffFullMove(previousMove: BackgammonMove, dice: Byte) {
        val move = findPartialBearOffMove(previousMove, dice)
        if (move != null) {
            fullMoves.add(BackgammonMovesSequence(previousMove, move))
            anyFullSequenceFound = true
        }
    }


    private fun findPartialBearOffMove(previousMove: BackgammonMove?, dice: Byte): BackgammonMove? {
        val indices: MutableSet<Byte> = sortedSetOf(Comparator.reverseOrder())

        //Add all home towers
        indices.addAll(playerCheckers.homeTowersIndices())

        if (previousMove != null) {
            // Remove the tower if it's empty after move
            if (playerCheckers.get(previousMove.oldIndex) == ONE_BYTE)
                indices.remove(previousMove.oldIndex)

            // Add new tower if previous move was not bearing off
            if (previousMove.newIndex != BEAR_OFF_INDEX)
                indices.add(previousMove.newIndex)
        }

        return firstForBearingOff(indices, dice)
    }

}