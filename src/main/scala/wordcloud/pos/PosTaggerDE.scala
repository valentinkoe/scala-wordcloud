package wordcloud.pos

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.io.Source

class PosTaggerDE extends PosTagger with FeatureExtractorDE {
  override val defaultTag: String = "NN"
}

object PosTaggerDE {
  def load(filename: String): PosTaggerDE = {
    implicit val formats = DefaultFormats
    val json = parse(Source.fromFile(filename).mkString)
    val loadedWeights = (json\"weights").extract[Map[String,Array[Float]]]
    val loadedSeenTags = (json\"seenTags").extract[Map[String,Set[String]]]
    new PosTaggerDE() with FeatureExtractorDE {
      weights = loadedWeights;
      seenTags = loadedSeenTags
    }
  }
}