package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSMovesProvider
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove

/**
 * Created by kkarolcz on 30.03.2018.
 */
interface BackgammonMovesProvider : MCTSMovesProvider<FullMove> {
    fun resetDice(newDice: Dice?)
    fun discardOtherDice(dice: Dice)
}