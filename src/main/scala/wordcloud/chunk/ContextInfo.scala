package wordcloud.chunk

case class ContextInfo(prevWord: String, prevTag: String,
                       word: String, tag: String,
                       precWord: String, precTag: String)
