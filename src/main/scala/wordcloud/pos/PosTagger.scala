package wordcloud.pos

import java.io.PrintWriter

import scala.io.Source
import org.json4s.jackson.Json
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import wordcloud.utils.{dotProduct, EqualPair}
import wordcloud.utils.corpus._


abstract class PosTagger {

  private var tagCounts: Map[String, Int] = Map().withDefaultValue(0)
  // count how often a tag was observed during trining
  protected var seenTags: Map[String, Set[String]] = Map().withDefaultValue(Set())
  // store information about which word was seen
  protected var weights: Map[String, Array[Double]] = Map() // tag -> weight vector

  protected val defaultTag: String  // the default tag to assign if no other tag could be predicted

  protected val featureFuncs: List[ContextInfo => Double]  // feature extracting functions

  private def getFeatureVec(feat: ContextInfo, train: Boolean): Array[Double] = {
    var featFuncsApplied = featureFuncs.map(_(feat))
    if (train) featFuncsApplied = featFuncsApplied.map(x => if (x == 0D) -1D else 1D)
    featFuncsApplied.toArray
  }

  def train(corpus: CorpusReader): Unit = {

    corpus.readAnnotatedSentences().foreach(
      sentence => {
        var prevToken = BOS_TOKEN
        for (token <- sentence) {
          seenTags += token.word -> (seenTags(token.word) + token.pos)
          tagCounts += token.pos -> (tagCounts(token.pos) + 1)
          val fVec = getFeatureVec(ContextInfo(prevToken.word, prevToken.pos, token.word), train=true)
          if (weights.contains(token.pos)) {
            weights += token.pos -> weights(token.pos).zip(fVec).map { case (x, y) => x+y }
          }
          else {
            weights += token.pos -> fVec
          }
          prevToken = token
        }
      }
    )

    // normalize weights, i.e. for tag in weights by element wise division
    // of the corresponding weights by the tags absolute frequency
    // note: this is why "incremental training" is not really supported
    weights.keys.foreach(tag =>
      weights += tag -> weights(tag).zip(Stream.continually(tagCounts(tag))).map { case (x, y) => x / y })
  }

  def tagSent(words: Iterable[String]) = new Iterator[String] {

      var prevTag = BOS_TAG
      val bigramIter = (Iterator(BOS) ++ words).sliding(2,1)

      def hasNext = bigramIter.hasNext

      def next = bigramIter.next match {
        case List(prevWord, curWord) =>
          val possibleTags = seenTags.getOrElse(curWord, tagCounts.keys)
          var bestTag = defaultTag
          if (possibleTags.size == 1) {
            bestTag = possibleTags.head
          }
          else if (possibleTags.nonEmpty) {
            val featVec = getFeatureVec(ContextInfo(prevWord, prevTag, curWord), train=false)
            val sims = possibleTags.map(x => (x, dotProduct(featVec, weights(x))))
            bestTag = sims.reduce((x, y) => if (x._2 > y._2) x else y)._1
          }
          prevTag = bestTag
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
    val goldTagSeqs = corpus.readAnnotatedSentences().map(x => x.map(_.pos))
    predictedTagSeqs.zip(goldTagSeqs).foreach {
      case (predSeq, goldSeq) =>
        predSeq.toList.zip(goldSeq).foreach {
          case EqualPair(_) => correct += 1; total += 1
          case _ => total += 1
        }
    }
    correct/total
  }
}

object PosTagger {
  def loadTaggerDataFromResource(res: Source): (Map[String, Array[Double]], Map[String, Set[String]]) = {
    implicit val formats = DefaultFormats
    val json = parse(res.mkString)
    val loadedWeights = (json \ "weights").extract[Map[String, Array[Double]]]
    val loadedSeenTags = (json \ "seenTags").extract[Map[String, Set[String]]]
    (loadedWeights, loadedSeenTags)
  }
  def loadTaggerData(filename: String) = loadTaggerDataFromResource(Source.fromFile(filename))
}