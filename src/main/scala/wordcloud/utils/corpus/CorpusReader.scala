package wordcloud.utils.corpus

import wordcloud.utils._

import scala.io.Source

abstract class CorpusReader(corpusFile: String) {

  def lineToToken(line : String): AnnotatedToken

  def readTokens(): Iterator[AnnotatedToken] = {
    val lines = Source.fromFile(corpusFile).getLines()
    lines.map(x => lineToToken(x))
  }

  def readSentences(): Iterator[List[AnnotatedToken]] = {
    iterSplitAt(readTokens(), (x: AnnotatedToken) => x == AnnotatedToken("", "", "EOS", ""))
  }

}