package pl.kkarolcz.mctsgammon.mcts

import java.util.*


/**
 * Created by kkarolcz on 25.07.2017.
 */
class State {
    val board: Board
    var playerNo: Int = 0
    var visitCount: Int = 0
    var winScore: Double = 0.0

    constructor(board: Board) {
        this.board = board.clone()
    }

    constructor(state: State) {
        this.board = state.board.clone()
        this.playerNo = state.playerNo
        this.visitCount = state.visitCount
        this.winScore = state.winScore
    }


    internal val opponent: Int
        get() = 3 - playerNo

    val allPossibleStates: List<State>
        get() {
            val possibleStates = ArrayList<State>()
            val availablePositions = this.board.getEmptyPositions()
            availablePositions.forEach { p ->
                val newState = State(this.board)
                newState.playerNo = 3 - this.playerNo
                newState.board.performMove(newState.playerNo, p)
                possibleStates.add(newState)
            }
            return possibleStates
        }

    fun incrementVisit() {
        this.visitCount++
    }

    fun addScore(score: Double) {
        this.winScore += score
    }

    fun randomPlay() {
        val availablePositions = this.board.getEmptyPositions()
        val totalPossibilities = availablePositions.size
        val selectRandom = (Math.random() * (totalPossibilities - 1 + 1)).toInt()
        this.board.performMove(this.playerNo, availablePositions.get(selectRandom))
    }

    fun togglePlayer() {
        this.playerNo = 3 - this.playerNo
    }
}