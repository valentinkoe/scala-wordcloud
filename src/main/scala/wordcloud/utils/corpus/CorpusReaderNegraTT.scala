package wordcloud.utils.corpus

import wordcloud.utils.POSAnnotatedToken

class CorpusReaderNegraTT(corpusFile: String) extends CorpusReader(corpusFile) {

  override val commentIndicator = "%"

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split("\t+")(0)
  }

  override def lineToPOSAnnotatedToken(line: String): POSAnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ => {val split = line.split("\t+")
      POSAnnotatedToken(split(0), split(1))}
  }

}