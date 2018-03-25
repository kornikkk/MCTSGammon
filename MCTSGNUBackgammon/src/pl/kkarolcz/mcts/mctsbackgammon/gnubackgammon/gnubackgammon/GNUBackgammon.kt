package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.cli.GNUBackgammonApplication
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.server.GNUBackgammonServerSocket
import java.io.OutputStream

/**
 * Created by kkarolcz on 25.03.2018.
 */
class GNUBackgammon(binaryPath: String) : AutoCloseable {
    private val serverSocket = GNUBackgammonServerSocket()
    private val application = GNUBackgammonApplication(binaryPath)

    fun start() {
        serverSocket.startServer()
        application.startApplication()

        application.connect(serverSocket.port)
    }

    fun redirectInputStream(outputStream: OutputStream) {
        Thread {
            while (true) {
                val line = application.inputReader.readLine() ?: break
                outputStream.write((line + "\n").toByteArray())
            }
        }.start()
    }

    fun addGameListener(listener: GameListener) {
        serverSocket.addGameListener(listener)
        application.addGameListener(listener)
    }

    fun setDifficulty(difficulty: GNUBackgammonDifficulty) {
        application.setDifficulty(difficulty)
    }

    fun newGame() {
        application.newGame()
    }

    fun doMove(move: FullMove) {
        serverSocket.doMove(move)
    }

    override fun close() {
        serverSocket.close()
        application.close()
    }

}