package wordcloud

import scala.collection.mutable.ListBuffer

package object utils {

  /**
    * transforms an iterator into returning sublists of the originally
    * iterated sequence based on a given predicate
    * http://stackoverflow.com/a/31229209
    * @param iter iterator to "split"
    * @param p predicate split is based on
    * @tparam T type of iterated items
    * @return iterator of lists
    */
  def splitAt[T](iter: Iterator[T], p: T => Boolean, keepSplitVals:Boolean = false): Iterator[List[T]] =
    new Iterator[List[T]] {
      val nextList = new ListBuffer[T]
      def hasNext() = iter.hasNext
      def next = {
        var noMatch = true
        while (noMatch && iter.hasNext) {
          val n = iter.next
          if (p(n)) {
            noMatch = false
            if (keepSplitVals) nextList += n
          }
          else nextList += n
        }
        val nl = nextList.toList
        nextList.clear()
        nl
      }
    }.withFilter(_.nonEmpty)

  // http://stackoverflow.com/a/12665467
  object EqualPair {
    def unapply[T](pair:(T,T)): Option[T] =
      if (pair._1 == pair._2) Some(pair._1) else None
  }

  // http://stackoverflow.com/a/4058564
  // TODO: workaround import inside function
  def dotProduct[T](v1: Seq[T], v2: Seq[T])(implicit n: Numeric[T]): T = {
    import n._
    v1.zip(v2).map {case (x, y) => x*y} sum
  }

}