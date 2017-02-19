package wordcloud.chunk

import java.io.PrintWriter
import scala.io.Source

import org.json4s.DefaultFormats
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods._

import wordcloud.utils.{dotProduct, splitAt}
import wordcloud.utils.corpus._

abstract class Chunker {

  private val threshold = 0.5F
  private val alpha = 2

  protected val featureFuncs: List[ContextInfo => Double]

  protected var weights: Array[Double]  // initially all 1, same size as featureFuncs List

  private def getFeatureVec(feat: ContextInfo): Array[Double] = featureFuncs.map(_(feat)).toArray

  def train(corpus: CorpusReader): Unit = {
    corpus.readAnnotatedSentences().foreach {
      sentence =>
        (List(BOS_TOKEN) ++ sentence ++ List(EOS_TOKEN)).sliding(3, 1).foreach {
          case List(AnnotatedToken(prevWord, prevTag, prevC),
                    AnnotatedToken(curWord, curTag, curC),
                    AnnotatedToken(precWord, precTag, precC)) =>
            val fVec = getFeatureVec(ContextInfo(prevWord, prevTag, curWord, curTag, precWord, precTag))
            val predicted = if (dotProduct(fVec, weights) > threshold) IS_CHUNK else NO_CHUNK
            if (predicted != curC) {
              if (curC) weights = weights.zip(fVec).map {case (a, b) => if (b == 1.0) a*alpha else a}  // is part of a chunk
              else weights = weights.zip(fVec).map {case (a, b) => if (b == 1.0) a/alpha else a}  // is not part of a chunk
            }
        }
    }
  }

  def tagSent(sent: Iterable[String], posTags: Iterable[String]) = new Iterator[ChunkVal] {

      val wordIter = Iterator(BOS) ++ sent ++ Iterator(EOS)
      val posIter = Iterator(BOS_TAG) ++ posTags ++ Iterator(EOS_TAG)
      val trigramIter = wordIter.zip(posIter).sliding(3,1)

      def hasNext = trigramIter.hasNext

      def next() = trigramIter.next match {
        case List((prevWord, prevTag), (curWord, curTag), (precWord, precTag)) =>
          val featVec = getFeatureVec(ContextInfo(prevWord, prevTag, curWord, curTag, precWord, precTag))
          if (dotProduct(featVec, weights) > threshold) IS_CHUNK
          else NO_CHUNK
      }
  }

  def extractChunksFromSent(sent: Iterable[String], tags: Iterable[String]): Iterator[String] =
    splitAt(sent.iterator.zip(tagSent(sent, tags)),
            (x: (String, ChunkVal)) => !x._2)
      .map(_.map(_._1))
      .map(_.mkString(" "))

  def getAccuracy(corpus: CorpusReader): Double = {
    var correct = 0.0
    var total = 0.0
    corpus.readAnnotatedSentences().foreach(
      sentence =>
        (List(BOS_TOKEN) ++ sentence ++ List(EOS_TOKEN)).sliding(3, 1).foreach {
          case List(AnnotatedToken(prevWord, prevTag, prevC),
          AnnotatedToken(curWord, curTag, curC),
          AnnotatedToken(precWord, precTag, precC)) =>
            val fVec = getFeatureVec(ContextInfo(prevWord, prevTag, curWord, curTag, precWord, precTag))
            val predicted = if (dotProduct(fVec, weights) > threshold) IS_CHUNK else NO_CHUNK
            if (predicted == curC) correct += 1
            total += 1
        }
    )
    correct/total
  }

  def save(filename: String): Unit = {
    val jString = Json(DefaultFormats).writePretty(Map[String, Any]("weights" -> weights))
    new PrintWriter(filename) {write(jString); close()}
  }

}

object Chunker {
  def loadWeightsFromResource(res: Source): Array[Double] = {
    implicit val formats = DefaultFormats
    val json = parse(res.mkString)
    (json \ "weights").extract[Array[Double]]
  }
  def loadWeights(chunkerFile: String) = loadWeightsFromResource(Source.fromFile(chunkerFile))
}