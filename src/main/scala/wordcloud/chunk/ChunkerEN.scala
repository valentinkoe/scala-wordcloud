package wordcloud.chunk

import wordcloud.utils.corpus.{BOS_TAG, EOS_TAG}

import scala.io.Source

class ChunkerEN extends Chunker {

  protected override val featureFuncs: List[(ContextInfo) => Double] = List(
    x => if (x.prevTag == BOS_TAG) 1 else 0,
    x => if (x.prevTag == "TO") 1 else 0,
    x => if (x.prevTag.startsWith("NN")) 1 else 0,
    x => if (x.prevTag == "SYM") 1 else 0,
    x => if (x.prevTag == "DET") 1 else 0,
    x => if (x.prevWord.length <= 3) 1 else 0,
    x => if (x.prevWord.length > 3 && x.word.length < 6) 1 else 0,
    x => if (x.prevWord.length >= 6) 1 else 0,
    x => if (x.tag.startsWith("NN")) 1 else 0,
    x => if (x.tag == "TO") 1 else 0,
    x => if (x.tag == "SYM") 1 else 0,
    x => if (x.tag == "FW") 1 else 0,
    x => if (x.word.length <= 3) 1 else 0,
    x => if (x.word.length > 3 && x.word.length < 6) 1 else 0,
    x => if (x.word.length >= 6) 1 else 0,
    x => if (x.precTag == EOS_TAG) 1 else 0,
    x => if (x.precTag.startsWith("NN")) 1 else 0,
    x => if (x.precTag == "TO") 1 else 0,
    x => if (x.precTag == "SYM") 1 else 0,
    x => if (x.precWord.length <= 3) 1 else 0,
    x => if (x.precWord.length > 3 && x.word.length < 6) 1 else 0,
    x => if (x.precWord.length >= 6) 1 else 0
  )

  protected override var weights: Array[Double] = Array.fill(featureFuncs.size)(1.0D)

}

object ChunkerEN {
  /** loads a english chunker saved in json format
    * unfortunately the feature extracting rules are not easily serializable
    * so we need to define the load function for each subclass separately
    */
  def load(taggerFile: String): Chunker = new ChunkerEN {
      weights = Chunker.loadWeights(taggerFile)
  }
  /** loads the default english chunker */
  def loadDefault = new ChunkerEN {
    weights = Chunker.loadWeightsFromResource(Source.fromResource("trained_classifiers/chunker_en.json"))
  }
}