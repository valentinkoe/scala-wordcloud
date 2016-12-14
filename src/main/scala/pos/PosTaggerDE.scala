package pos

class PosTaggerDE extends PosTagger with FeatureExtractorDE

trait FeatureExtractorDE extends FeatureExtractor {

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
  private val features = featureList.map(x => x -> featureList.indexOf(x)).toMap
  private val numFeatures = features.size
  private val wordEndsList: List[String] =
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


  override def getFeatureVec(prevWord: String, prevTag: String, word: String, train: Boolean): Array[Int] = {

    val featureVec = if (train) Array.fill[Int](numFeatures)(-1) else Array.fill[Int](numFeatures)(0)

    // TODO: order feature checks by expected frequency of their real world occurrence, to avoid some of them
    // TODO: evaluate current and add more features

    if (prevWord.equals("") && prevTag.equals("")) featureVec(features("beginningOfSentence")) = 1
    else {
      // features based on previous word
      if (prevWord.length < 3) featureVec(features("prevWordLengthShort")) = 1
      else if (prevWord.length < 6) featureVec(features("prevWordLengthMiddle")) = 1
      else featureVec(features("prevWordLengthLong")) = 1

      // features based on previous tag
      if (prevTag.startsWith("N")) featureVec(features("prevTagNoun")) = 1
      else if (prevTag.startsWith("V")) featureVec(features("prevTagVerb")) = 1
      else if (prevTag.startsWith("ADJ")) featureVec(features("prevTagAdjective")) = 1
      else featureVec(features("prevTagOther")) = 1
    }

    // features based on current word
    // length
    if (word.length < 3) featureVec(features("wordLengthShort")) = 1
    else if (word.length == 3) featureVec(features("wordLength3")) = 1
    else if (word.length == 4) featureVec(features("wordLength4")) = 1
    else if (word.length == 5) featureVec(features("wordLength5")) = 1
    else featureVec(features("wordLengthLong")) = 1
    // end characters

    for (ending <- wordEndsList if word.endsWith(ending)) (
        featureVec(features("endsWith" + ending.toUpperCase)) = 1
        )

    // start characters
    if (word(0).isUpper) featureVec(features("startsWithCapital")) = 1
    if (word.toLowerCase.startsWith("ge")) featureVec(features("startsWithGE")) = 1

    featureVec
    }
 }