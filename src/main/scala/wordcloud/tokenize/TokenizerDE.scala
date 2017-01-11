package wordcloud.tokenize

import wordcloud.utils

class TokenizerDE extends Tokenizer {

  val wordRE =
    ("(['\"„“\\(\\[\\{<]+)?" +           // preceding punctuation
    "([\\p{L}-]+)" +                     // word itself, may contain hyphens
    "([\\.!?,:;'\"„“\\)\\]\\}>]+)?").r   // succeeding punctuation
  val sentEndRE = "(\\s+)(\\w+)([.!?]+)".r

  // list of abbreviations that do not terminate a sentence when a dot follows
  val abbrevs = utils.getAbbrevs("src/main/resources/abbreviations_de")

  // TODO: special cases: english "I'd", "it's", ...; german "fürs", "am", ...

  override def splitSentences(text: String): List[String] = ???
    //find words that end with any sentence ending character (sequence thereof)
    //if the corresponding end is a dot and the "word" is in abbrevs (or a numeric string?)
    //  continue to the next one
    //else
    //  return the text up to this match

  override def tokenize(text: String): List[String] = {
    var tokens = List[String]()
    for (str <- blankRE.split(text)) {
      str match {
        case wordRE(pre, in, after) => {
          if (pre != null) {
            tokens = tokens :+ pre
          }
          tokens = tokens :+ in
          if (after != null) {
            tokens = tokens :+ after
          }
        }
        case _ => tokens = tokens :+ str  // all other cases
      }
    }
    tokens
  }

}
