package wordcloud.pos

/** utility class for storing information about a words context */
case class ContextInfo(prevWord: String, prevTag: String, word: String)