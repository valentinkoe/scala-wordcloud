package wordcloud.chunk

import wordcloud.utils.corpus.{BOS_TAG, EOS_TAG}

class ChunkerEN extends Chunker {

  override val threshold: Float = 1
  override val alpha: Float = 2

  override val featureFuncs: List[(ContextInfo) => Float] = List(
    x => if (x.prevTag == BOS_TAG) 1 else 0,
    x => if (x.prevTag.startsWith("NN")) 1 else 0,
    x => if (x.tag.startsWith("NN")) 1 else 0,
    x => if (x.precTag == EOS_TAG) 1 else 0,
    x => if (x.precTag.startsWith("NN")) 1 else 0
  )

  override var weights: Array[Float] = Array.fill(featureFuncs.size)(1.0F)

}
