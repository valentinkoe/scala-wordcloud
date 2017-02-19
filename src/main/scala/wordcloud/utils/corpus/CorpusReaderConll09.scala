package wordcloud.utils.corpus

class CorpusReaderConll09(corpusFile: String) extends CorpusReader(corpusFile) {

  override def lineToToken(line: String): String = line match {
    case "" => ""
    case _ => line.split("\t")(1)
  }

  def lineToAnnotatedToken(line : String): AnnotatedToken = line match {
    case "" => EOS_TOKEN
    case _ => {val split = line.split("\t")
      AnnotatedToken(split(1), split(4), NO_CHUNK)}  // no information about chunks
  }

}