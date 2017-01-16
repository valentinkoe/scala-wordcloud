package wordcloud.pos

class PosTaggerDE extends PosTagger with FeatureExtractorDE {
  override val defaultTag: String = "NN"
}