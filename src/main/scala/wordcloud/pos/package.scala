package wordcloud

import wordcloud.utils.Token

package object pos {
  val BOS = "BOS"
  val EOS = "EOS"
  val BosToken = Token("", "", "BOS", "")
  val EosToken = Token("", "", "EOS", "")
}
