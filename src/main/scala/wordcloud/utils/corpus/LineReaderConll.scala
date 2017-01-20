package wordcloud.utils.corpus

import wordcloud.utils.AnnotatedToken

trait LineReaderConll {
  def lineToToken(line : String): AnnotatedToken = {
    if (line == "") {
      AnnotatedToken("", "", "EOS", "")
    }
    else {
      val split = line.split("\t")
      AnnotatedToken(split(1), split(2), split(4), split(10))
    }
  }
}