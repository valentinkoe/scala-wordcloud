package wordcloud.pos

trait FeatureExtractorDE {

  private val featureList : List[String] =
    List("beginningOfSentence",
      "prevWordLengthShort",
      "prevWordLengthMiddle",
      "prevWordLengthLong",
      "prevTagArticle",
      "prevTagNoun",
      "prevTagVerb",
      "prevTagAdjective",
      "prevTagOther",
      "wordLengthShort",
      "wordLength3",
      "wordLength4",
      "wordLength5",
      "wordLengthLong",
      "endsWithE",
      "endsWithEN",
      "endsWithER",
      "endsWithES",
      "endsWithET",
      "endsWithS",
      "endsWithST",
      "endsWithSE",
      "endsWithUNG",
      "endsWithKEIT",
      "endsWithHEIT",
      "startsWithCapital",
      "startsWithGE"
    )
  private val suffixList: List[String] =
    List("heit",
      "keit",
      "ung",
      "se",
      "st",
      "en",
      "er",
      "es",
      "et",
      "e",
      "s"
    )
  private val features = featureList.map(x => x -> featureList.indexOf(x)).toMap
  private val numFeatures = features.size


  def getFeatureVec(prevWord: String, prevTag: String, word: String, train: Boolean) : Array[Float] = {

    val featureVec = if (train) Array.fill[Float](numFeatures)(-1) else Array.fill[Float](numFeatures)(0)

    if (prevTag == BOS) featureVec(features("beginningOfSentence")) = 1
    else {
      if (prevWord.length < 3) featureVec(features("prevWordLengthShort")) = 1
      else if (prevWord.length < 6) featureVec(features("prevWordLengthMiddle")) = 1
      else featureVec(features("prevWordLengthLong")) = 1

      if (prevWord.startsWith("N")) featureVec(features("prevTagNoun")) = 1
      else if (prevWord.startsWith("V")) featureVec(features("prevTagVerb")) = 1
      else if (prevWord.startsWith("ADJ")) featureVec(features("prevTagAdjective")) = 1
      else featureVec(features("prevTagOther")) = 1
    }

    if (word.length < 3) featureVec(features("wordLengthShort")) = 1
    else if (word.length == 3) featureVec(features("wordLength3")) = 1
    else if (word.length == 4) featureVec(features("wordLength4")) = 1
    else if (word.length == 5) featureVec(features("wordLength5")) = 1
    else featureVec(features("wordLengthLong")) = 1

    for (suffix <- suffixList if word.endsWith(suffix)) {
      featureVec(features("endsWith" + suffix.toUpperCase)) = 1
    }

    if (word(0).isUpper) featureVec(features("startsWithCapital")) = 1
    if (word.toLowerCase.startsWith("ge")) featureVec(features("startsWithGE")) = 1

    featureVec
  }
}
