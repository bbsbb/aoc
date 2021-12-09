import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source

def inputMap (filepath: String): List[List[Int]] = {
  val input = Source.fromFile(filepath).getLines.map(l => l.split("").map(_.toInt).toList).toList
  val extendedInput = input.map(r => (-1::r).:+(-1))
  val guardRow = List.fill(extendedInput.head.length)(-1)
  (guardRow::extendedInput):+guardRow
}


def neighbours(matrix: List[List[Int]], i: Int, j:Int): List[(Int, Int, Int)] = {
  List((i-1, j, matrix(i-1)(j)), (i+1, j, matrix(i+1)(j)), (i, j-1, matrix(i)(j-1)), (i,j+1, matrix(i)(j+1))).filter{case (_,_,v) => v != -1}
}

def findLowPoints(matrix: List[List[Int]]): List[(Int, Int, Int)] = {
  val lowPoints = ListBuffer[(Int, Int, Int)]()
  for (
    i <- 1 until matrix.length -1;
    j <- 1 until matrix.head.length - 1
  ) {
    val n = neighbours(matrix, i, j)
    if (n.forall{case (_,_,v) => v > matrix(i)(j)}) {
      lowPoints += ((i,j,matrix(i)(j)))
    }
  }
  lowPoints.toList
}

@tailrec
def buildBasin(matrix: List[List[Int]], visited: Set[(Int, Int, Int)], toVisit: Set[(Int, Int, Int)], basin: Set[(Int, Int, Int)]): Set[(Int, Int, Int)] = {
  if (toVisit.isEmpty) {
    return basin
  }
  val (i,j,v) = toVisit.head
  val flowing = neighbours(matrix, i, j).filter{case (_,_,nv) => nv > v && nv != 9}
  buildBasin(matrix, visited + ((i,j,v)), toVisit.tail ++ flowing, basin + ((i,j,v)))
}


@main def solution = {
  val matrix = inputMap("input-day-09.txt")

  //1
  val lowPoints = findLowPoints(matrix)
  println(lowPoints.map{case (_,_,v) => v}.sum + lowPoints.length)

  //2
   val lowPointCoordinates = findLowPoints(matrix)
  val basins = lowPointCoordinates.map(p => buildBasin(matrix, Set(), Set(p), Set()))
    .map(_.size)
    .sortWith(_ > _)
    .take(3)
    .fold(1)(_*_)
  println(basins)
}
