package wordcloud.tokenize

import scala.io.Source
import scala.util.matching.Regex

/**  */
trait Abbreviations {
  protected val abbrevResource: String    // where to find abbreviation file
  protected val abbrevsNoDot: Regex       // abbreviation files store the abbrevs without dot
  protected val abbrevsWithDotRE: Regex   // for tokenization we need them also with dots
  /** reads in the abbreviation as regex as requested, filters comments (lines starting with #) on the fly */
  protected def getAbbrevRegex(trailingDot: Boolean = false): Regex = {
    if (trailingDot) ("(?:" + Source.fromResource(abbrevResource).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|") + ")\\.").r
    else Source.fromResource(abbrevResource).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|").r
  }
}