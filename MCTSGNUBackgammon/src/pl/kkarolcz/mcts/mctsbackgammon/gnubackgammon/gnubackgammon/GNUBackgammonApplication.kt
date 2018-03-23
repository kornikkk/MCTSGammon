package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.Settings
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.Evaluation
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty.Companion.booleanToGNUBackgammonFormat
import java.io.*
import java.util.*
import java.util.concurrent.Semaphore


/**
 * Created by kkarolcz on 22.03.2018.
 */
class GNUBackgammonApplication(private val onSingleGameFinished: (Player) -> Unit) : AutoCloseable {

    private lateinit var process: Process
    var connected = false
        private set

    private lateinit var inputReaderProxy: InputReaderProxy
    val inputReader: BufferedReader get() = inputReaderProxy

    private lateinit var outputWriter: BufferedWriter

    companion object {
        const val MCTS_PLAYER_NAME = "MCTS"
        const val GNU_BACKGAMMON_PLAYER_NAME = "GNUBackgammon"
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            if (::process.isInitialized) process.destroy()
        })
    }

    fun start() {
        if (::process.isInitialized) throw IllegalStateException("GNU Backgammon already started")

        val processBuilder = ProcessBuilder(Settings.gnuBackgammonBinary)
        processBuilder.redirectErrorStream(true)
        process = processBuilder.start()

        inputReaderProxy = InputReaderProxy(BufferedReader(InputStreamReader(process.inputStream)))
        outputWriter = BufferedWriter(OutputStreamWriter(process.outputStream))

        Thread {
            listenForGameEnding()
        }.start()
    }

    override fun close() {
        if (::inputReaderProxy.isInitialized) inputReaderProxy.close()
        if (::outputWriter.isInitialized) outputWriter.close()
    }

    fun connect(port: Int) {
        if (connected) throw IllegalStateException("GNUBackgammon already connected")
        connected = true

        command("set player 0 name $MCTS_PLAYER_NAME")
        command("set player 1 name $GNU_BACKGAMMON_PLAYER_NAME")
        command("set player 1 gnubg")
        command("set player 0 external localhost:$port")
        command("set automatic game off")
    }

    fun startNewGame(gameSettings: GameSettings) {
        command("set matchlength 1")
        setDifficulty(gameSettings.difficulty)
        command("new game")
    }

    private fun setDifficulty(difficulty: GNUBackgammonDifficulty) {
        setEvaluationParameter("chequerplay", difficulty.chequerPlay)
        setEvaluationParameter("cubedecision", difficulty.chequerPlay)
    }

    private fun setEvaluationParameter(parameter: String, evaluation: Evaluation) {
        command("set player 1 $parameter type evaluation")
        command("set player 1 $parameter evaluation cubeful ${booleanToGNUBackgammonFormat(evaluation.cubeful)}")
        command("set player 1 $parameter evaluation deterministic ${booleanToGNUBackgammonFormat(evaluation.deterministicNoise)}")
        command("set player 1 $parameter evaluation noise ${evaluation.noise}")
        command("set player 1 $parameter evaluation plies ${evaluation.lookahead}")
        command("set player 1 $parameter evaluation prune ${evaluation.prune}")
    }

    private fun listenForGameEnding() {
        while (true) {
            val line = inputReaderProxy.preProcessLine() ?: break
            if (line.contains("wins a single game")) {
                val winner = when (line.startsWith(MCTS_PLAYER_NAME)) {
                    true -> Player.MCTS
                    else -> Player.OPPONENT
                }
                onSingleGameFinished.invoke(winner)
            }
        }
    }

    private fun command(command: String) {
        outputWriter.write(command + "\n")
        outputWriter.flush()
    }

    private class InputReaderProxy(reader: Reader) : BufferedReader(reader) {
        private val linesBuffer = Stack<String?>()
        private val readLock = Semaphore(0)

        fun preProcessLine(): String? {
            val line = super.readLine()
            linesBuffer.push(line)
            readLock.release()
            return line
        }

        override fun readLine(): String? {
            readLock.acquire()
            return linesBuffer.pop()
        }
    }

}