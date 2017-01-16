package wordcloud.pos

trait FeatureExtractor {

  def getFeatureVec(prevWord: String, prevTag: String, word: String, train: Boolean) : Array[Float]

}
