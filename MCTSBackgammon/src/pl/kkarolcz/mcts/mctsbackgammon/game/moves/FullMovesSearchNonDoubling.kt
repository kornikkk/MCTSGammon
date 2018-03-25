package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.isOnHomeBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.utils.ByteMath.ONE_BYTE
import pl.kkarolcz.utils.singletonOrEmptyList

/**
 * Created by kkarolcz on 21.11.2017.
 */
class FullMovesSearchNonDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractFullMovesSearch(board, currentPlayer, dice) {

    private var moveFromBarFirstDie: SingleMove? = null
    private var moveFromBarSecondDie: SingleMove? = null

    private var partialPreBearOffMoveFirstDie: SingleMove? = null
    private var partialPreBearOffMoveSecondDie: SingleMove? = null

    private val partialMovesFirstDie = mutableListOf<SingleMove>()
    private val partialMovesSecondDie = mutableListOf<SingleMove>()

    private var anyFullSequenceFound = false

    override fun findAllImpl() {
        // Find bar untriedMoves only when there are any checkers which must be moved
        if (playerCheckers.barCheckers > 0) {
            findBarPartialMoves()

            // Find only bar sequences
            if (playerCheckers.barCheckers >= 2) {
                if (moveFromBarFirstDie != null && moveFromBarSecondDie != null)
                    addFullMove(moveFromBarFirstDie!!, moveFromBarSecondDie!!)
                else if (moveFromBarFirstDie != null)
                    addFullMove(moveFromBarFirstDie!!)
                else if (moveFromBarSecondDie != null)
                    addFullMove(moveFromBarSecondDie!!)

                return // Finish finding untriedMoves. No more possible untriedMoves
            } else if (moveFromBarFirstDie == null && moveFromBarSecondDie == null) {
                return
            }
        }

        // Find sequences of a bar checker and the preceding normal move
        if (playerCheckers.barCheckers == ONE_BYTE) {
            findStandardPartialMoves()

            findStandardFullMoves(singletonOrEmptyList(moveFromBarFirstDie), partialMovesSecondDie, dice.second, false)
            findStandardFullMoves(singletonOrEmptyList(moveFromBarSecondDie), partialMovesFirstDie, dice.first, false)

            if (fullMoves.isEmpty()) {
                if (moveFromBarFirstDie != null)
                    addFullMove(moveFromBarFirstDie!!)
                if (moveFromBarSecondDie != null)
                    addFullMove(moveFromBarSecondDie!!)
            }
        }
        // Find normal sequences
        else {
            findStandardPartialMoves()

            findStandardFullMoves(partialMovesFirstDie, partialMovesSecondDie, dice.second, playerCheckers.canBearOff)
            findStandardFullMoves(partialMovesSecondDie, partialMovesFirstDie, dice.first, playerCheckers.canBearOff)

            // Find full bear off untriedMoves
            if (playerCheckers.canBearOff) {
                findBearOffFullMoves()
            }
            // Find bear off sequence with the preceding normal move
            else if (findPreBearOffPartialMoves()) {
                findBearOffAfterPreBearOffFullMoves()
            }

            if (fullMoves.isEmpty()) {
                partialMovesFirstDie.forEach(this::addFullMove)
                partialMovesSecondDie.forEach(this::addFullMove)
            }
        }
    }

    private fun findBarPartialMoves() {
        moveFromBarFirstDie = findPartialBarMove(dice.first)
        moveFromBarSecondDie = findPartialBarMove(dice.second)
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            val firstMove = findStandardPartialMoveForTower(tower.index, dice.first)
            if (firstMove != null) {
                partialMovesFirstDie.add(firstMove)
            }

            val secondMove = findStandardPartialMoveForTower(tower.index, dice.second)
            if (secondMove != null) {
                partialMovesSecondDie.add(secondMove)
            }
        }
    }

    private fun findStandardFullMoves(firstPartialMoves: Collection<SingleMove>, secondPartialMoves: Collection<SingleMove>,
                                      secondDice: Byte, bearingOff: Boolean) {

        for (move1 in firstPartialMoves) {
            findSequentialFullMove(move1, secondDice, bearingOff)

            for (move2 in secondPartialMoves) {
                if (isMovePossibleAfterMove(move1, move2)) {
                    addFullMove(move1, move2)
                    anyFullSequenceFound = true
                }
            }
        }
    }

    private fun isMovePossibleAfterMove(previousMove: SingleMove, move: SingleMove): Boolean {
        if (previousMove.oldIndex == move.oldIndex) {
            return playerCheckers.get(previousMove.oldIndex) > 1
        }
        return true
    }

    private fun findSequentialFullMove(previousMove: SingleMove, die: Byte, bearingOff: Boolean) {
        if (bearingOff) {// Check bear off move first
            val bearOffMove = findPartialBearOffMove(previousMove, die)
            if (bearOffMove != null) {
                addFullMove(previousMove, bearOffMove)
                anyFullSequenceFound = true
                return
            }
        }

        val newIndex = findMove(previousMove.newIndex, die)
        if (newIndex != NO_INDEX) {
            addFullMove(previousMove, SingleMove(previousMove.newIndex, newIndex))
            anyFullSequenceFound = true
        }
    }


    private fun findBearOffFullMoves() {
        val firstBearOffMove = findPartialBearOffMove(null, dice.first)
        if (firstBearOffMove != null) {
            val secondBearOffMove = findPartialBearOffMove(firstBearOffMove, dice.second)
            if (secondBearOffMove != null) {
                addFullMove(firstBearOffMove, secondBearOffMove)
                anyFullSequenceFound = true
            }
            if (!anyFullSequenceFound) {
                addFullMove(firstBearOffMove)
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
                val firstDieNewIndex = findMove(nonHomeIndex, dice.first)
                val secondDieNewIndex = findMove(nonHomeIndex, dice.second)

                if (firstDieNewIndex != NO_INDEX && isOnHomeBoard(firstDieNewIndex))
                    partialPreBearOffMoveFirstDie = SingleMove(nonHomeIndex, firstDieNewIndex)
                if (secondDieNewIndex != NO_INDEX && isOnHomeBoard(secondDieNewIndex))
                    partialPreBearOffMoveSecondDie = SingleMove(nonHomeIndex, secondDieNewIndex)

                return partialPreBearOffMoveFirstDie != null || partialPreBearOffMoveSecondDie != null
            }
        }
        return false
    }

    private fun findBearOffAfterPreBearOffFullMoves() {
        if (partialPreBearOffMoveFirstDie != null) {
            findBearOffAfterPreBearOffFullMove(partialPreBearOffMoveFirstDie!!, dice.second)
        }

        if (partialPreBearOffMoveSecondDie != null) {
            findBearOffAfterPreBearOffFullMove(partialPreBearOffMoveSecondDie!!, dice.first)
        }
    }

    private fun findBearOffAfterPreBearOffFullMove(previousMove: SingleMove, die: Byte) {
        val move = findPartialBearOffMove(previousMove, die)
        if (move != null) {
            addFullMove(previousMove, move)
            anyFullSequenceFound = true
        }
    }


    private fun findPartialBearOffMove(previousMove: SingleMove?, die: Byte): SingleMove? {
        val indices = playerCheckers.homeTowersIndices().toMutableSet()

        if (previousMove != null) {
            // Remove the tower if it's empty after move
            if (playerCheckers.get(previousMove.oldIndex) == ONE_BYTE)
                indices.remove(previousMove.oldIndex)

            // Add new tower if previous move was not bearing off
            if (previousMove.newIndex != BEAR_OFF_INDEX)
                indices.add(previousMove.newIndex)
        }

        return firstForBearingOff(indices, die)
    }

    private fun addFullMove(move: SingleMove) {
        fullMoves.add(FullMovesBuilder(dice).append(move).build())
    }

    private fun addFullMove(move1: SingleMove, move2: SingleMove) {
        fullMoves.add(FullMovesBuilder(dice).append(move1, move2).build())
    }

}