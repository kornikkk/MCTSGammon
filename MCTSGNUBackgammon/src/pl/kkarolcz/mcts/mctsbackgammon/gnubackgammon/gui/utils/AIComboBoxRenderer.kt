package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils

import pl.kkarolcz.mcts.mctsbackgammon.game.ai.BackgammonAIType
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

/**
 * Created by kkarolcz on 23.03.2018.
 */
class AIComboBoxRenderer : DefaultListCellRenderer() {

    override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        text = (value as BackgammonAIType).aiName
        return this
    }

}