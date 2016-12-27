package wordcloud.pos

class PosTaggerDE extends PosTagger with FeatureExtractorDE {
  override def defaultTag: String = "NN"
}