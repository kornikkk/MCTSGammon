package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import com.jgoodies.forms.factories.CC.xy
import com.jgoodies.forms.layout.FormLayout
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress.BackgammonMCTSProgressListener
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress.BackgammonMCTSProgressListenerAdapter
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.SwingUtilities

/**
 * Created by kkarolcz on 25.03.2018.
 */
class GamesProgressPanel : JPanel() {
    private val gamesProgressBar = JProgressBar()
    private val gameRoundLabel = JLabel("0")
    private val simulationsProgressBar = JProgressBar()

    val progressListener: BackgammonMCTSProgressListener = GamesProgressPanelBackgammonMCTSProgressListener()

    init {
        buildPanel()
    }

    private fun buildPanel() {
        layout = FormLayout(
                "p, 60dlu, 7dlu, p, 9dlu, 7dlu, p, 60dlu", // Columns
                "f:p:g" // Rows
        )

        add(JLabel("Games progress: "), xy(1, 1))
        add(gamesProgressBar, xy(2, 1))

        add(JLabel("Current game round: "), xy(4, 1))
        add(gameRoundLabel, xy(5, 1))

        add(JLabel("Simulations progress: "), xy(7, 1))
        add(simulationsProgressBar, xy(8, 1))
    }

    private inner class GamesProgressPanelBackgammonMCTSProgressListener : BackgammonMCTSProgressListenerAdapter() {

        override fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int) {
            SwingUtilities.invokeLater {
                gamesProgressBar.maximum = gamesCount
                gameRoundLabel.text = "0"
                simulationsProgressBar.maximum = simulationsLimit
            }
        }

        override fun onGameRoundStart(round: Int) {
            SwingUtilities.invokeLater {
                gameRoundLabel.text = round.toString()
                simulationsProgressBar.value = 0
            }
        }

        override fun onMonteCarloSimulationStart(simulation: Int) {
            SwingUtilities.invokeLater {
                simulationsProgressBar.value = simulation - 1
            }
        }

        override fun onGameRoundEnd(round: Int, simulationsLimit: Int, move: FullMove) {
            SwingUtilities.invokeLater {
                simulationsProgressBar.value = simulationsLimit
            }
        }

        override fun onGameEnd(game: Int, gameRounds: Int, winner: Player) {
            SwingUtilities.invokeLater {
                gamesProgressBar.value = game
                gameRoundLabel.text = "0"
            }
        }

        override fun onGamesSequenceEnd(games: Int) {
            SwingUtilities.invokeLater {
                gamesProgressBar.value = 0
                gameRoundLabel.text = "0"
                simulationsProgressBar.value = 0
            }
        }
    }
}