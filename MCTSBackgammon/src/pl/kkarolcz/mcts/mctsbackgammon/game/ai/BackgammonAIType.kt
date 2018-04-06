package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress

/**
 * Created by kkarolcz on 04.04.2018.
 */
enum class BackgammonAIType(val aiName: String) {
    MCTS_WITH_UCT("MCTS with UCT") {
        override fun create(progress: BackgammonGamesProgress) = MCTSWithUCT(progress)
    },
    SINGLE_SH_ON_ROOT_THEN_MCTS_WITH_UCT("Single SH on root, MCTS with UCT") {
        override fun create(progress: BackgammonGamesProgress) = SHOnRootSingleThenMCTSWithUCT(progress)
    },
    DOUBLE_SH_ON_ROOT_THEN_MCTS_WITH_UCT("Double SH on root, MCTS with UCT") {
        override fun create(progress: BackgammonGamesProgress): BackgammonAI = SHOnRootDoubleThenMCTSWithUCT(progress)
    },
    QUADRUPLE_SH_ON_ROOT_THEN_MCTS_WITH_UCT("Quadruple SH on root, MCTS with UCT") {
        override fun create(progress: BackgammonGamesProgress): BackgammonAI = SHOnRootQuadrupleThenMCTSWithUCT(progress)
    },
    LOGARITHMIC_SH_ON_ROOT_THEN_MCTS_WITH_UCT("Logarithmic SH on root, MCTS with UCT") {
        override fun create(progress: BackgammonGamesProgress): BackgammonAI = SHOnRootLogarithmicThenMCTSWithUCT(progress)
    },
    SH_ON_ROOT("Sequential Halving on root") {
        override fun create(progress: BackgammonGamesProgress): BackgammonAI = SHOnRoot(progress)
    };

    abstract fun create(progress: BackgammonGamesProgress): BackgammonAI

}