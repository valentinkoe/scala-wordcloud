package wordcloud.pos

trait FeatureExtractorDE extends FeatureExtractor {

  // tuples should be: prevWord, prevTag, word
  override val featureFuncs: List[ContextInfo => Float] = List(
    x => if (x.prevTag == BOS) 1 else 0,  // beginning of sentence
    x => if (x.prevWord.length <= 3) 1 else 0,
    x => if (x.prevWord.length > 3 &&  x.prevWord.length < 6) 1 else 0,
    x => if (x.prevWord.length >= 6) 1 else 0,
    x => if (x.prevTag == "ART") 1 else 0,
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
