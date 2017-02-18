package wordcloud.utils.corpus

class CorpusReaderConll00(corpusFile: String) extends CorpusReader(corpusFile) {

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split(" +")(0)
  }

  def lineToAnnotatedToken(line: String): AnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ =>
      val split = line.split(" +")
      if (split(2).endsWith("NP")) AnnotatedToken(split(0), split(1), IS_CHUNK)
      else AnnotatedToken(split(0), split(1), NO_CHUNK)
  }

}