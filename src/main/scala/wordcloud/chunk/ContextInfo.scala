package wordcloud.chunk

/** utility class for storing information about a words context */
case class ContextInfo(prevWord: String, prevTag: String,
                       word: String, tag: String,
                       precWord: String, precTag: String)
