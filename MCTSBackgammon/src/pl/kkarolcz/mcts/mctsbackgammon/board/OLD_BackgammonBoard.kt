package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.OLD_SingleBackgammonMove

/**
 * Created by kkarolcz on 24.08.2017.
 */
@Deprecated("REMOVE")
class OLD_BackgammonBoard : Cloneable {
    private val player1Checkers: OLD_BackgammonPlayerCheckers
    private val player2Checkers: OLD_BackgammonPlayerCheckers

    companion object {
        val SIZE = OLD_BackgammonBoardIndex.MAX_INDEX + 1
    }

    constructor(player1Checkers: OLD_BackgammonPlayerCheckers, player2Checkers: OLD_BackgammonPlayerCheckers) {
        this.player1Checkers = player1Checkers
        this.player2Checkers = player2Checkers
    }

    @Suppress("UNCHECKED_CAST")
    private constructor(other: OLD_BackgammonBoard) {
        this.player1Checkers = other.player1Checkers.clone()
        this.player2Checkers = other.player2Checkers.clone()
    }

    public override fun clone(): OLD_BackgammonBoard {
        return OLD_BackgammonBoard(this)
    }

    fun getPlayerCheckers(player: Player): OLD_BackgammonPlayerCheckers = when (player) {
        Player.PLAYER_ONE -> player1Checkers
        Player.PLAYER_TWO -> player2Checkers
    }

    fun doMove(player: Player, move: OLD_SingleBackgammonMove) {
        getPlayerCheckers(player).doMove(move)
    }

    fun undoMove(player: Player, move: OLD_SingleBackgammonMove) {
        getPlayerCheckers(player).undoMove(move)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OLD_BackgammonBoard

        if (player1Checkers != other.player1Checkers) return false
        if (player2Checkers != other.player2Checkers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player1Checkers.hashCode()
        result = 31 * result + player2Checkers.hashCode()
        return result
    }


}