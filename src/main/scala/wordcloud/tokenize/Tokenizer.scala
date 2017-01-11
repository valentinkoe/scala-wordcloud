package wordcloud.tokenize

abstract class Tokenizer {

  val blankRE = "\\s+".r

  def splitSentences(text: String): List[String]

  def tokenize(text: String): List[String]

}
