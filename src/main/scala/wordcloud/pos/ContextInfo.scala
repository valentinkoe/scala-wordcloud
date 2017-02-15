package wordcloud.pos

import wordcloud.utils.POSAnnotatedToken

case class ContextInfo(prevToken: POSAnnotatedToken, word: String)