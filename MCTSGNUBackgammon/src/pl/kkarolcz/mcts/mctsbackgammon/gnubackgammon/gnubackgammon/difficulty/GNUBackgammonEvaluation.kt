package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty

data class GNUBackgammonEvaluation(val lookahead: Int, val prune: Boolean, val cubeful: Boolean, val noise: Double, val deterministicNoise: Boolean)