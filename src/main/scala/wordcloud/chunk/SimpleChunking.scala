package wordcloud.chunk

import wordcloud.utils.splitAt

object SimpleChunking {
  def getChunks(taggedSent: List[(String, String)]): List[String] = {
    splitAt(taggedSent.iterator, (x: (String, String)) => !x._2.startsWith("N"))
      .map(_.map(_._1)).map(_.mkString(" ")).toList
  }
}