package wordcloud

import scala.collection.mutable.ListBuffer

package object utils {

  /**
    * transforms an iterator into returning sublists of the originally
    * iterated sequence based on a given predicate
    * @param iter iterator to "split"
    * @param p predicate split is based on
    * @tparam T type of iterated items
    * @return iterator of lists
    */
  def iterSplitAt[T](iter: Iterator[T], p: T => Boolean, keepSplitVals:Boolean = false): Iterator[List[T]] =
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

}