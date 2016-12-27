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
    val lines = Source.fromFile(corpusFile).getLines
    val tokenLines = lines.map(x => lineToToken(x))
    iterSplitAt(tokenLines, (x: Token) => x == Token("", "", "EOS", ""))
  }

}