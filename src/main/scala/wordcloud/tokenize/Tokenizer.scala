package wordcloud.tokenize

import wordcloud.utils._

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

abstract class Tokenizer {

  val blankRE = "\\s+".r
  val sentEndRE = "\\S+[.!?]+".r
  val wordRE =
    ("(['’\"„“\\(\\[\\{<]+)?" +           // preceding punctuation
    "([\\p{L}-]+)" +                      // word itself, may contain hyphens
    "([\\.!?,:;'’\"„“\\)\\]\\}>]+)?").r   // succeeding punctuation
  val abbrevsWithDotRE: Regex
  val abbrevsNoDot: Regex

  def splitToSents(text: Iterator[Char]): Iterator[String] = {
    // split at blank characters
    val blankSplitted = iterSplitAt(text, (x: Char) => x match {case blankRE() => true; case _ => false}, keepSplitVals=false)
    // the above yields lists of characters, make strings again
    val tokens = blankSplitted.map(x => x.mkString)
    // split at sentence delimiting characters, except if these are abbreviations
    val sents = iterSplitAt(tokens, (x: String) =>
      x match {case abbrevsWithDotRE() => false;  // TODO: refine special cases/abbreviations
               case sentEndRE() => true;          // TODO: improve regex
               case _ => false},
      keepSplitVals = true)
    sents.map(x => x.mkString(" "))
  }

  def tokenizeSent(text: String): List[String] = {
    var tokens = ListBuffer[String]()
    for (str <- blankRE.split(text)) {
      str match {
        case wordRE(pre_punct, token, suc_punct) => {
          if (pre_punct != null) {
            tokens += pre_punct
          }
          if (suc_punct != null && suc_punct.length == 1
            && suc_punct(0) == '.' && (token match {case abbrevsNoDot() => true; case _ => false})) {
            // expecting abbreviations to be followed by exactly 1 dot
            tokens += token + suc_punct
          }
          else {
            tokens += token
            if (suc_punct != null) {
              tokens += suc_punct
            }
          }
        }
        // TODO: case numeric values in parantheses like "(1972)" or "(1984-1988)" as encounted in e.g. wikipedia texts
        // TODO: case numeric values at sentence end like "1951."
        case _ => tokens = tokens :+ str // all other cases, also captures cases of abbreviations that contain punctuation
      }
    }
    tokens.toList
  }

  def tokenizedSents(text: Iterator[Char]): Iterator[List[String]] = splitToSents(text).map(tokenizeSent)

}

class TokenizerDE extends Tokenizer with AbbreviationsDE

class TokenizerEN extends Tokenizer with AbbreviationsEN