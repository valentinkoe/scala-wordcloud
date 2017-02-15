package wordcloud.utils.corpus

import wordcloud.utils.POSAnnotatedToken

class CorpusReaderConll09(corpusFile: String) extends CorpusReader(corpusFile) {

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split("\t")(1)
  }

  def lineToPOSAnnotatedToken(line : String): POSAnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ => {val split = line.split("\t")
      POSAnnotatedToken(split(1), split(4))}
  }

}