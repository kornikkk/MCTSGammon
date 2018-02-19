package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import java.util.*

/**
 * Created by kkarolcz on 27.12.2017.
 */
class FullMovesSearchDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractFullMovesSearch(board, currentPlayer, dice) {

    private val die = dice.first

    private val barMoves = mutableListOf<PartialMove>()
    private val barSequentialMoves = SequencesForPartialMoves()

    private val partialMoves = mutableListOf<PartialMove>()
    private val standardSequentialMoves = SequencesForPartialMoves()

    private var diceLeft = 4

    private var currentFullMoveMaxLength = 0

    override fun findAllImpl() {
        if (playerCheckers.barCheckers > 0) {
            findBarMoves()

            if (playerCheckers.barCheckers >= 4) {
                fullMoves.add(FullMove.create(barMoves.map(PartialMove::move)))
                return // No need to do anything when there's >= 4 checkers on the bar
            }
            diceLeft -= playerCheckers.barCheckers
        }

        findBarSequentialMoves()
        findStandardPartialMoves()
        findStandardSequentialMoves()

        findFullMovesRecursive()
    }

    private fun findBarMoves() {
        val moveFromBar = findPartialBarMove(die) ?: return // Stop if there are checkers on the bar and no possible moves
        for (i in 1..minOf(playerCheckers.barCheckers, 4)) {
            barMoves.add(PartialMove(moveFromBar))
        }
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

    private fun findBarSequentialMoves() {
        // No need to decrement dice like in findStandardSequentialMoves(). It was already done because bar moves exist
        findSequentialMoves(barMoves, barSequentialMoves, diceLeft)
    }

    private fun findStandardSequentialMoves() {
        // We assume that one of the partial moves is used so dice are decremented
        findSequentialMoves(partialMoves, standardSequentialMoves, diceLeft - 1)
    }

    private fun findSequentialMoves(initialMoves: List<PartialMove>, sequences: SequencesForPartialMoves, initialDiceLeft: Int) {
        for (partialMove in initialMoves.distinct()) {
            var diceLeft = initialDiceLeft
            var oldIndex: Byte
            var newIndex = partialMove.move.newIndex

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == NO_INDEX) {
                    break
                }

                sequences.getOrCreate(partialMove).add(SingleMove.create(oldIndex, newIndex))
            }
        }
    }

    private fun findFullMovesRecursive() {
        findFullMovesRecursive(RecursionState(), diceLeft)
    }

    private fun findFullMovesRecursive(recursionState: RecursionState, diceLeft: Int) {
        val fullMoveBuilder = recursionState.fullMovesBuilder
        val barMovesSequences = recursionState.barMovesSequences
        val partialMoves = recursionState.partialMoves
        val sequences = recursionState.standardSequences

        //TODO handle situation when dice left
        if (diceLeft > 0) {
            //TODO bear off

            for (sequence in barMovesSequences) {
                findFullMovesWithBarequence(recursionState.clone(), diceLeft, sequence.key)
            }

            for (sequence in sequences) {
                findFullMovesWithSequence(recursionState.clone(), diceLeft, sequence.key)
            }

            for (partialMove in partialMoves) {
                findFullMovesWithPartialMove(recursionState.clone(), diceLeft, partialMove)
            }
        }
        addFullMoveIfCorrect(fullMoveBuilder.build())

        //TODO handle situation when dice left
    }

    private fun findFullMovesWithBarequence(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        val sequentialMove = recursionState.barMovesSequences.getSequence(partialMove)?.poll()
        if (sequentialMove != null) {
            recursionState.fullMovesBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun findFullMovesWithSequence(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        val sequentialMove = recursionState.standardSequences.getSequence(partialMove)?.poll()
        if (sequentialMove != null) {
            recursionState.fullMovesBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun findFullMovesWithPartialMove(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        recursionState.fullMovesBuilder.append(partialMove.move)

        val sequenceForPartialMove = standardSequentialMoves.getSequence(partialMove)
        if (sequenceForPartialMove != null) {
            recursionState.standardSequences.add(sequenceForPartialMove)
        }

        recursionState.partialMoves.remove(partialMove)

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun addFullMoveIfCorrect(fullMove: FullMove) {
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

    private inner class RecursionState : Cloneable {

        val fullMovesBuilder: FullMovesBuilder
        val barMovesSequences: SequencesForPartialMoves
        val partialMoves: MutableList<PartialMove>
        val standardSequences: SequencesForPartialMoves

        constructor() {
            this.fullMovesBuilder = FullMovesBuilder(this@FullMovesSearchDoubling.barMoves.map(PartialMove::move))
            this.barMovesSequences = SequencesForPartialMoves()
            this.partialMoves = this@FullMovesSearchDoubling.partialMoves
            this.standardSequences = SequencesForPartialMoves()

            addAllBarMovesSequences()
        }

        constructor(other: RecursionState) {
            this.fullMovesBuilder = other.fullMovesBuilder.clone()
            this.barMovesSequences = other.barMovesSequences.clone()
            this.partialMoves = other.partialMoves.toMutableList()
            this.standardSequences = other.standardSequences.clone()
        }

        private fun addAllBarMovesSequences() {
            for (barMove in barMoves) {
                val sequence = this@FullMovesSearchDoubling.barSequentialMoves.getSequence(barMove)
                if (sequence != null) {
                    barMovesSequences.add(sequence)
                }
            }
        }

        public override fun clone(): RecursionState {
            return RecursionState(this)
        }

    }

    private class PartialMove(val move: SingleMove)

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
        private val sequence = LinkedList<SingleMove>()

        constructor(key: PartialMove) {
            this.key = key
        }

        private constructor(other: SequenceForPartialMove) {
            this.key = other.key
            sequence.addAll(other.sequence)
        }

        fun add(move: SingleMove) {
            sequence.add(move)
        }

        fun isEmpty() = sequence.isEmpty()

        fun poll(): SingleMove? {
            return sequence.pollFirst()
        }

        public override fun clone() = SequenceForPartialMove(this)

    }
}