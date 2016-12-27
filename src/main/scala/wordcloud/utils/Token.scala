package wordcloud.utils

case class Token (form: String,
                  lemma: String,
                  pos: String,
                  label: String) {
  def length = form.length
}