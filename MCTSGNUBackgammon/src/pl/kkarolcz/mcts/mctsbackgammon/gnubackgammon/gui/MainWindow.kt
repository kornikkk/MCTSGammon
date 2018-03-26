package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import com.jgoodies.forms.builder.FormBuilder
import com.jgoodies.forms.factories.CC.*
import com.jgoodies.forms.factories.Paddings
import com.jgoodies.forms.layout.FormLayout
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress.BackgammonMCTSProgressListener
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress.BackgammonMCTSProgressListenerAdapter
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GamesProperties
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.TextAreaOutputStream
import javax.swing.*


/**
 * Created by kkarolcz on 22.03.2018.
 */
class MainWindow : JFrame("GNU Backgammon MCTS") {
    private val gameSettingsPanel = GamesSettingsPanel()
    private val gameProgressPanel = GamesProgressPanel()
    private val statisticsPanel = StatisticsPanel()

    private val gnuBackgammonConsoleTextArea = JTextArea()
    private val programConsoleTextArea = JTextArea()

    val gnuBackgammonTextAreaOutputStream = TextAreaOutputStream(gnuBackgammonConsoleTextArea, 100000)
    val programConsoleTextAreaOutputStream = TextAreaOutputStream(programConsoleTextArea, 100000)

    val progressListeners: List<BackgammonMCTSProgressListener>
        get() = listOf(MainWindowBackgammonMCTSProgressListener(), gameProgressPanel.progressListener, statisticsPanel.progressListener)

    init {
        setUpGuiProperties()
        add(buildMainPanel())

        pack()
        minimumSize = size
    }

    fun addGamesStartedListener(onGamesStarted: ((gamesProperties: GamesProperties) -> Unit)) {
        gameSettingsPanel.addGamesStartedListener(onGamesStarted)
    }

    private fun setUpGuiProperties() {
        programConsoleTextArea.isEditable = false
        gnuBackgammonConsoleTextArea.isEditable = false
    }


    private fun buildMainPanel(): JPanel {
        val layout = FormLayout(
                "f:550dlu:g, 5dlu, f:150dlu:g", // Columns
                "p, 5dlu, p, f:100dlu:g, 5dlu, p, f:200dlu:g, 5dlu, p" // Rows
        )
        val builder = FormBuilder.create().layout(layout).border(Paddings.DLU4)

        builder.add(gameSettingsPanel).at(xy(1, 1, LEFT, DEFAULT))

        builder.add("Games statistics: ").at(xy(3, 3))
        builder.add(statisticsPanel).at(xywh(3, 4, 1, 4))

        builder.add(JLabel("Program output: ")).at(xy(1, 3))
        builder.add(JScrollPane(programConsoleTextArea)).at(xy(1, 4))

        builder.add(JLabel("GNU Backgammon CLI output: ")).at(xy(1, 6))
        builder.add(JScrollPane(gnuBackgammonConsoleTextArea)).at(xy(1, 7))

        builder.add(gameProgressPanel).at(xy(1, 9))

        return builder.build()
    }

    private inner class MainWindowBackgammonMCTSProgressListener : BackgammonMCTSProgressListenerAdapter() {

        override fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int) {
            SwingUtilities.invokeLater {
                gameSettingsPanel.isEnabled = false
            }
        }

        override fun onGamesSequenceEnd(games: Int) {
            SwingUtilities.invokeLater {
                gameSettingsPanel.isEnabled = true
            }
        }

    }
}