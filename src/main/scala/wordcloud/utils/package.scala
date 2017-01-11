package wordcloud

import scala.io.Source

package object utils {

  /**
    * transforms an iterator into returning sublists of the originally
    * iterated sequence based on a given predicate
    * @param iter iterator to "split"
    * @param p predicate split is based on
    * @tparam T type of iterated items
    * @return iterator of lists
    */
  def iterSplitAt[T](iter: Iterator[T], p: T => Boolean): Iterator[List[T]] =
    new Iterator[List[T]] {
      def hasNext = iter.hasNext
      def next = {
        val nextList = iter.takeWhile(!p(_)).toList
        iter.dropWhile(p)
        nextList
      }
    }.withFilter(x => x.nonEmpty)

  def getAbbrevs(abbrevFile: String): Set[String] = {
    Source.fromFile("src/main/resources/abbreviations_de")
      .getLines().filter(_ != "").filter(_(0) != '#').toSet
  }

}