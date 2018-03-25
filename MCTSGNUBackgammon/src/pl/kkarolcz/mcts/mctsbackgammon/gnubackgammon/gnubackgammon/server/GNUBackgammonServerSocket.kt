package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.server

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GameListener
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.convertToBackgammonState
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.toGNUBackgammonFormat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.SocketException


/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonServerSocket : AutoCloseable {

    val port: Int get() = server.localPort

    private val server = ServerSocket(0)
    private lateinit var outputWriter: PrintWriter

    private val gameListeners = mutableListOf<GameListener>()

    fun startServer() {
        println("Server socket started. Waiting for connection on port ${server.localPort}...")
        Thread {
            listenOnSocket()
        }.start()
    }

    fun addGameListener(listener: GameListener) {
        gameListeners.add(listener)
    }

    fun doMove(move: FullMove) {
        outputWriter.println(move.toGNUBackgammonFormat())
    }

    override fun close() {
        outputWriter.close()
        server.close()
    }

    private fun requestDiceRoll() {
        outputWriter.println("roll")
    }

    private fun listenOnSocket() {
        val socket = server.accept()
//        println("Connected to " + socket.remoteSocketAddress)

        try {
            outputWriter = PrintWriter(socket.getOutputStream(), true)
            BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
                while (true) {
                    val data = reader.readLine()?.replace("\u0000", "")
                    if (data != null) {
                        handleData(data)
                    }
                }
            }
        } catch (e: SocketException) {
            println(e.message)
        }
    }

    private fun handleData(data: String) {
        if (data.startsWith("board")) {
            val boardInfo = BoardInfoDecoder.decode(data)
            when (requestForDiceRollRequired(boardInfo)) {
                true -> requestDiceRoll()
                else -> gameListeners.forEach { l -> l.onNextRound(boardInfo.convertToBackgammonState()) }
            }
        }
    }

    private fun requestForDiceRollRequired(boardInfo: BoardInfo): Boolean =
            boardInfo.playerDice1 == 0 && boardInfo.playerDice2 == 0 && boardInfo.opponentDice1 == 0 && boardInfo.opponentDice2 == 0

}
