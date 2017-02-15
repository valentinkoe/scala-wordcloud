package webapp

import java.util.concurrent.Executors

import org.json4s._
import org.http4s._
import org.http4s.dsl._
import org.http4s.json4s.jackson._
import org.http4s.server.staticcontent
import org.http4s.server.staticcontent.ResourceService.Config
import org.http4s.server.websocket._
import org.http4s.websocket.WebsocketBits._

import scala.concurrent.duration._
import scalaz.stream.{Exchange, Process, time}


object Routes {

  private implicit val scheduledEC = Executors.newScheduledThreadPool(4)

  // Get the static content
  private val static  = cachedResource(Config("/static", "/static"))
  private val views   = cachedResource(Config("/pages", "/"))

  //case class JData(data: String)

  //implicit val formats = DefaultFormats
  //implicit val jDataReader = new Reader[JData] {
  //  def read(value: JValue): JData = value.extract[JData]
  //}
  //implicit val jDataDecoder = jsonOf[JData]

  val service: HttpService = HttpService {

    // retriews static resources
    case r @ GET -> _ if r.pathInfo.startsWith("/static") => static(r)

    // base route shows index.html
    case r @ GET -> _ if r.pathInfo.endsWith("/") => service(r.withPathInfo(r.pathInfo + "index.html"))

    // shows all html pages in pages
    case r @ GET -> _ =>
      val rr = if (r.pathInfo.contains('.')) r else r.withPathInfo(r.pathInfo + ".html")
      views(rr)

    // TODO
    //case r @ GET -> Root / "websocket" =>
    //  // Send a ping every second
    //  val src = time.awakeEvery(1.seconds).map(d => Text("Delay -> " + d))
    //  WS(Exchange(src, Process.halt))

    //case req @ POST -> Root / "getChunkCounts" =>
    //  req.as[JData].flatMap(x => Ok(Processing.getChunkCounts(x.data)))


  }

  private def cachedResource(config: Config): HttpService = {
    val cachedConfig = config.copy(cacheStrategy = staticcontent.MemoryCache())
    staticcontent.resourceService(cachedConfig)
  }

}
