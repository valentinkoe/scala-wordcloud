package wordcloud.pos

import java.io.PrintWriter

import scala.collection.mutable.ListBuffer
import org.json4s.jackson.Json
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import wordcloud.utils.POSAnnotatedToken
import wordcloud.utils.corpus.BOS_TOKEN
import wordcloud.utils.corpus.CorpusReader

import scala.io.Source


abstract class PosTagger {

  var tagCounts: Map[String, Int] = Map().withDefaultValue(0)
  // tag -> count
  var seenTags: Map[String, Set[String]] = Map().withDefaultValue(Set())
  // word -> possible tags
  var weights: Map[String, Array[Float]] = Map() // tag -> weight vector

  val defaultTag: String

  val featureFuncs: List[ContextInfo => Float]

  def getFeatureVec(feat: ContextInfo, train: Boolean) : Array[Float] = {
    var featFuncsApplied = featureFuncs.map(_(feat))
    if (train) featFuncsApplied = featFuncsApplied.map(x => if (x == 0F) -1F else 1F)
    featFuncsApplied.toArray
  }

  def train(corpus: CorpusReader): Unit = {

    for (sentence <- corpus.readPOSAnnotatedSentences()) {
      var prevToken = BOS_TOKEN
      for (token <- sentence) {
        seenTags += token.word -> (seenTags(token.word) + token.pos)
        tagCounts += token.pos -> (tagCounts(token.pos) + 1)
        val fVec = getFeatureVec(ContextInfo(prevToken, token.word), train = true)
        if (weights.contains(token.pos)) {
          weights += token.pos -> weights(token.pos).zip(fVec).map { case (x, y) => x + y }
        }
        else {
          weights += token.pos -> fVec
        }
        prevToken = token
      }
    }
    // normalize weights, i.e. for tag in weights by dividing the corresponding weights
    // by the tags absolute frequency element wise
    for (tag <- weights.keys) {
      weights += tag -> weights(tag).zip(Stream.continually(tagCounts(tag))).map { case (x, y) => x / y }
    }
  }

  def tagSent(words : List[String]): List[String] = tagSentIter(words.iterator).toList

  def tagSentIter(words: Iterator[String]): Iterator[String] = new Iterator[String] {
      var prevToken = BOS_TOKEN

      def hasNext = words.hasNext

      def next = {
        val curWord = words.next()
        val possibleTags = seenTags.getOrElse(curWord, tagCounts.keys)
        var bestTag = defaultTag
        if (possibleTags.size == 1) {
          bestTag = possibleTags.head
        }
        else if (possibleTags.nonEmpty) {
          val featVec = getFeatureVec(ContextInfo(prevToken, curWord), train=false)
          val sims = possibleTags.map(x => (x, featVec.zip(weights(x)).map({ case (x, y) => x + y }).sum))
          bestTag = sims.reduce((x, y) => if (x._2 > y._2) x else y)._1
        }
        prevToken = POSAnnotatedToken(curWord, bestTag)
        bestTag
      }
    }

  def save(filename: String): Unit = {
    val jString = Json(DefaultFormats).writePretty(Map[String, Any]("weights" -> weights, "seenTags" -> seenTags))
    new PrintWriter(filename) {write(jString); close()}
  }

  def getAccuracy(corpus: CorpusReader): Double = {
    var correct = 0.0
    var total = 0.0
    val predictedTagSeqs = corpus.readSentences().map(tagSent)
    val goldTagSeqs = corpus.readPOSAnnotatedSentences().map(x => x.map(_.pos))
    for ((predSeq, goldSeq) <- predictedTagSeqs.zip(goldTagSeqs)) {
      for ((p, g) <- predSeq.zip(goldSeq)) {
        if (p == g) correct += 1
        total += 1
      }
    }
    correct/total
  }
}

object PosTagger {

  def loadTaggerData(resource: String) = {
    implicit val formats = DefaultFormats
    val json = parse(Source.fromResource(resource).mkString)
    val loadedWeights = (json \ "weights").extract[Map[String, Array[Float]]]
    val loadedSeenTags = (json \ "seenTags").extract[Map[String, Set[String]]]
    (loadedWeights, loadedSeenTags)
  }

}