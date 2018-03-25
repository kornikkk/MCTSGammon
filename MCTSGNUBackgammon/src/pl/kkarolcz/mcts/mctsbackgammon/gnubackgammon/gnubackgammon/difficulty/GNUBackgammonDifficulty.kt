package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty

/**
 * Created by kkarolcz on 23.03.2018.
 */
enum class GNUBackgammonDifficulty(val userFriendlyName: String, val chequerPlay: GNUBackgammonEvaluation, val cubeDecisions: GNUBackgammonEvaluation) {
    BEGINNER("Beginner",
            GNUBackgammonEvaluation(0, false, true, 0.060, true),
            GNUBackgammonEvaluation(0, false, true, 0.060, true)
    ),

    CASUAL_PLAY("Casual player",
            GNUBackgammonEvaluation(0, false, true, 0.050, true),
            GNUBackgammonEvaluation(0, false, true, 0.050, true)
    ),

    INTERMEDIATE("Intermediate",
            GNUBackgammonEvaluation(0, false, true, 0.040, true),
            GNUBackgammonEvaluation(0, false, true, 0.040, true)
    ),

    ADVANCED("Advanced",
            GNUBackgammonEvaluation(0, false, true, 0.015, true),
            GNUBackgammonEvaluation(0, false, true, 0.015, true)
    ),
    EXPERT("Expert",
            GNUBackgammonEvaluation(0, false, true, 0.0, true),
            GNUBackgammonEvaluation(0, false, true, 0.0, true)
    ),
    WORLD_CLASS("World Class",
            GNUBackgammonEvaluation(2, true, true, 0.0, true),
            GNUBackgammonEvaluation(2, true, true, 0.0, true)
    ),
    SUPREMO("Supremo",
            GNUBackgammonEvaluation(2, true, true, 0.0, true),
            GNUBackgammonEvaluation(2, true, true, 0.0, true)
    ),
    GRANDMASTER("Grandmaster",
            GNUBackgammonEvaluation(3, true, true, 0.0, true),
            GNUBackgammonEvaluation(3, true, true, 0.0, true)
    ),
    FOUR_PLIES("4 plies",
            GNUBackgammonEvaluation(4, true, true, 0.0, true),
            GNUBackgammonEvaluation(4, true, true, 0.0, true)
    );

    companion object {
        fun booleanToGNUBackgammonFormat(value: Boolean) = when (value) {
            true -> "on"
            false -> "off"
        }
    }
}