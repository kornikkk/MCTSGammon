package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket


/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonServerSocket(private val gnuBackgammonInterface: GNUBackgammonReceiver) : AutoCloseable {
    private val server: ServerSocket = ServerSocket(667)

    fun startServer() {
        println("Waiting for connection on port " + server.localPort + "...")

        val socket = server.accept()
        println("Connected to " + socket.remoteSocketAddress)

        BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
            PrintWriter(socket.getOutputStream(), true).use { writer ->
                listenOnSocket(reader, writer)
            }
        }
    }

    private fun listenOnSocket(reader: BufferedReader, writer: PrintWriter) {
        while (true) {
            val data = reader.readLine().replace("\u0000", "")
            handleData(data, writer)
        }
    }

    private fun handleData(data: String, writer: PrintWriter) {
        if (data.startsWith("board")) {
            println("INFO: Board received...: $data")

            val boardInfo = BoardInfoDecoder.decode(data)
            when {
                requestForDicesRequired(boardInfo) -> {
                    println("INFO: No dice. Requesting for dice roll...")
                    writer.println("roll")
                }
                else -> gnuBackgammonInterface.onBoardInfoReceived(boardInfo, { response ->
                    println("INFO: Moving checkers: $response")
                    writer.println(response)
                })
            }
        }
    }

    private fun requestForDicesRequired(boardInfo: BoardInfo): Boolean =
            boardInfo.player1Dice1 == 0 && boardInfo.player1Dice2 == 0 && boardInfo.player2Dice1 == 0 && boardInfo.player2Dice2 == 0

    override fun close() {
        server.close()
    }

}
