package wordcloud.utils.corpus

import wordcloud.utils._

import scala.io.Source

abstract class CorpusReader(corpusFile: String) {

  val commentIndicator: String = null

  def lineToToken(line: String): String

  def lineToAnnotatedToken(line : String): AnnotatedToken

  def readTokens(): Iterator[String] = {
    var lines = Source.fromFile(corpusFile).getLines()
    if (commentIndicator != null) lines = lines.filter(!_.startsWith(commentIndicator))
    lines.map(x => lineToToken(x))
  }

  def readAnnotatedTokens(): Iterator[AnnotatedToken] = {
    var lines = Source.fromFile(corpusFile).getLines()
    if (commentIndicator != null) lines = lines.withFilter(!_.startsWith(commentIndicator))
    lines.map(x => lineToAnnotatedToken(x))
  }

  def readSentences(): Iterator[List[String]] = {
    iterSplitAt(readTokens(), (x: String) => x == "")
  }

  def readAnnotatedSentences(): Iterator[List[AnnotatedToken]] = {
    iterSplitAt(readAnnotatedTokens(), (x: AnnotatedToken) => x == EOS_TOKEN)
  }

  def readAnnotatedNgramSentences(n: Int): Iterator[Iterator[List[AnnotatedToken]]] = {
    readAnnotatedSentences().map(s => (List.fill(n-2)(BOS_TOKEN) ++ s ++ List.fill(n-2)(EOS_TOKEN)).sliding(n, 1))
  }

}

object CorpusReader {
  def combinedReaders(corpusReaders: List[CorpusReader]) = new CorpusReader(null) {

    // not necessary just to suppress warnings
    override def lineToToken(line: String): String = ???
    override def lineToAnnotatedToken(line: String): AnnotatedToken = ???

    override def readTokens(): Iterator[String] = {
      val iterators = corpusReaders.map(x => x.readTokens())
      iterators.foldLeft(Iterator[String]())(_ ++ _)
    }

    override def readAnnotatedTokens(): Iterator[AnnotatedToken] = {
      val iterators = corpusReaders.map(x => x.readAnnotatedTokens())
      iterators.foldLeft(Iterator[AnnotatedToken]())(_ ++ _)
    }

    override def readSentences(): Iterator[List[String]] = {
      val iterators = corpusReaders.map(x => x.readSentences())
      iterators.foldLeft(Iterator[List[String]]())(_ ++ _)
    }

    override def readAnnotatedSentences(): Iterator[List[AnnotatedToken]] = {
      val iterators = corpusReaders.map(x => x.readAnnotatedSentences())
      iterators.foldLeft(Iterator[List[AnnotatedToken]]())(_ ++ _)
    }

  }
}