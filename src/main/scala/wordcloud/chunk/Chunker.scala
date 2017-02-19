package wordcloud.chunk

import wordcloud.utils.dotProduct
import wordcloud.utils.EqualPair
import wordcloud.utils.corpus._

abstract class Chunker {

  val threshold: Float
  val alpha: Float

  val featureFuncs: List[ContextInfo => Float]

  var weights: Array[Float]  // initially all 1, same size as featureFuncs List

  def getFeatureVec(feat: ContextInfo): Array[Float] = featureFuncs.map(_(feat)).toArray

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
              if (curC) weights = weights.zip(fVec).map {case (a, b) => if (b == 1.0) a*alpha else a}
              else weights = weights.zip(fVec).map {case (a, b) => if (b == 1.0) a/alpha else a}
            }
        }
    }
  }

  def tagSent(sent: List[String], posTags: List[String]) = tagSentIter(sent.iterator, posTags.iterator).toList

  def tagSentIter(sent: Iterator[String], posTags: Iterator[String]) = new Iterator[ChunkVal] {

      val wordIter = Iterator(BOS) ++ sent ++ Iterator(EOS)
      val posIter = Iterator(BOS_TAG) ++ posTags ++ Iterator(EOS_TAG)
      val trigramIter = wordIter.zip(posIter).sliding(3,1)

      def hasNext = trigramIter.hasNext

      def next() = trigramIter.next match {
        case List((prevWord, prevTag), (curWord, curTag), (precWord, precTag)) =>
          val cInfo = ContextInfo(prevWord, prevTag, curWord, curTag, precWord, precTag)
          val featVec = getFeatureVec(cInfo)
          if (dotProduct(featVec, weights) > threshold) IS_CHUNK
          NO_CHUNK
      }
  }

  def getAccuracy(corpus: CorpusReader): Double = {
    0.0 // TODO
//    var correct = 0.0
//    var total = 0.0
//    val predictedTagSeqs = corpus.readSentences().map(tagSent)
//    val goldTagSeqs = corpus.readAnnotatedSentences().map(x => x.map(_.pos))
//    predictedTagSeqs.zip(goldTagSeqs).foreach {
//      case (predSeq, goldSeq) =>
//        predSeq.zip(goldSeq).foreach {
//          case EqualPair(_) => correct += 1; total += 1
//          case _ => total += 1
//        }
//    }
//    correct/total
  }

  def save(filename: String): Unit = {
    // TODO
  }

}

object Chunker {
  //def load(resource: String): Chunker = {
  //  new Chunker // TODO
  //}
}
