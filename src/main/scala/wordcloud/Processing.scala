package wordcloud

import wordcloud.tokenize._
import wordcloud.pos._
import wordcloud.chunk.SimpleChunker

import scala.collection.immutable.ListMap
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

object Processing {

  lazy val tokenizerDE = new TokenizerDE
  lazy val taggerDE = PosTaggerDE.load("src/main/resources/trained_taggers/tagger_de.json")

  lazy val tokenizerEN = new TokenizerEN
  lazy val taggerEN = PosTaggerEN.load("src/main/resources/trained_taggers/tagger_en.json")

  // TODO: chunkers

  def getTokens(input: Iterator[Char], lang: String, adj: Boolean, noun: Boolean): Iterator[String] = {

    if (!List("de", "en").contains(lang)) throw UnsupportedLanguageException(s"language not yet supported: $lang")

    var tokenizer: Tokenizer = null
    var tagger: PosTagger = null
    var adjStart: String = null
    var nounStart: String = null

    if (lang == "de") {
      tokenizer = tokenizerDE
      tagger = taggerDE
      adjStart = "ADJ"
      nounStart = "N"
    }
    else if (lang == "en") {
      tokenizer = tokenizerEN
      tagger = taggerEN
      adjStart = "JJ"
      nounStart = "NN"
    }

    tokenizer.tokenizedSents(input)
      .map(x => x.zip(tagger.tagSent(x)))
      .flatten
      .withFilter(x => if ((!adj && !noun)
                            || (adj && x._2.startsWith(adjStart))
                            || (noun && x._2.startsWith(nounStart))) true
                        else false)
      .map(x => x._1)
  }

}
