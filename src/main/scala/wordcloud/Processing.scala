package wordcloud

import wordcloud.tokenize._
import wordcloud.pos._
import wordcloud.chunk.ChunkerEN

/**
  * bundles up processing steps for wordcloud requests
  */
object Processing {

  // load processors only for requested languages, thus lazy
  private lazy val tokenizerDE = new TokenizerDE
  private lazy val taggerDE = PosTaggerDE.loadDefault

  private lazy val tokenizerEN = new TokenizerEN
  private lazy val taggerEN = PosTaggerEN.loadDefault
  private lazy val chunkerEN = ChunkerEN.loadDefault

  /** tokenizes the given text and optionally filters adjectives */
  def getTokens(input: Iterator[Char], lang: String, adj: Boolean=false): Iterator[String] = {

    if (!List("de", "en").contains(lang)) throw UnsupportedLanguageException(s"language not yet supported: $lang")

    // get the processors according to requested language
    var tokenizer: Tokenizer = null
    var tagger: PosTagger = null
    var adjStart: String = null

    if (lang == "de") {
      tokenizer = tokenizerDE
      tagger = taggerDE
      adjStart = "ADJ"
    }
    else if (lang == "en") {
      tokenizer = tokenizerEN
      tagger = taggerEN
      adjStart = "JJ"
    }

    tokenizer.tokenizedSents(input)                 // tokenize
      .map(x => x.zip(tagger.tagSent(x).toList))    // tag with POS
      .flatten                                      // remove sentence boundaries
      .withFilter(x => if (!adj || (adj && x._2.startsWith(adjStart))) true   // filter non-adjectives if requested
                       else false)
      .map(x => x._1)                               // remove POS tags again
  }

  /** extracts noun chunks from the given text */
  def getChunks(input: Iterator[Char], lang: String): Iterator[String] = {

    if (lang != "en") throw UnsupportedLanguageException(s"language not yet supported: $lang")

    // currently only English is supported for chunking
    val tokenizer = tokenizerEN
    val tagger = taggerEN
    val chunker = chunkerEN

    tokenizer.tokenizedSents(input)               // tokenize
      .map(x => x.zip(tagger.tagSent(x).toList))  // tag with POS
      .map(_.unzip)                               // separate iterables of tokens and text
      .map {
        case (toks, tags) => chunker.extractChunksFromSent(toks, tags)  // get chunks
      }
      .flatten                                    // remove sentence boundaries
  }
}
