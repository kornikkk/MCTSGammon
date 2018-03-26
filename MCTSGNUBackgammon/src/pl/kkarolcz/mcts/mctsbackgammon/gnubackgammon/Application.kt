package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammon
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GNUBackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GamesProperties
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.GNUBackgammonBinaryFileChooser
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.MainWindow
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.ApplicationIconUtils
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.statistics.BackgammonMCTSProgressLogger
import java.io.Closeable
import java.io.PrintStream
import javax.swing.WindowConstants

/**
 * Created by kkarolcz on 22.03.2018.
 */
object Application : Closeable {
    private val settings = Settings()

    private val mainWindow = MainWindow()

    private lateinit var gnuBackgammon: GNUBackgammon
    private lateinit var gnuBackgammonMCTS: GNUBackgammonMCTS

    init {
        ApplicationIconUtils.setIcons(mainWindow)
        mainWindow.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        mainWindow.setLocationRelativeTo(null)
        mainWindow.addGamesStartedListener(::onGamesStarted)

        Runtime.getRuntime().addShutdownHook(Thread { close() })
    }

    fun run() {
        mainWindow.isVisible = true

        settings.load()
        checkIfGnuBackgammonBinarySet()

        System.setOut(PrintStream(mainWindow.programConsoleTextAreaOutputStream))

        gnuBackgammon = GNUBackgammon(settings.gnuBackgammonBinary!!)
        gnuBackgammon.start()
        gnuBackgammon.redirectInputStream(mainWindow.gnuBackgammonTextAreaOutputStream)

        gnuBackgammonMCTS = GNUBackgammonMCTS(gnuBackgammon)
        gnuBackgammonMCTS.addProgressListener(BackgammonMCTSProgressLogger())
        mainWindow.progressListeners.forEach(gnuBackgammonMCTS::addProgressListener)
    }

    override fun close() {
        if (::gnuBackgammon.isInitialized) gnuBackgammon.close()
    }

    private fun onGamesStarted(gamesProperties: GamesProperties) {
        gnuBackgammonMCTS.startNewGamesSequence(gamesProperties)
    }

    private fun checkIfGnuBackgammonBinarySet() {
        if (settings.gnuBackgammonBinary == null) {
            settings.gnuBackgammonBinary = GNUBackgammonBinaryFileChooser.chooseFile(mainWindow).absolutePath
        }
    }

}
