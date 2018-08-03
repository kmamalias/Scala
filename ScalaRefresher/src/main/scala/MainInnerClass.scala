import scalaInnerClass.{Graph, Graphv2}

object MainInnerClass {
  def main(args: Array[String]): Unit = {
    val graph1: Graph = new Graph
    val node1: graph1.Node = graph1.newNode
    val node2: graph1.Node = graph1.newNode
    val node3: graph1.Node = graph1.newNode
    node1.connectTo(node2)
    node3.connectTo(node1)

    //node1,2,3 are declared as type Graph.newNode
    //If there are two graphs, this implementation of Graph will not work, see Graphv2 implementation below

    val graph2: Graphv2 = new Graphv2
    val node4: graph2.Node = graph2.newNode
    val node5: graph2.Node = graph2.newNode
    node4.connectTo(node5)
    val graph3: Graphv2 = new Graphv2
    val node6: graph3.Node = graph3.newNode
    node6.connectTo(node4)
  }
}
