package wordcloud

import wordcloud.tokenize._
import wordcloud.pos._
import wordcloud.chunk.SimpleChunker

import scala.collection.immutable.ListMap
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

object Processing {

//  var tokenizer: Tokenizer
//  var tagger: PosTagger
//  var chunker: ??? // TODO
//
//  def getChunkCounts(input: Iterator[Char], language: String): Map[String, Int] = {
//
//    if (language == "de") {
//      tokenizer = new TokenizerDE
//      tagger = PosTaggerDE.load("...")  // TODO: from resources
//      chunker = null  // TODO
//    }
//    else if (language == "en") {
//      tokenizer = new TokenizerEN
//      tagger = PosTaggerEN.load("...")  // TODO: from resource
//      chunker = null // TODO
//    }
//    else {
//      throw UnsupportedLanguageException(s"language not yet supported: $language")
//    }
//
//    val taggedSents = tokenizer.tokenizedSents(input).map(x => x.zip(tagger.tagSent(x)))
//
//    Map()
//    // TODO:
//    //val chunkLists = taggedSents.map(x => chunker.getChunks(x))
//    //val chunkCounts = chunkLists.flatten.foldLeft(Map.empty[String, Int]){
//    //  (count, chunk) => count + (chunk -> (count.getOrElse(chunk, 0) + 1))}
//    //Json(DefaultFormats).write(ListMap(chunkCounts.toList.sortBy{-_._2}:_*))
//
//  }

}
