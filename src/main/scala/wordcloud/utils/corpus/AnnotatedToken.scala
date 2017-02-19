package wordcloud.utils.corpus

case class AnnotatedToken(word: String, pos: String, isChunk: Boolean) {
  val length = word.length
}