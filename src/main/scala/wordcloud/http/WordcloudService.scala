package wordcloud.http

import org.json4s._
import org.json4s.jackson.JsonMethods._

import org.http4s._
import org.http4s.dsl._
import org.http4s.json4s.jackson._

import wordcloud.Processing


object WordcloudService {

  case class JData(data: String)

  implicit val formats = DefaultFormats
  implicit val jDataReader = new Reader[JData] {
    def read(value: JValue): JData = value.extract[JData]
  }
  implicit val jDataDecoder = jsonOf[JData]

  val service = HttpService {
    case GET -> Root =>
      Ok("Hallo Welt!")
    case req @ POST -> Root / "getChunkCounts" =>
      req.as[JData].flatMap(x => Ok(Processing.getChunkCounts(x.data)))
  }
}
