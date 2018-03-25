package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import com.jgoodies.forms.builder.FormBuilder
import com.jgoodies.forms.factories.CC.*
import com.jgoodies.forms.factories.Paddings
import com.jgoodies.forms.layout.FormLayout
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GamesProperties
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.DifficultyComboBoxRenderer
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.TextAreaOutputStream
import javax.swing.*


/**
 * Created by kkarolcz on 22.03.2018.
 */
class MainWindow(private val onGamesStarted: ((gamesProperties: GamesProperties) -> Unit)) : JFrame("GNU Backgammon MCTS") {

    private val simulationsLimitTextField = JTextField()
    private val numberOfGamesTextField = JTextField()
    private val difficultyComboBox = JComboBox<GNUBackgammonDifficulty>(GNUBackgammonDifficulty.values())
    private val gnuBackgammonConsoleTextArea = JTextArea()
    private val programConsoleTextArea = JTextArea()

    val gnuBackgammonTextAreaOutputStream = TextAreaOutputStream(gnuBackgammonConsoleTextArea)
    val programConsoleTextAreaOutputStream = TextAreaOutputStream(programConsoleTextArea)

    private val startGameButton = JButton("Play games")

    init {
        setUpGuiProperties()
        addListeners()
        add(buildMainPanel())

        pack()
        minimumSize = size
    }

    private fun setUpGuiProperties() {
        programConsoleTextArea.isEditable = false
        gnuBackgammonConsoleTextArea.isEditable = false
        difficultyComboBox.renderer = DifficultyComboBoxRenderer()
    }

    private fun addListeners() {
        startGameButton.addActionListener {
            val simulationsLimit = simulationsLimitTextField.text.toInt()
            val numberOfGames = numberOfGamesTextField.text.toInt()
            val difficulty = difficultyComboBox.selectedItem as GNUBackgammonDifficulty

            SwingUtilities.invokeLater {
                onGamesStarted(GamesProperties(simulationsLimit, numberOfGames, difficulty))
            }
        }
    }

    private fun buildMainPanel(): JPanel {
        val layout = FormLayout(
                "f:600dlu:g", // Columns
                "p, 5dlu, p, f:100dlu:g, 5dlu, p, f:200dlu:g" // Rows
        )
        val builder = FormBuilder.create().layout(layout).border(Paddings.DLU4)

        builder.add(buildGameSettingsPanel()).at(xy(1, 1, LEFT, DEFAULT))

        builder.add(JLabel("Program output: ")).at(xy(1, 3))
        builder.add(JScrollPane(programConsoleTextArea)).at(xy(1, 4))

        builder.add(JLabel("GNU Backgammon CLI output: ")).at(xy(1, 6))
        builder.add(JScrollPane(gnuBackgammonConsoleTextArea)).at(xy(1, 7))

        return builder.build()
    }

    private fun buildGameSettingsPanel(): JPanel {
        val layout = FormLayout(
                "p, 30dlu, 7dlu, p, 30dlu, 7dlu, p, 70dlu, 12dlu, p, f:p:g", // Columns
                "p" // Rows
        )
        val builder = FormBuilder.create().layout(layout)

        builder.add(JLabel("Number of simulations: ")).at(xy(1, 1))
        builder.add(simulationsLimitTextField).at(xy(2, 1))

        builder.add(JLabel("Number of games: ")).at(xy(4, 1))
        builder.add(numberOfGamesTextField).at(xy(5, 1))

        builder.add(JLabel("Difficulty: ")).at(xy(7, 1))
        builder.add(difficultyComboBox).at(xy(8, 1))

        builder.add(startGameButton).at(xy(10, 1))

        return builder.build()
    }

}