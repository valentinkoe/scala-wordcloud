package wordcloud.utils

package object corpus {
  val EOS = "EOS"
  val EOS_TOKEN = POSAnnotatedToken("", EOS)
  val BOS = "BOS"
  val BOS_TOKEN = POSAnnotatedToken("", BOS)
}