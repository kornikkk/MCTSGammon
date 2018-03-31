package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 23.02.2018.
 */
interface MCTSMovesProvider<out M : MCTSMove> {
    fun hasUntriedMoves(): Boolean
    fun pollNextRandomUntriedMove(): M
    fun findMovesForPlayer(player: Player)
}