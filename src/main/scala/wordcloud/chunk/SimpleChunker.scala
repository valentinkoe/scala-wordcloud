package wordcloud.chunk

import wordcloud.utils.iterSplitAt

class SimpleChunker {

  def getChunks(taggedSent: List[(String, String)]): List[String] = {
    iterSplitAt(taggedSent.iterator, (x: (String, String)) => !x._2.startsWith("N"))
      .map(_.map(_._1)).map(_.mkString(" ")).toList
  }

}
