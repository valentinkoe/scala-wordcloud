package webapp

import org.http4s._
import org.http4s.MediaType._
import org.http4s.headers.`Content-Type`
import org.http4s.dsl._
import org.http4s.server.staticcontent
import org.http4s.server.staticcontent.ResourceService.Config
import scalaz.concurrent.Task
import scalaz.stream.Process
import scalaz.stream.io.toInputStream

import wordcloud.Processing

import scala.io.Source

import scala.concurrent.duration._
import scalaz.stream.time
import scalaz.concurrent.Task
import scalaz.concurrent.Strategy.DefaultTimeoutScheduler

object Routes {

  private def cachedResource(config: Config): HttpService = {
    val cachedConfig = config.copy(cacheStrategy = staticcontent.MemoryCache())
    staticcontent.resourceService(cachedConfig)
  }
  // Get the static content
  private val static  = cachedResource(Config("/static", "/static"))
  private val pages   = cachedResource(Config("/pages", "/"))

  object LangQueryParamMatcher extends QueryParamDecoderMatcher[String]("lang")
  object AdjQueryParamMatcher extends QueryParamDecoderMatcher[Boolean]("adj")
  object NounQueryParamMatcher extends QueryParamDecoderMatcher[Boolean]("noun")

  val service: HttpService = HttpService {

    case GET -> Root / "streaming" =>
      // Its also easy to stream responses to clients
      Ok(dataStream(100))

    case r @ POST -> Root / "tokens" :? LangQueryParamMatcher(lang)
                                       +& AdjQueryParamMatcher(adj)
                                       +& NounQueryParamMatcher(noun) =>
      val inp = Source.fromInputStream(toInputStream(r.body), "UTF-8")
      Ok(tokenStream(inp, lang, adj, noun)).withContentType(Some(`Content-Type`(`text/plain`)))

    // retrieves static resources
    case r @ GET -> _ if r.pathInfo.startsWith("/static") => static(r)

    // base route shows index.html
    case r @ GET -> _ if r.pathInfo.endsWith("/") => service(r.withPathInfo(r.pathInfo + "index.html"))

    // shows all html pages in pages
    case r @ GET -> _ =>
      val rr = if (r.pathInfo.contains('.')) r else r.withPathInfo(r.pathInfo + ".html")
      pages(rr)

  }

  def toStream[T](iter: Iterator[T]): Process[Nothing, T] = {
    Process.unfold(iter) { it =>
      if (it.hasNext) Some((it.next, it))
      else None
    }
  }

  def tokenStream(text: Iterator[Char], lang: String, adj: Boolean=false, noun: Boolean=true): Process[Task, String] =
    toStream(Processing.getTokens(text, lang, adj, noun))

  def dataStream(n: Int): Process[Task, String] = {
    implicit def defaultScheduler = DefaultTimeoutScheduler
    val interval = 1.second
    time.awakeEvery(interval)
      .map(_ => s"Current system time: ${System.currentTimeMillis()} ms\n")
      .take(n)
  }
}
