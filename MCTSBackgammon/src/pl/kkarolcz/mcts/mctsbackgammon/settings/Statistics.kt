package pl.kkarolcz.mcts.mctsbackgammon.settings

import java.io.File

/**
 * Created by kkarolcz on 14.03.2018.
 */
object Statistics {
    private var logFile: File? = null

    private lateinit var _currentGame: Game
    val currentGame get() = _currentGame

    private val _games = mutableListOf<Game>()
    val games: List<Game> get() = _games

    fun setLogFile(logFile: File) {
        if (logFile.exists()) {
            logFile.createNewFile()
        }
        Statistics.logFile = logFile
    }

    fun newGame() {
        _currentGame = Game()
        _games.add(_currentGame)
        _currentGame.newRound()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        games.forEachIndexed { index, game ->
            builder.append("Game $index:\n$game")
        }
        return builder.toString()
    }

    fun logRound() {
        System.out.println(currentGame.currentRound.toString())
        if (logFile != null) {
            logFile!!.appendText(currentGame.currentRound.toString())
        }
    }


    class Game {
        private lateinit var _currentRound: Round
        val currentRound get() = _currentRound

        private val _rounds = mutableListOf<Round>()
        val rounds: List<Round> get() = _rounds

        fun newRound() {
            _currentRound = Round()
            _rounds.add(_currentRound)
        }

        override fun toString(): String {
            val builder = StringBuilder()
            rounds.forEachIndexed { index, round ->
                builder.append("Round $index:\n$round")
            }
            return builder.toString()
        }

        class Round {
            private var _nonDoublingSearches: Long = 0
            val nonDoublingSearches get() = _nonDoublingSearches

            private var _doublingSearches: Long = 0
            val doublingSearches get() = _doublingSearches

            fun incNonDoublingSearches() {
                _nonDoublingSearches += 1
            }

            fun incDoublingSearches() {
                _doublingSearches += 1
            }

            override fun toString() =
                    "Non doubling searches: $nonDoublingSearches\n" +
                            "Doubling searches:     $doublingSearches"

        }
    }
}