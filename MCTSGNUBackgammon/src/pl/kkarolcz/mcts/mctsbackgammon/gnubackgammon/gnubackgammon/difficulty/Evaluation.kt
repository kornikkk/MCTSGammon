package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty

data class Evaluation(val lookahead: Int, val prune: Boolean, val cubeful: Boolean, val noise: Double, val deterministicNoise: Boolean)