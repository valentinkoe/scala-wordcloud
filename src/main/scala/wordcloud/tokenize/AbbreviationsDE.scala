package wordcloud.tokenize

trait AbbreviationsDE extends Abbreviations {

  override val abbrevResource: String = "abbreviations/de"
  override lazy val abbrevsNoDot = getAbbrevRegex(trailingDot = false)
  override lazy val abbrevsWithDotRE = getAbbrevRegex(trailingDot = true)

}