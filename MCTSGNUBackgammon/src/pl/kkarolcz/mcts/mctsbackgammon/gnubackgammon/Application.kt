package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammonApplication
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GameSettings
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.GNUBackgammonBinaryFileChooser
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.MainWindow
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.ApplicationIconUtils
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.GNUBackgammonServerSocket
import java.io.PrintStream
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

/**
 * Created by kkarolcz on 22.03.2018.
 */
object Application : AutoCloseable {

    private val mainWindow = MainWindow(::startGames)

    private val gnuBackgammonMCTS = GNUBackgammonMCTS()
    private val gnuBackgammonServerSocket = GNUBackgammonServerSocket(gnuBackgammonMCTS)

    private val gnuBackgammonApplication = GNUBackgammonApplication(::onSingleGameFinished)

    init {
        ApplicationIconUtils.setIcons(mainWindow)
        mainWindow.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    fun run() {
        mainWindow.isVisible = true
        mainWindow.setLocationRelativeTo(null)

        checkIfGnuBackgammonBinarySet()

        gnuBackgammonApplication.start()

        redirectStreams()

        gnuBackgammonServerSocket.startServer()
    }

    override fun close() {
        gnuBackgammonServerSocket.close()
        gnuBackgammonApplication.close()
    }


    private fun checkIfGnuBackgammonBinarySet() {
        if (Settings.gnuBackgammonBinary == null) {
            Settings.gnuBackgammonBinary = GNUBackgammonBinaryFileChooser.chooseFile(mainWindow).absolutePath
        }
    }

    private fun redirectStreams() {
        Thread {
            while (true) {
                val line = gnuBackgammonApplication.inputReader.readLine() ?: break
                mainWindow.gnuBackgammonTextAreaOutputStream.write((line + "\n").toByteArray())
            }
        }.start()

        System.setOut(PrintStream(mainWindow.programConsoleTextAreaOutputStream))
    }

    private fun startGames(gameSettings: GameSettings) {
        SwingUtilities.invokeLater {
            if (!gnuBackgammonApplication.connected) {
                gnuBackgammonApplication.connect(gnuBackgammonServerSocket.port)
            }
            gnuBackgammonMCTS.reset(gameSettings.simulationsLimit)
            gnuBackgammonApplication.startNewGame(gameSettings)
        }
    }

    private fun onSingleGameFinished(winner: Player) {
        println("Game finished. Winner: $winner")
        println()
        gnuBackgammonMCTS.reset()
        // println("Game $gamesCounter finished")

    }

}
