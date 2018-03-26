package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import com.jgoodies.forms.factories.CC.xy
import com.jgoodies.forms.layout.FormLayout
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.GamesProperties
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.DifficultyComboBoxRenderer
import javax.swing.*

/**
 * Created by kkarolcz on 25.03.2018.
 */
class GamesSettingsPanel : JPanel() {
    private val simulationsLimitTextField = JTextField()
    private val numberOfGamesTextField = JTextField()
    private val difficultyComboBox = JComboBox<GNUBackgammonDifficulty>(GNUBackgammonDifficulty.values())

    private val startGameButton = JButton("Play games")

    init {
        difficultyComboBox.renderer = DifficultyComboBoxRenderer()
        buildPanel()
    }

    fun addGamesStartedListener(onGamesStarted: (gameProperties: GamesProperties) -> Unit) {
        startGameButton.addActionListener {
            val simulationsLimit = simulationsLimitTextField.text.toInt()
            val numberOfGames = numberOfGamesTextField.text.toInt()
            val difficulty = difficultyComboBox.selectedItem as GNUBackgammonDifficulty

            onGamesStarted(GamesProperties(simulationsLimit, numberOfGames, difficulty))
        }
    }

    override fun setEnabled(enabled: Boolean) {
        startGameButton.isEnabled = enabled
    }

    private fun buildPanel() {
        layout = FormLayout(
                "p, 30dlu, 7dlu, p, 30dlu, 7dlu, p, 70dlu, 12dlu, p, f:p:g", // Columns
                "p" // Rows
        )

        add(JLabel("Number of simulations: "), xy(1, 1))
        add(simulationsLimitTextField, xy(2, 1))

        add(JLabel("Number of games: "), xy(4, 1))
        add(numberOfGamesTextField, xy(5, 1))

        add(JLabel("Difficulty: "), xy(7, 1))
        add(difficultyComboBox, xy(8, 1))

        add(startGameButton, xy(10, 1))
    }
}