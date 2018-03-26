package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.cli

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GameListener
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStreamReader
import java.io.Reader
import java.util.concurrent.BlockingQueue
import java.util.concurrent.SynchronousQueue


/**
 * Created by kkarolcz on 22.03.2018.
 */
class GNUBackgammonApplication(private val binaryPath: String) : Closeable {

    var connected = false
        private set

    val inputReader: BufferedReader get() = inputReaderProxy

    private lateinit var process: Process
    private lateinit var inputReaderProxy: InputReaderProxy
    private lateinit var commandWriter: GNUBackgammonCommandWriter

    private val gameListeners = mutableListOf<GameListener>()

    companion object {
        const val MCTS_PLAYER_NAME = "MCTS"
        const val GNU_BACKGAMMON_PLAYER_NAME = "GNUBackgammon"
    }

    fun startApplication() {
        if (::process.isInitialized) throw IllegalStateException("GNU Backgammon already started")

        val processBuilder = ProcessBuilder(binaryPath)
        processBuilder.redirectErrorStream(true)
        process = processBuilder.start()

        inputReaderProxy = InputReaderProxy(BufferedReader(InputStreamReader(process.inputStream)))
        commandWriter = GNUBackgammonCommandWriter(process.outputStream)

        Thread {
            listenForGameEnding()
        }.start()
    }

    /**
     * That method can be called only if the application is not already connected.
     */
    fun connect(port: Int) {
        if (connected) throw IllegalStateException("GNUBackgammon already connected")
        connected = true

        commandWriter.send(GNUBackgammonCommands.connect(port))
    }

    fun addGameListener(listener: GameListener) {
        gameListeners.add(listener)
    }

    fun setDifficulty(difficulty: GNUBackgammonDifficulty) {
        commandWriter.send(GNUBackgammonCommands.setUpNewGames(difficulty))
    }

    fun newGame() {
        commandWriter.send(GNUBackgammonCommands.startGame())
    }

    override fun close() {
        if (::process.isInitialized) process.destroy()
        if (::inputReaderProxy.isInitialized) inputReaderProxy.close()
        if (::commandWriter.isInitialized) commandWriter.close()
    }


    private fun listenForGameEnding() {
        while (true) {
            val line = inputReaderProxy.preProcessLine() ?: break
            if (line.contains("wins a single game")) {
                val winner = when (line.startsWith(MCTS_PLAYER_NAME)) {
                    true -> Player.MCTS
                    else -> Player.OPPONENT
                }
                gameListeners.forEach { l -> l.onGameFinished(winner) }
            }
        }
    }


    /**
     * Allows to read a line before it's read by a real reader. Needed to listen for game endings
     */
    private class InputReaderProxy(reader: Reader) : BufferedReader(reader) {
        private val linesBuffer: BlockingQueue<Line> = SynchronousQueue()

        fun preProcessLine(): String? {
            val line = super.readLine()
            linesBuffer.put(Line(line))
            return line
        }

        override fun readLine(): String? {
            return linesBuffer.take().line
        }

        /** To allow null string in the queue */
        private data class Line(val line: String?)
    }

}