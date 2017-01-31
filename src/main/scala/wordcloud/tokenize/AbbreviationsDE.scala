package wordcloud.tokenize

trait AbbreviationsDE extends Abbreviations {

  override val abbrevFile: String = "src/main/resources/abbreviations_de"
  override val abbrevsNoDot = getAbbrevRegex(trailingDot = false)
  override val abbrevsWithDotRE = getAbbrevRegex(trailingDot = true)

}
