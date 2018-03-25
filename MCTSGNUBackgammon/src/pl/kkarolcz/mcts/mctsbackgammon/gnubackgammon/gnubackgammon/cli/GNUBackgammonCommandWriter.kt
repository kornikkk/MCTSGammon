package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.cli

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.cli.GNUBackgammonCommands.Command
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Created by kkarolcz on 23.03.2018.
 */
class GNUBackgammonCommandWriter(outputStream: OutputStream) : AutoCloseable {

    private val outputWriter = BufferedWriter(OutputStreamWriter(outputStream))

    fun send(command: Command) {
        command.forEach(::send)
    }

    override fun close() {
        outputWriter.close()
    }

    private fun send(singleCommand: String) {
        outputWriter.write(singleCommand + "\n")
        outputWriter.flush()
    }
}