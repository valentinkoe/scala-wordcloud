package webapp

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.staticcontent
import org.http4s.server.staticcontent.ResourceService.Config
import org.http4s.server.websocket._
import org.http4s.websocket.WebsocketBits.{Text, WebSocketFrame}

import scalaz.concurrent.Task
import scalaz.stream.async.unboundedQueue
import scalaz.stream.Process
import scalaz.stream.Exchange

import wordcloud.Processing


object Routes {

  private def cachedResource(config: Config): HttpService = {
    val cachedConfig = config.copy(cacheStrategy = staticcontent.MemoryCache())
    staticcontent.resourceService(cachedConfig)
  }

  private val static = cachedResource(Config("/static", "/static"))
  private val pages = cachedResource(Config("/pages", "/"))

  object LangQueryParamMatcher extends QueryParamDecoderMatcher[String]("lang")
  object AdjQueryParamMatcher extends QueryParamDecoderMatcher[Boolean]("adj")

  val service: HttpService = HttpService {

    case r @ GET -> Root / "tokens" :? LangQueryParamMatcher(lang)
                                       +& AdjQueryParamMatcher(adj) =>
      val q = unboundedQueue[WebSocketFrame]

      val src = q.dequeue.collect {
        case Text(msg, _) => msg
      }.flatMap(x => tokenStream(x.iterator, lang, adj)).map(Text(_))

      WS(Exchange(src, q.enqueue))

    case r @ GET -> Root / "chunks" :? LangQueryParamMatcher(lang) =>
      val q = unboundedQueue[WebSocketFrame]

      val src = q.dequeue.collect {
        case Text(msg, _) => msg
      }.flatMap(x => chunkStream(x.iterator, lang)).map(Text(_))

      WS(Exchange(src, q.enqueue))

    // routes to retrieve static resources
    case r @ GET -> _ if r.pathInfo.startsWith("/static") => static(r)

    // base route shows index.html
    case r @ GET -> _ if r.pathInfo.endsWith("/") => service(r.withPathInfo(r.pathInfo + "index.html"))

    // shows all html pages in pages folder
    case r @ GET -> _ =>
      val rr = if (r.pathInfo.contains('.')) r else r.withPathInfo(r.pathInfo + ".html")
      pages(rr)

  }

  private def toStream[T](iter: Iterator[T]): Process[Nothing, T] = {
    Process.unfold(iter) { it =>
      if (it.hasNext) Some((it.next, it))
      else None
    }
  }

  private def tokenStream(text: Iterator[Char], lang: String, adj: Boolean): Process[Task, String] =
    toStream(Processing.getTokens(text, lang, adj=adj))

  private def chunkStream(text: Iterator[Char], lang: String): Process[Task, String] =
    toStream(Processing.getChunks(text, lang))

}