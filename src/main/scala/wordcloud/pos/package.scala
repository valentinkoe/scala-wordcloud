package wordcloud

import wordcloud.utils.AnnotatedToken

package object pos {
  val BOS = "BOS"
  val EOS = "EOS"
  val BosToken = AnnotatedToken("", "", "BOS", "")
  val EosToken = AnnotatedToken("", "", "EOS", "")
}
