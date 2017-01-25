package wordcloud

import java.io.{FileInputStream, ObjectInputStream}

import org.json4s.DefaultFormats
import wordcloud.tokenize.TokenizerDE
import wordcloud.pos.{PosTagger, PosTaggerDE}
import wordcloud.chunk.SimpleChunker

import scala.collection.immutable.ListMap
import org.json4s.jackson.Json

object Processing {

  // load tagger
  val tagger = PosTaggerDE.load("tagger.json")

  val tokenizer = new TokenizerDE
  val chunker = new SimpleChunker

  def getChunkCounts(input: String): String = {

    val taggedSents = tokenizer.tokenizedSents(input.iterator).map(x => x.zip(tagger.tag(x)))
    val chunkLists = taggedSents.map(x => chunker.getChunks(x))

    val chunkCounts = chunkLists.flatten.foldLeft(Map.empty[String, Int]){
      (count, chunk) => count + (chunk -> (count.getOrElse(chunk, 0) + 1))}

    Json(DefaultFormats).write(ListMap(chunkCounts.toList.sortBy{-_._2}:_*))

  }

}
