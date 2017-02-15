package wordcloud.utils.corpus

import wordcloud.utils._

import scala.io.Source

abstract class CorpusReader(corpusFile: String) {

  val commentIndicator: String = null

  def lineToToken(line: String): String

  def lineToPOSAnnotatedToken(line : String): POSAnnotatedToken

  def readTokens(): Iterator[String] = {
    var lines = Source.fromFile(corpusFile).getLines()
    if (commentIndicator != null) lines = lines.filter(!_.startsWith(commentIndicator))
    lines.map(x => lineToToken(x))
  }

  def readPOSAnnotatedTokens(): Iterator[POSAnnotatedToken] = {
    var lines = Source.fromFile(corpusFile).getLines()
    if (commentIndicator != null) lines = lines.filter(!_.startsWith(commentIndicator))
    lines.map(x => lineToPOSAnnotatedToken(x))
  }

  def readSentences(): Iterator[List[String]] = {
    iterSplitAt(readTokens(), (x: String) => x == "")
  }

  def readPOSAnnotatedSentences(): Iterator[List[POSAnnotatedToken]] = {
    iterSplitAt(readPOSAnnotatedTokens(), (x: POSAnnotatedToken) => x == EOS_TOKEN)
  }

}

object CorpusReader {
  def combinedReaders(corpusReaders: List[CorpusReader]) = new CorpusReader(null) {

    // not necessary just to suppress warnings
    override def lineToToken(line: String): String = ???
    override def lineToPOSAnnotatedToken(line: String): POSAnnotatedToken = ???

    override def readTokens(): Iterator[String] = {
      val iterators = corpusReaders.map(x => x.readTokens())
      iterators.foldLeft(Iterator[String]())(_ ++ _)
    }

    override def readPOSAnnotatedTokens(): Iterator[POSAnnotatedToken] = {
      val iterators = corpusReaders.map(x => x.readPOSAnnotatedTokens())
      iterators.foldLeft(Iterator[POSAnnotatedToken]())(_ ++ _)
    }

    override def readSentences(): Iterator[List[String]] = {
      val iterators = corpusReaders.map(x => x.readSentences())
      iterators.foldLeft(Iterator[List[String]]())(_ ++ _)
    }

    override def readPOSAnnotatedSentences(): Iterator[List[POSAnnotatedToken]] = {
      val iterators = corpusReaders.map(x => x.readPOSAnnotatedSentences())
      iterators.foldLeft(Iterator[List[POSAnnotatedToken]]())(_ ++ _)
    }

  }
}