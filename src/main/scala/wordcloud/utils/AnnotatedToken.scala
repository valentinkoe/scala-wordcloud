package wordcloud.utils

case class AnnotatedToken(form: String, lemma: String, pos: String, label: String) {
  val length = form.length
}