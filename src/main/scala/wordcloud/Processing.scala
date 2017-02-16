package wordcloud

import wordcloud.tokenize._
import wordcloud.pos._
import wordcloud.chunk.SimpleChunker

import scala.collection.immutable.ListMap
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

object Processing {

  lazy val tokenizerDE = new TokenizerDE
  lazy val taggerDE = PosTaggerDE.load("trained_taggers/tagger_de.json")

  lazy val tokenizerEN = new TokenizerEN
  lazy val taggerEN = PosTaggerEN.load("trained_taggers/tagger_en.json")

  // TODO: chunkers

  def getTokens(input: Iterator[Char], lang: String, adj: Boolean, noun: Boolean): Iterator[String] = {

    if (lang == "de") {
      tokenizerDE.tokenizedSents(input)
        .map(x => x.zip(taggerDE.tagSent(x)))
        .flatten
        .withFilter(x => if ((!adj && !noun) || (adj && x._2.startsWith("ADJ")) || (noun && x._2.startsWith("N"))) true else false)
        .map(x => x._1)
    }
    else if (lang == "en") {
      tokenizerEN.tokenizedSents(input)
        .map(x => x.zip(taggerEN.tagSent(x)))
        .flatten
        .withFilter(x => if ((!adj && !noun) || (adj && x._2.startsWith("JJ")) || (noun && x._2.startsWith("NN"))) true else false)
        .map(x => x._1)
    }
    else {
      throw UnsupportedLanguageException(s"language not yet supported: $language")
    }
  }

}
