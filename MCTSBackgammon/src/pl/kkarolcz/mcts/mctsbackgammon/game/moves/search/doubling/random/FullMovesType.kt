package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.PossibleMovesDoubling

/**
 * Created by kkarolcz on 29.03.2018.
 */
interface FullMovesType {
    fun count(possibleMoves: PossibleMovesDoubling): Int
    fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove

}
