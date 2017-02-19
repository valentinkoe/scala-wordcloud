package wordcloud.tokenize

trait AbbreviationsEN extends Abbreviations {
  protected override val abbrevResource: String = "abbreviations/en"
  protected override lazy val abbrevsNoDot = getAbbrevRegex(trailingDot = false)
  protected override lazy val abbrevsWithDotRE = getAbbrevRegex(trailingDot = true)
}
