package wordcloud.pos

trait FeatureExtractor {

  val featureFuncs: List[ContextInfo => Float]

  def getFeatureVec(feat: ContextInfo, train: Boolean) : Array[Float] = {
    var featFuncsApplied = featureFuncs.map(_(feat))
    // TODO: does this distinction between train and tag really matter that much or could we just drop the train flag?
    if (train) featFuncsApplied = featFuncsApplied.map(x => if (x == 0F) -1F else 1F)
    featFuncsApplied.toArray
  }

}
