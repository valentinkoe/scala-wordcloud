package wordcloud.utils.corpus

import wordcloud.utils.POSAnnotatedToken

class CorpusReaderConll00(corpusFile: String) extends CorpusReader(corpusFile) {

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split(" +")(0)
  }

  def lineToPOSAnnotatedToken(line : String): POSAnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ => {val split = line.split(" +")
      POSAnnotatedToken(split(0), split(1))}
  }

}