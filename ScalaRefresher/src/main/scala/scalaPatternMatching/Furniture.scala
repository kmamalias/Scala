package scalaPatternMatching

sealed abstract class Furniture

case class Couch() extends Furniture

case class Chair() extends Furniture
