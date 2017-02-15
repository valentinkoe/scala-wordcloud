package wordcloud.pos

import wordcloud.utils.corpus.BOS_TOKEN

class PosTaggerEN extends PosTagger {

  override val defaultTag: String = "NN"

  override val featureFuncs: List[ContextInfo => Float] = List(
    x => if (x.prevToken == BOS_TOKEN) 1 else 0,  // beginning of sentence
    x => if (x.prevToken.length <= 3) 1 else 0,
    x => if (x.prevToken.length > 3 &&  x.prevToken.length < 6) 1 else 0,
    x => if (x.prevToken.length >= 6) 1 else 0,
    x => if (x.prevToken.pos == "DT") 1 else 0,
    x => if (x.prevToken.pos == "TO") 1 else 0,
    x => if (x.prevToken.pos.startsWith("N")) 1 else 0,
    x => if (x.prevToken.pos.startsWith("J")) 1 else 0,
    x => if (x.prevToken.pos.startsWith("V")) 1 else 0,
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

  def load(filename: String): PosTagger = {
    val (loadedWeights, loadedSeenTags) = PosTagger.loadTaggerData(filename)
    new PosTaggerEN() {
      weights = loadedWeights
      seenTags = loadedSeenTags
    }
  }

}