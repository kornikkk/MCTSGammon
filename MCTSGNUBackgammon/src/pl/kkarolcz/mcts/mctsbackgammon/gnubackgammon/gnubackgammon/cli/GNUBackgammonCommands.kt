package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.cli

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonEvaluation
import java.io.File
import java.sql.Timestamp
import java.util.*
import java.text.SimpleDateFormat



/**
 * Created by kkarolcz on 23.03.2018.
 */
object GNUBackgammonCommands {

    fun connect(port: Int): Command = CommandBuilder()
            .append("set player 0 name ${GNUBackgammonApplication.MCTS_PLAYER_NAME}")
            .append("set player 1 name ${GNUBackgammonApplication.GNU_BACKGAMMON_PLAYER_NAME}")
            .append("set player 1 gnubg")
            .append("set player 0 external localhost:$port")
            .append("set sound enable off")
            .append("set automatic game off")
            .append("set jacoby off")
            .append("set cube use off")
            .build()

    fun setUpNewGames(difficulty: GNUBackgammonDifficulty): Command = CommandBuilder()
            .append("set matchlength 1")
            .append(setPlayerEvaluationParameter("chequerplay", difficulty.chequerPlay))
            .append(setPlayerEvaluationParameter("cubedecision", difficulty.cubeDecisions))
            .build()

    fun startGame(): Command = CommandBuilder()
            .append("new game")
            .build()

    fun saveMatch(): Command = CommandBuilder()
            .append("save match \"${File(".").canonicalPath}${File.separator}${SimpleDateFormat("yyyyMMddHHmm'.sgf'").format(Date())}\"")
            .build()

    private fun setPlayerEvaluationParameter(parameter: String, evaluation: GNUBackgammonEvaluation): Command = CommandBuilder()
            .append("set player 1 $parameter type evaluation")
            .append("set player 1 $parameter evaluation cubeful ${GNUBackgammonDifficulty.booleanToGNUBackgammonFormat(evaluation.cubeful)}")
            .append("set player 1 $parameter evaluation deterministic ${GNUBackgammonDifficulty.booleanToGNUBackgammonFormat(evaluation.deterministicNoise)}")
            .append("set player 1 $parameter evaluation noise ${evaluation.noise}")
            .append("set player 1 $parameter evaluation plies ${evaluation.lookahead}")
            .append("set player 1 $parameter evaluation prune ${evaluation.prune}")
            .build()

    class Command(private val singleCommands: List<String>) : Iterable<String> {
        override fun iterator(): Iterator<String> = singleCommands.iterator()
    }

    private class CommandBuilder {
        private val singleCommands = mutableListOf<String>()

        fun append(singleCommand: String) = apply { singleCommands.add(singleCommand) }

        fun append(command: Command) = apply { command.forEach { singleCommands.add(it) } }

        fun build(): Command = Command(singleCommands)
    }

}