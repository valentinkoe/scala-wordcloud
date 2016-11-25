package pos

import java.io.FileInputStream
import java.io.PrintWriter

import scala.io.Source
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

abstract class PosTagger () {

  var seenTags : Map[String, Set[String]] = Map() // word -> possible tags
  var weights : Map[String, Array[Int]] = Map() // tag -> weight vector

  def train(trainFile: String): Unit = {
    val trainSamples = Source.fromFile(trainFile).getLines.map(_.split("\t"))  // iterator of tuples (word, tag)

    var prevWord = ""
    var prevTag = ""
    var word = ""
    var tag = ""

    for (tupleList <- trainSamples.sliding(2,1)) {  // TODO: input file must also start with empty line
      if (!tupleList(1).sameElements(Array(""))) {  // if used to work like java's continue - end of sentence {
        if (tupleList(0).sameElements(Array(""))) {
          prevWord = ""
          prevTag = ""
        }
        else {
          prevWord = tupleList(0)(0)
          prevTag = tupleList(0)(1)
        }
        word = tupleList(1)(0)
        tag = tupleList(1)(1)

        // add seen tag to seenTags for this word
        var tagsForWord = seenTags.getOrElse(word, Set())
        tagsForWord += tag
        seenTags += (word -> tagsForWord)

        // generate feature vector and add it up to the vector stored for this tag
        // TODO: the next lines could surely be done in a fancy functional way
        var fVec = getFeatureVec(prevWord, prevTag, word)
        if (weights.contains(tag)) {
          val prevWeights = weights(tag)
          weights += (tag -> prevWeights.zip(fVec).map { case (x, y) => x + y } ) // element-wise addition of vectors
        }
        else {
          weights += (tag -> fVec)
        }
      }
    }
    // TODO: normalize weights - dividing by the number of occurrences of the tag might be a good idea...
    // values of weights are floats then --> weights = Map[String, Array[Float]]
  }

  def getFeatureVec(prevWord : String, prevTag : String, word : String) : Array[Int]

  def load(loadFile : String): Unit = ???  // TODO
    // val stream = new FileInputStream(loadFile)
    // val json = try {  parse(stream) } finally { stream.close() }
    //or
    // val json = parse(Source.fromFile(loadFile).getLines.mkString)

  def save(saveFile : String): Unit = {
    val json = ("weights" -> this.weights.mapValues(_.toList)) ~ ("seenTags" -> this.seenTags)
    new PrintWriter(saveFile) { write(pretty(render(json))); close() }
  }

  def tag(words : Array[String]): Array[String] = ???  // TODO

}

trait FeatureExtractor {
  def getFeatureVec(prevWord : String, prevTag : String, word : String) : Array[Int]
}
