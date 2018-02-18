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

    private val partialMoves = mutableListOf<PartialMove>()
    private val sequentialMoves = SequencesForPartialMoves()

    private var diceLeft = 4

    private var currentFullMoveMaxLength = 0

    override fun findAllImpl() {
        if (playerCheckers.barCheckers > 0) {
            moveFromBar = findPartialBarMove(die)
            if (moveFromBar == null) {
                return
            }
            if (playerCheckers.barCheckers >= 4) {
                fullMoves.add(BackgammonMovesSequence.create(moveFromBar!!, moveFromBar!!, moveFromBar!!, moveFromBar!!))
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
                    partialMoves.add(PartialMove(move))
                }
            }
        }
    }

    private fun findStandardSequentialMoves() {
        for (partialMove in partialMoves.distinct()) {
            var diceLeft = this.diceLeft - 1 // We assume that one of the partial moves is used
            var oldIndex: Byte
            var newIndex = partialMove.move.newIndex

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == NO_INDEX) {
                    break
                }

                sequentialMoves.getOrCreate(partialMove).add(BackgammonMove.create(oldIndex, newIndex))
            }
        }
    }

    private fun findFullMovesRecursive() {
        findFullMovesRecursive(RecursionState(FullMoveBuilder(), partialMoves, SequencesForPartialMoves()), diceLeft)
    }

    private fun findFullMovesRecursive(recursionState: RecursionState, diceLeft: Int) {
        val fullMoveBuilder = recursionState.fullMoveBuilder
        val partialMoves = recursionState.partialMoves
        val sequences = recursionState.sequences

        //TODO handle situation when dice left
        if (diceLeft == 0) {
            //TODO Choose only maximum length (example: 3 moves should not be chosen if 4 moves were found
            addFullMoveIfCorrect(fullMoveBuilder.build())
            return
        }

        //TODO bear off

        for (sequence in sequences) {
            findFullMovesWithSequence(recursionState.clone(), diceLeft, sequence.key)
        }

        for (partialMove in partialMoves) {
            findFullMovesWithPartialMove(recursionState.clone(), diceLeft, partialMove)
        }


        //TODO handle situation when dice left
    }

    private fun findFullMovesWithSequence(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        val sequentialMove = recursionState.sequences.getSequence(partialMove)?.poll()
        if (sequentialMove != null) {
            recursionState.fullMoveBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun findFullMovesWithPartialMove(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        recursionState.fullMoveBuilder.append(partialMove.move)

        val sequenceForPartialMove = sequentialMoves.getSequence(partialMove)
        if (sequenceForPartialMove != null) {
            recursionState.sequences.add(sequenceForPartialMove)
        }

        recursionState.partialMoves.remove(partialMove)

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun addFullMoveIfCorrect(fullMove: BackgammonMovesSequence) {
        val length = fullMove.length()
        if (length < currentFullMoveMaxLength) {
            return
        }
        if (length > currentFullMoveMaxLength) {
            currentFullMoveMaxLength = length
            fullMoves.clear()
        }
        fullMoves.add(fullMove)

    }

    private class RecursionState(fullMoveBuilder: FullMoveBuilder, partialMoves: MutableList<PartialMove>,
                                 sequences: SequencesForPartialMoves) : Cloneable {

        val fullMoveBuilder: FullMoveBuilder = fullMoveBuilder.clone()
        val partialMoves: MutableList<PartialMove> = partialMoves.toMutableList()
        val sequences: SequencesForPartialMoves = sequences.clone()

        public override fun clone(): RecursionState {
            return RecursionState(fullMoveBuilder, partialMoves, sequences)
        }

    }

    private class PartialMove(val move: BackgammonMove)

    private class SequencesForPartialMoves : Cloneable, Iterable<SequenceForPartialMove> {
        private val map = mutableMapOf<PartialMove, SequenceForPartialMove>()

        fun getOrCreate(key: PartialMove): SequenceForPartialMove {
            return map.computeIfAbsent(key) { SequenceForPartialMove(key) }
        }

        fun getSequence(partialMove: PartialMove): SequenceForPartialMove? {
            return map.getOrDefault(partialMove, null)
        }

        fun add(sequence: SequenceForPartialMove) {
            map.put(sequence.key, sequence.clone())
        }

        override fun iterator(): Iterator<SequenceForPartialMove> = map.values.iterator()

        public override fun clone(): SequencesForPartialMoves {
            val copy = SequencesForPartialMoves()
            for (entry in map) {
                if (!entry.value.isEmpty()) {
                    copy.map.put(entry.key, entry.value.clone())
                }
            }
            return copy
        }
    }

    private class SequenceForPartialMove : Cloneable {
        val key: PartialMove
        private val sequence = LinkedList<BackgammonMove>()

        constructor(key: PartialMove) {
            this.key = key
        }

        private constructor(other: SequenceForPartialMove) {
            this.key = other.key
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