package scalaInnerClass

class Graphv2 {
  class Node{
    var connectedNodes: List[Graphv2#Node] = Nil //if we want to connect nodes of different Graph types

    def connectTo(node: Graphv2#Node): Unit ={  //if we want to connect nodes of different Graph types
      if(connectedNodes.find(node.equals).isEmpty){
        connectedNodes = node :: connectedNodes
      }
    }
  }

  var nodes: List[Graphv2#Node] = Nil //if we want to connect nodes of different graph
  def newNode: Node ={
    val res = new Node
    nodes = res :: nodes
    res
  }
}
