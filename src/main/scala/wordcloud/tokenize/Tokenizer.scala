package wordcloud.tokenize

import wordcloud.utils._

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/**
  * superclass for tokenizers
  * tokenizers for specific languages must extend this with appropriate abbreviations
  */
abstract class Tokenizer {

  private val blankRE = "\\s+".r
  private val sentEndRE = "\\S+[.!?]+".r
  private val wordRE =
    ("(['’\"„“\\(\\[\\{<]+)?" +           // preceding punctuation
    "([\\p{L}-]+)" +                      // word itself, may contain hyphens
    "([\\.!?,:;'’\"„“\\)\\]\\}>]+)?").r   // succeeding punctuation
  protected val abbrevsWithDotRE: Regex   // language specific
  protected val abbrevsNoDot: Regex       // language specific

  /** splits a character iterator into sentences as strings */
  def splitToSents(text: Iterator[Char]): Iterator[String] = {
    // split at blank characters
    val blankSplitted = splitAt(text, (x: Char) => x match {case blankRE() => true; case _ => false}, keepSplitVals=false)
    // the above yields lists of characters, make strings again
    val tokens = blankSplitted.map(x => x.mkString)
    // split at sentence delimiting characters, except if these are abbreviations
    val sents = splitAt(tokens, (x: String) =>
      x match {case abbrevsWithDotRE() => false;  // abbreviations with trailing dot do not end sentences
               case sentEndRE() => true;          // any number of dots, exclamation marks and question marks terminate sentences
               case _ => false},
      keepSplitVals = true)
    sents.map(x => x.mkString(" "))               // recreate create the sentences again
                                                  // note that sequences of any blanks are now replaced with a single whitespace
  }

  /** splits a single sentence into separate tokens */
  def tokenizeSent(text: String): List[String] = {
    var tokens = ListBuffer[String]()   // build result with a buffer
    blankRE.split(text).foreach {
      case wordRE(pre_punct, token, suc_punct) =>   // found a token with possible leading and trailing punctuation
        if (pre_punct != null) {                    // separate this into the ne
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
      // TODO: case for numeric values in parentheses like "(1972)" or "(1984-1988)" as encountered in many wikipedia texts
      // TODO: case for numeric values at sentence end like "1951."
      case x => tokens = tokens :+ x // all other cases, also captures cases of abbreviations that contain punctuation
    }
    tokens.toList
  }

  /** generates a list of tokenized sentences from a character iterator */
  def tokenizedSents(text: Iterator[Char]): Iterator[List[String]] = splitToSents(text).map(tokenizeSent)

}

// different languages require different abbreviations, but nothing more
class TokenizerDE extends Tokenizer with AbbreviationsDE
class TokenizerEN extends Tokenizer with AbbreviationsEN