package wordcloud.pos

import java.io.PrintWriter
import scala.collection.mutable.ListBuffer
import org.json4s.jackson.Json
import org.json4s.DefaultFormats

import wordcloud.utils.corpus.CorpusReader


abstract class PosTagger {

  var tagCounts: Map[String, Int] = Map().withDefaultValue(0)
  // tag -> count
  var seenTags: Map[String, Set[String]] = Map().withDefaultValue(Set())
  // word -> possible tags
  var weights: Map[String, Array[Float]] = Map() // tag -> weight vector

  val defaultTag: String

  def getFeatureVec(feat: ContextInfo, train: Boolean): Array[Float]

  def train(corpus: CorpusReader): Unit = {

    for (sentence <- corpus.readSentences()) {
      var prevToken = BosToken
      for (token <- sentence) {
        seenTags += token.form -> (seenTags(token.form) + token.pos)
        tagCounts += token.pos -> (tagCounts(token.pos) + 1)
        val fVec = getFeatureVec(ContextInfo(prevToken.form, prevToken.pos, token.form), train = true)
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

  def tag(words : List[String]): List[String] = {
    var tags = new ListBuffer[String]
    var prevWord = ""
    var prevTag = BOS
    for (word <- words) {
      val possibleTags = seenTags.getOrElse(word, tagCounts.keys)
      var bestTag = defaultTag
      if (possibleTags.size == 1) {
        bestTag = possibleTags.head
      }
      else {
        val featVec = getFeatureVec(ContextInfo(prevWord, prevTag, word), train=false)
        var maxSim = Float.NegativeInfinity
        for (pTag <- possibleTags) {
          val sim = featVec.zip(weights(pTag)).map{case (x, y) => x+y}.sum
          if (sim > maxSim) {
            maxSim = sim
            bestTag = pTag
          }
        }
      }
      prevWord = word
      prevTag = bestTag
      tags += bestTag
    }
    tags.toList
  }

  def tagIter(words: Iterator[String]): Iterator[String] =
    new Iterator[String] {
      var prevWord = ""
      var prevTag = BOS

      def hasNext = words.hasNext

      def next = {
        val curWord = words.next()
        val possibleTags = seenTags.getOrElse(curWord, tagCounts.keys)
        var bestTag = defaultTag
        if (possibleTags.size == 1) {
          bestTag = possibleTags.head
        }
        else {
          val featVec = getFeatureVec(ContextInfo(prevWord, prevTag, curWord), train = false)
          var maxSim = Float.NegativeInfinity
          for (pTag <- possibleTags) {
            val sim = featVec.zip(weights(pTag)).map { case (x, y) => x + y }.sum
            if (sim > maxSim) {
              maxSim = sim
              bestTag = pTag
            }
          }
        }
        prevWord = curWord
        prevTag = bestTag
        bestTag
      }
    }

  def save(filename: String): Unit = {
    val jString = Json(DefaultFormats).write(Map[String, Any]("weights" -> weights, "seenTags" -> seenTags))
    new PrintWriter(filename) {write(jString); close}
  }
}
