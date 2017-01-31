package wordcloud.http

import org.http4s.server.blaze.BlazeBuilder

object BlazeServer extends App {
  BlazeBuilder.bindHttp(8080)
    .mountService(WordcloudService.service, "/")
    .run
    .awaitShutdown()
}
