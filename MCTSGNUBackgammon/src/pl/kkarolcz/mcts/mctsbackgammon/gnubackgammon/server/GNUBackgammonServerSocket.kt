package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.SocketException


/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonServerSocket(private val gnuBackgammonInterface: GNUBackgammonReceiver) : AutoCloseable {
    private val server = ServerSocket(0)

    val port: Int get() = server.localPort

    fun startServer() {
        println("Server socket started. Waiting for connection on port ${server.localPort}...")
        Thread {
            listenOnSocket()
        }.start()
    }

    override fun close() {
        server.close()
    }

    private fun listenOnSocket() {
        val socket = server.accept()
        println("Connected to " + socket.remoteSocketAddress)

        try {
            BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
                PrintWriter(socket.getOutputStream(), true).use { writer ->
                    while (true) {
                        val data = reader.readLine()?.replace("\u0000", "")
                        if (data != null) {
                            handleData(data, writer)
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            println(e.message)
        }
    }

    private fun handleData(data: String, writer: PrintWriter) {
        if (data.startsWith("board")) {
            val boardInfo = BoardInfoDecoder.decode(data)
            when (requestForDiceRollRequired(boardInfo)) {
                true -> writer.println("roll")
                else -> gnuBackgammonInterface.onBoardInfoReceived(boardInfo, { response ->
                    writer.println(response)
                })
            }
        }
    }

    private fun requestForDiceRollRequired(boardInfo: BoardInfo): Boolean =
            boardInfo.playerDice1 == 0 && boardInfo.playerDice2 == 0 && boardInfo.opponentDice1 == 0 && boardInfo.opponentDice2 == 0

}
