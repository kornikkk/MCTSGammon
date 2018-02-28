package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server

/**
 * Created by kkarolcz on 29.08.2017.
 */
interface GNUBackgammonReceiver {
    fun onBoardInfoReceived(boardInfo: BoardInfo, callback: (String) -> Unit)

}
