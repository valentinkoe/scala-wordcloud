package webapp

import org.http4s.server.blaze.BlazeBuilder

object WordcloudServer extends App {
  BlazeBuilder.bindHttp(8080)
    .mountService(Routes.service, "/")
    .run
    .awaitShutdown()
}
