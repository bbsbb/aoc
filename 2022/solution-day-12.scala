import scala.annotation.tailrec
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue
import scala.io.Source

def parseNode(grid: List[String], i: Int, j: Int): (String, Int) = {
  grid(i)(j) match {
    case 'S' => ("START", 97)
    case 'E' => ("END", 122)
    case c => (s"${i}-${j}", c.toInt)
  }
}

def neighbours (grid: List[String], i: Int, j: Int): (String, List[String]) = {
  val (k, v) = parseNode(grid, i, j)
  (k, List((i-1, j), (i+1, j), (i, j-1), (i, j + 1)).foldLeft(List[String]()){
    case (acc, (ci, cj)) => {
      ci >= 0 && cj >= 0 && ci < grid.length && cj < grid.head.length && v + 1 >= (parseNode(grid, ci, cj)._2) match {
        case true => acc :+ parseNode(grid, ci, cj)._1
        case _ => acc
      }
    }
  })
}

def inputStarts (filepath: String): List[String] = {
  val input = Source.fromFile(filepath).getLines.map(l => l).toList
  val starts = ListBuffer[String]()
  input.zipWithIndex.foreach { (l, i) =>
    l.zipWithIndex.map {
      (c, j) => {
        input(i)(j) match {
          case 'a' => starts += s"${i}-${j}"
          case _ => ()
        }
      }
    }
  }
  starts.toList
}

def inputGraph (filepath: String): HashMap[String, List[String]] = {
  val input = Source.fromFile(filepath).getLines.map(l => l).toList
  val graph: HashMap[String, List[String]] = HashMap()

  input.zipWithIndex.foreach { (l, i) =>
    l.zipWithIndex.map {
      (c, j) => {
        graph.addOne(neighbours(input, i, j))
      }
    }
  }
  graph
}

def path (graph: HashMap[String, List[String]], start: String): List[String] = {
  val q = Queue(start)
  val backtrack = HashMap[String, String]()

  while(q.nonEmpty) {
    val current = q.dequeue()
    if (current == "END") {
      val path = ListBuffer[String](current)
      while (path(path.length - 1) != start) {
        backtrack.get(path(path.length - 1)) match {
          case Some(c) => path += c
          case _ => ()
        }
      }
      return path.toList
    }


    val neighbours = graph.get(current)
    for (n <- neighbours;
      v <- n) {
      if (!backtrack.contains(v)) {
        backtrack.addOne(v -> current)
        q.enqueue(v)
      }
    }
  }
  return List[String]()
}

@main def solution = {
  val graph = inputGraph("input-day-12.txt")
  val p1 = path(graph, "START")
  // .420
  println(p1.length - 1)


  val starts = inputStarts("input-day-12.txt")
  // .414
  println(starts.map(s => path(graph, s).length - 1).filter(_ > 0).min)
}
