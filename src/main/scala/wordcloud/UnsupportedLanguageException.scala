package wordcloud

case class UnsupportedLanguageException(message: String = "", cause: Throwable = null)
  extends Exception(message, cause)