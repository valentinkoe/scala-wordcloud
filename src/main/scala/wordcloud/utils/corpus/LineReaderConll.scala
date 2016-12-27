package wordcloud.utils.corpus

import wordcloud.utils.Token

trait LineReaderConll {
  def lineToToken(line : String): Token = {
    if (line == "") {
      Token("", "", "EOS", "")
    }
    else {
      val split = line.split("\t")
      Token(split(1), split(2), split(4), split(10))
    }
  }
}