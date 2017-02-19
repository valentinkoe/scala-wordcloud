package wordcloud.tokenize

trait AbbreviationsDE extends Abbreviations {
  protected override val abbrevResource: String = "abbreviations/de"
  protected override lazy val abbrevsNoDot = getAbbrevRegex(trailingDot = false)
  protected override lazy val abbrevsWithDotRE = getAbbrevRegex(trailingDot = true)
}