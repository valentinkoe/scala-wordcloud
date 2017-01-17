package wordcloud.utils.corpus

import wordcloud.utils._

import scala.io.Source

abstract class CorpusReader(corpusFile: String) {

  def lineToToken(line : String): Token

  def readTokens(): Iterator[Token] = {
    val lines = Source.fromFile(corpusFile).getLines()
    lines.map(x => lineToToken(x))
  }

  def readSentences(): Iterator[List[Token]] = {
    iterSplitAt(readTokens(), (x: Token) => x == Token("", "", "EOS", ""))
  }

}