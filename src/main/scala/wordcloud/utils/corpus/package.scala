package wordcloud.utils

package object corpus {
  val BOS = "<s>"
  val EOS = "</s>"
  val BOS_TAG = "BOS"
  val EOS_TAG = "EOS"
  type ChunkVal = Boolean
  val NO_CHUNK: ChunkVal = false
  val IS_CHUNK: ChunkVal = true
  val BOS_TOKEN = AnnotatedToken(BOS, BOS_TAG, NO_CHUNK)
  val EOS_TOKEN = AnnotatedToken(EOS, EOS_TAG, NO_CHUNK)
}