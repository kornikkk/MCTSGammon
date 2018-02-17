package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDice
import java.util.*

/**
 * Created by kkarolcz on 27.12.2017.
 */
class FullMovesSearchDoubling(board: BackgammonBoard, currentPlayer: BackgammonPlayer, dice: BackgammonDice)
    : AbstractFullMovesSearch(board, currentPlayer, dice) {

    private val die = dice.first

    private var moveFromBar: BackgammonMove? = null

    private val partialMoves = mutableListOf<BackgammonMove>()
    private val sequentialMoves = SequencesForPartialMoves()

    private var diceLeft = 4

    override fun findAllImpl() {
        if (playerCheckers.barCheckers > 0) {
            moveFromBar = findPartialBarMove(die)
            if (playerCheckers.barCheckers >= 4) {
                fullMoves.add(BackgammonMovesSequence(moveFromBar, moveFromBar, moveFromBar, moveFromBar))
                return
            }
            diceLeft -= playerCheckers.barCheckers
        }
        findStandardPartialMoves()
        findStandardSequentialMoves()
        findFullMovesRecursive()
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            val move = findStandardPartialMoveForTower(tower.index, die)
            if (move != null) {
                for (i in 1..minOf(tower.checkers.toInt(), diceLeft)) {
                    partialMoves.add(move)
                }
            }
        }
    }

    private fun findStandardSequentialMoves() {
        for (partialMove in partialMoves.distinct()) {
            var diceLeft = this.diceLeft - 1 // We assume that one of the partial moves is used
            var oldIndex: Byte
            var newIndex = partialMove.newIndex

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == NO_INDEX) {
                    break
                }

                sequentialMoves.addToSequence(partialMove, BackgammonMove.create(oldIndex, newIndex))
            }
        }
    }

    private fun findFullMovesRecursive() {
        findFullMovesRecursive(FullMoveBuilder(), partialMoves, SequencesList(), diceLeft)
    }

    private fun findFullMovesRecursive(fullMoveBuilder: FullMoveBuilder, partialMoves: List<BackgammonMove>,
                                       sequences: SequencesList, diceLeft: Int) {

        //TODO handle situation when dice left
        if (diceLeft == 0) {
            //TODO Choose only maximum length (example: 3 moves should not be chosen if 4 moves were found
            fullMoves.add(fullMoveBuilder.build())
        }

        //TODO bear off


        //TODO That works wrong. Something should be cloned
        for (sequence in sequences) {
            findFullMovesWithSequence(fullMoveBuilder.clone(), partialMoves, sequences, diceLeft, sequence)
        }

        for (partialMove in partialMoves) {
            findFullMovesWithPartialMove(fullMoveBuilder.clone(), partialMoves, sequences.clone(), diceLeft, partialMove)
        }
        //TODO handle situation when dice left
    }

    private fun findFullMovesWithSequence(fullMoveBuilder: FullMoveBuilder, partialMoves: List<BackgammonMove>,
                                          sequences: SequencesList, diceLeft: Int, sequence: SequenceForPartialMove) {

        val sequentialMove = sequence.poll()
        if (sequentialMove != null) {
            fullMoveBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(fullMoveBuilder.clone(), partialMoves, sequences.clone(), diceLeft - 1)
    }

    private fun findFullMovesWithPartialMove(fullMoveBuilder: FullMoveBuilder, partialMoves: List<BackgammonMove>,
                                             sequences: SequencesList, diceLeft: Int, partialMove: BackgammonMove) {
        fullMoveBuilder.append(partialMove)

        val sequencesCopy = sequences.clone()
        val sequenceForPartialMove = sequentialMoves.getSequence(partialMove)
        if (sequenceForPartialMove != null) {
            sequencesCopy.add(sequenceForPartialMove)
        }

        val partialMovesCopy = partialMoves.toMutableList()
        partialMovesCopy.remove(partialMove)

        findFullMovesRecursive(fullMoveBuilder.clone(), partialMovesCopy, sequencesCopy, diceLeft - 1)
    }

    private class SequencesList : Cloneable, Iterable<SequenceForPartialMove> {

        private val list = mutableListOf<SequenceForPartialMove>()

        constructor()

        private constructor(other: SequencesList) {
            other.list.filterNot { it.isEmpty() }.forEach { add(it) }
        }

        override fun iterator(): Iterator<SequenceForPartialMove> = list.iterator()

        public override fun clone(): SequencesList {
            return SequencesList(this)
        }

        fun isEmpty(): Boolean = list.isEmpty()

        fun add(sequence: SequenceForPartialMove) {
            list.add(sequence.clone())
        }

    }


    private class SequencesForPartialMoves {
        private val sequentialMoves = mutableMapOf<BackgammonMove, SequenceForPartialMove>()

        fun addToSequence(partialMove: BackgammonMove, nextMove: BackgammonMove) {
            sequentialMoves.computeIfAbsent(partialMove) { SequenceForPartialMove() }.add(nextMove)
        }

        fun getSequence(partialMove: BackgammonMove): SequenceForPartialMove? {
            return sequentialMoves.getOrDefault(partialMove, null)
        }
    }

    private class SequenceForPartialMove : Cloneable {
        private val sequence = LinkedList<BackgammonMove>()

        constructor()

        private constructor(other: SequenceForPartialMove) {
            sequence.addAll(other.sequence)
        }

        fun add(move: BackgammonMove) {
            sequence.add(move)
        }

        fun isEmpty() = sequence.isEmpty()

        fun poll(): BackgammonMove? {
            return sequence.pollFirst()
        }

        public override fun clone() = SequenceForPartialMove(this)

    }
}