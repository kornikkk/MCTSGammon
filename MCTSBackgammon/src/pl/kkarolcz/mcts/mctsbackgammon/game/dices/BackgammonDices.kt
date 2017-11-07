package pl.kkarolcz.mcts.mctsbackgammon.game.dices

import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonDices constructor(dice1Value: Dice, dice2Value: Dice) {

    val doubling = dice1Value == dice2Value

    val values: List<Dice> = when (doubling) {
        true -> arrayListOf(dice1Value, dice1Value, dice2Value, dice2Value)
        false -> arrayListOf(dice1Value, dice2Value)
    }

    companion object {
        private val random = Random()
        fun throwDices() = BackgammonDices(throwSingleDice(), throwSingleDice())

        private fun throwSingleDice(): Dice = Dice(random.nextInt(6) + 1)
    }

}