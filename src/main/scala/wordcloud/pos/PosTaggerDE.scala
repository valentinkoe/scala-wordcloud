package wordcloud.pos

import wordcloud.utils.corpus.BOS_TAG

class PosTaggerDE extends PosTagger {

  override val defaultTag: String = "NN"

  override val featureFuncs: List[ContextInfo => Float] = List(
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

  def load(taggerFile: String): PosTagger = {
    val (loadedWeights, loadedSeenTags) = PosTagger.loadTaggerData(taggerFile)
    new PosTaggerDE() {
      weights = loadedWeights
      seenTags = loadedSeenTags
    }
  }

}