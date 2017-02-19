package wordcloud.pos

import wordcloud.utils.corpus.BOS_TAG

import scala.io.Source

class PosTaggerDE extends PosTagger {

  protected override val defaultTag: String = "NN"

  protected override val featureFuncs: List[ContextInfo => Double] = List(
    x => if (x.prevTag == BOS_TAG) 1 else 0,  // beginning of sentence
    x => if (x.prevWord.length <= 3) 1 else 0,
    x => if (x.prevWord.length > 3 &&  x.prevWord.length < 6) 1 else 0,
    x => if (x.prevWord.length >= 6) 1 else 0,
    x => if (x.prevTag == "ART") 1 else 0,
    x => if (x.prevTag == "PTKA") 1 else 0,
    x => if (x.prevTag == "PTKZU") 1 else 0,
    x => if (x.prevTag == "TRUNC") 1 else 0,
    x => if (x.prevTag.startsWith("N")) 1 else 0,
    x => if (x.prevTag.startsWith("V")) 1 else 0,
    x => if (x.prevTag.startsWith("ADJ")) 1 else 0,
    x => if (x.word.length <= 3) 1 else 0,
    x => if (x.word.length == 4) 1 else 0,
    x => if (x.word.length == 5) 1 else 0,
    x => if (x.word.length == 6) 1 else 0,
    x => if (x.word.length < 6) 1 else 0,
    x => if (x.word.endsWith("e")) 1 else 0,
    x => if (x.word.endsWith("en")) 1 else 0,
    x => if (x.word.endsWith("es")) 1 else 0,
    x => if (x.word.endsWith("er")) 1 else 0,
    x => if (x.word.endsWith("et")) 1 else 0,
    x => if (x.word.endsWith("st")) 1 else 0,
    x => if (x.word.endsWith("ung")) 1 else 0,
    x => if (x.word.endsWith("heit")) 1 else 0,
    x => if (x.word.endsWith("keit")) 1 else 0,
    x => if (x.word.startsWith("ge")) 1 else 0,
    x => if (x.word(0).isUpper) 1 else 0,
    x => if (x.word.forall(Character.isLetter)) 1 else 0,
    x => if (x.word.forall(Character.isDigit)) 1 else 0
  )

}

object PosTaggerDE {
  /** loads a german POS tagger saved in json format
    * unfortunately the feature extracting rules are not easily serializable
    * so we need to define the load function for each subclass separately
    */
  def load(taggerFile: String) = new PosTaggerDE {
    (weights, seenTags) = PosTagger.loadTaggerData(taggerFile)
  }
  def loadDefault = new PosTaggerDE {
    (weights, seenTags) = PosTagger.loadTaggerDataFromResource(Source.fromResource("trained_classifiers/tagger_de.json"))
  }
}