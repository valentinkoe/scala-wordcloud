package wordcloud.tokenize

import scala.io.Source
import scala.util.matching.Regex

trait Abbreviations {

  val abbrevFile: String
  val abbrevsNoDot: Regex
  val abbrevsWithDotRE: Regex

  def getAbbrevRegex(trailingDot: Boolean = false): Regex = {
    if (trailingDot) ("(?:" + Source.fromFile(abbrevFile).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|") + ")\\.").r
    else Source.fromFile(abbrevFile).getLines().filter(_ != "").filter(_(0) != '#').map(Regex.quote).mkString("|").r
  }

}
