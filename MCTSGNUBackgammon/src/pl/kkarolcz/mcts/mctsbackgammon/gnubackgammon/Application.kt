package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammon
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GamesProperties
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.GNUBackgammonBinaryFileChooser
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.MainWindow
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.ApplicationIconUtils
import java.io.PrintStream
import javax.swing.WindowConstants

/**
 * Created by kkarolcz on 22.03.2018.
 */
object Application : AutoCloseable {

    private val mainWindow = MainWindow(::onGamesStarted)

    private lateinit var gnuBackgammon: GNUBackgammon
    private lateinit var gnuBackgammonMCTS: GNUBackgammonMCTS

    init {
        ApplicationIconUtils.setIcons(mainWindow)
        mainWindow.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        mainWindow.setLocationRelativeTo(null)
    }

    fun run() {
        mainWindow.isVisible = true

        checkIfGnuBackgammonBinarySet()

        System.setOut(PrintStream(mainWindow.programConsoleTextAreaOutputStream))

        gnuBackgammon = GNUBackgammon(Settings.gnuBackgammonBinary!!)
        gnuBackgammon.start()
        gnuBackgammon.redirectInputStream(mainWindow.gnuBackgammonTextAreaOutputStream)

        gnuBackgammonMCTS = GNUBackgammonMCTS(gnuBackgammon)
        //TODO gnuBackgammonMCTS.progress.addProgressListener() for window (button etc.) and statistics
    }

    override fun close() {
        if (::gnuBackgammon.isInitialized) gnuBackgammon.close()
    }

    private fun onGamesStarted(gamesProperties: GamesProperties) {
        gnuBackgammonMCTS.startNewGamesSequence(gamesProperties)
    }

    private fun checkIfGnuBackgammonBinarySet() {
        if (Settings.gnuBackgammonBinary == null) {
            Settings.gnuBackgammonBinary = GNUBackgammonBinaryFileChooser.chooseFile(mainWindow).absolutePath
        }
    }

}
