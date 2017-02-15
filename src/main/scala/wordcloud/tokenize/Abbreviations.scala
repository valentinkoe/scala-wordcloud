package wordcloud.tokenize

import scala.io.Source
import scala.util.matching.Regex

trait Abbreviations {

  val abbrevResource: String
  val abbrevsNoDot: Regex
  val abbrevsWithDotRE: Regex

  def getAbbrevRegex(trailingDot: Boolean = false): Regex = {
    if (trailingDot) ("(?:" + Source.fromResource(abbrevResource).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|") + ")\\.").r
    else Source.fromResource(abbrevResource).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|").r
  }

}