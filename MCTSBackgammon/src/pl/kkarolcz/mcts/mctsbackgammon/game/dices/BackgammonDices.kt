package pl.kkarolcz.mcts.mctsbackgammon.game.dices

import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonDices private constructor(val dice1Value: Dice, val dice2Value: Dice) {

    val doubling = dice1Value == dice2Value

    companion object {
        private val random = Random()
        fun throwDices() = BackgammonDices(throwSingleDice(), throwSingleDice())

        private fun throwSingleDice(): Dice = Dice(random.nextInt(6) + 1)
    }


}