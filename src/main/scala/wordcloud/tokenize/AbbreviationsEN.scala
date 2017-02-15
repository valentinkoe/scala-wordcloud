package wordcloud.tokenize

trait AbbreviationsEN extends Abbreviations {

  override val abbrevResource: String = "abbreviations/en"
  override lazy val abbrevsNoDot = getAbbrevRegex(trailingDot = false)
  override lazy val abbrevsWithDotRE = getAbbrevRegex(trailingDot = true)

}
