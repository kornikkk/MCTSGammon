package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty

/**
 * Created by kkarolcz on 23.03.2018.
 */
enum class GNUBackgammonDifficulty(val userFriendlyName: String, val chequerPlay: Evaluation, val cubeDecisions: Evaluation) {
    BEGINNER("Beginner",
            Evaluation(0, false, true, 0.060, true),
            Evaluation(0, false, true, 0.060, true)
    ),

    CASUAL_PLAY("Casual player",
            Evaluation(0, false, true, 0.050, true),
            Evaluation(0, false, true, 0.050, true)
    ),

    INTERMEDIATE("Intermediate",
            Evaluation(0, false, true, 0.040, true),
            Evaluation(0, false, true, 0.040, true)
    ),

    ADVANCED("Advanced",
            Evaluation(0, false, true, 0.015, true),
            Evaluation(0, false, true, 0.015, true)
    ),
    EXPERT("Expert",
            Evaluation(0, false, true, 0.0, true),
            Evaluation(0, false, true, 0.0, true)
    ),
    WORLD_CLASS("World Class",
            Evaluation(2, true, true, 0.0, true),
            Evaluation(2, true, true, 0.0, true)
    ),
    SUPREMO("Supremo",
            Evaluation(2, true, true, 0.0, true),
            Evaluation(2, true, true, 0.0, true)
    ),
    GRANDMASTER("Grandmaster",
            Evaluation(3, true, true, 0.0, true),
            Evaluation(3, true, true, 0.0, true)
    ),
    FOUR_PLIES("4 plies",
            Evaluation(4, true, true, 0.0, true),
            Evaluation(4, true, true, 0.0, true)
    );

    companion object {
        fun booleanToGNUBackgammonFormat(value: Boolean) = when (value) {
            true -> "on"
            false -> "off"
        }
    }
}