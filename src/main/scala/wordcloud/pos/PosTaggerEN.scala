package wordcloud.pos

import wordcloud.utils.corpus.BOS_TAG

import scala.io.Source

class PosTaggerEN extends PosTagger {

  protected override val defaultTag: String = "NN"

  protected override val featureFuncs: List[ContextInfo => Double] = List(
    x => if (x.prevTag == BOS_TAG) 1 else 0,  // beginning of sentence
    x => if (x.prevWord.length <= 3) 1 else 0,
    x => if (x.prevWord.length > 3 &&  x.prevWord.length < 6) 1 else 0,
    x => if (x.prevWord.length >= 6) 1 else 0,
    x => if (x.prevTag == "DT") 1 else 0,
    x => if (x.prevTag == "TO") 1 else 0,
    x => if (x.prevTag.startsWith("N")) 1 else 0,
    x => if (x.prevTag.startsWith("J")) 1 else 0,
    x => if (x.prevTag.startsWith("V")) 1 else 0,
    x => if (x.word.length <= 3) 1 else 0,
    x => if (x.word.length == 4) 1 else 0,
    x => if (x.word.length == 5) 1 else 0,
    x => if (x.word.length == 6) 1 else 0,
    x => if (x.word.length < 6) 1 else 0,
    x => if (x.word.endsWith("s")) 1 else 0,
    x => if (x.word.endsWith("ed")) 1 else 0,
    x => if (x.word.endsWith("ing")) 1 else 0,
    x => if (x.word(0).isUpper) 1 else 0,
    x => if (x.word.forall(Character.isLetter)) 1 else 0,
    x => if (x.word.forall(Character.isDigit)) 1 else 0
  )

}

object PosTaggerEN {
  /** loads an english POS tagger saved in json format
    * unfortunately the feature extracting rules are not easily serializable
    * so we need to define the load function for each subclass separately
    */
  def load(taggerFile: String) = new PosTaggerEN {
    (weights, seenTags) = PosTagger.loadTaggerData(taggerFile)
  }
  def loadDefault = new PosTaggerEN {
    (weights, seenTags) = PosTagger.loadTaggerDataFromResource(Source.fromResource("trained_classifiers/tagger_en.json"))
  }
}