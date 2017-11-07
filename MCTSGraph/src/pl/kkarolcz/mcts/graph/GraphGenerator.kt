package pl.kkarolcz.mcts.graph

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.view.mxPerimeter
import com.mxgraph.view.mxStylesheet
import org.jgrapht.DirectedGraph
import org.jgrapht.ext.DOTExporter
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.ext.StringComponentNameProvider
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode
import java.awt.Dimension
import java.io.File
import java.util.*
import javax.swing.JFrame
import javax.swing.WindowConstants


/**
 * Created by kkarolcz on 25.10.2017.
 */
object GraphGenerator {

    fun generateGraph(rootNode: MCTSNode<out MCTSMove>): DirectedGraph<MCTSNode<out MCTSMove>, DefaultEdge> {
        val graph = DefaultDirectedGraph<MCTSNode<out MCTSMove>, DefaultEdge>(DefaultEdge::class.java)
        addNodeToGraph(graph, rootNode)
        return graph
    }

    fun addNodeToGraph(graph: DirectedGraph<MCTSNode<out MCTSMove>, DefaultEdge>, node: MCTSNode<out MCTSMove>) {
        graph.addVertex(node)
        node.children.forEach { child ->
            graph.addVertex(child)
            graph.addEdge(node, child)
            addNodeToGraph(graph, child)
        }
    }

    fun <V, E> drawGraph(graph: DirectedGraph<V, E>) {
        val jGraph = JGraphXAdapter<V, E>(graph)

        val comp = mxGraphComponent(jGraph)

        val layout = mxHierarchicalLayout(jGraph)
        layout.isDisableEdgeStyle = true
        layout.execute(jGraph.defaultParent)


        val stylesheet = mxStylesheet()

        val vertexStyle = HashMap<String, Any>()
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        vertexStyle.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        vertexStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#FF0000");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#774400");

        stylesheet.defaultVertexStyle = vertexStyle

        val edgeStyle = HashMap<String, Any>()
        edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CLOUD)
        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND)
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000")
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000")
        edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff")

        jGraph.stylesheet = stylesheet

        jGraph.refresh()

        val frame = JFrame()
        frame.preferredSize = Dimension(500, 500)
        frame.pack()
        frame.contentPane.add(comp)
        frame.isVisible = true
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    }

    fun generateGraph() {
        val graph = DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        graph.addVertex("TEST")
        graph.addVertex("TEST2")
        graph.addVertex("TEST3")
        graph.addEdge("TEST", "TEST2")

        val jGraph = JGraphXAdapter<String, DefaultEdge>(graph)

        val comp = mxGraphComponent(jGraph)

        val layout = mxHierarchicalLayout(jGraph)
        layout.isDisableEdgeStyle = true
        layout.execute(jGraph.defaultParent)


        val stylesheet = mxStylesheet()

        val vertexStyle = HashMap<String, Any>()
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        vertexStyle.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        vertexStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#FF0000");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#774400");

        stylesheet.defaultVertexStyle = vertexStyle

        val edgeStyle = HashMap<String, Any>()
        edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CLOUD)
        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND)
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000")
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000")
        edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff")

        jGraph.stylesheet = stylesheet

        jGraph.refresh()

        val frame = JFrame()
        frame.preferredSize = Dimension(500, 500)
        frame.pack()
        frame.contentPane.add(comp)
        frame.isVisible = true
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        System.out.println(graph.toString())
        val exporter = DOTExporter(StringComponentNameProvider<String>(), null,
                StringComponentNameProvider<DefaultEdge>())
        val file = File("C:\\Users\\korni\\Desktop\\testgraph.dot")
        if (!file.exists()) {
            file.createNewFile()
        }
        exporter.exportGraph(graph, file)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        generateGraph()
    }
}