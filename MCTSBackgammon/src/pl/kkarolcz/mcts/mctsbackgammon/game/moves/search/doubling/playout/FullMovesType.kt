package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder

/**
 * Created by kkarolcz on 29.03.2018.
 */
interface FullMovesType {
    fun count(possibleMoves: PossibleMoves): Int
    fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove

}
