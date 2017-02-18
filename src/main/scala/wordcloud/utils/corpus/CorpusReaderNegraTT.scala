package wordcloud.utils.corpus

class CorpusReaderNegraTT(corpusFile: String) extends CorpusReader(corpusFile) {

  override val commentIndicator = "%"

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split("\t+")(0)
  }

  override def lineToAnnotatedToken(line: String): AnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ => {val split = line.split("\t+")
      AnnotatedToken(split(0), split(1), NO_CHUNK)}  // no information about chunks
  }

}