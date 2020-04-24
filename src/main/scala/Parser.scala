import java.util

import com.google.gson.GsonBuilder

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Parser {
  val gson = new GsonBuilder().setPrettyPrinting().create

  def toJson(res: util.List[Book]): String = gson.toJson(res)

  def parseAddRequest(body: String): Book = fromJson(body)

  def fromJson(sb: String): Book = gson.fromJson(sb, classOf[Book])

  def parseSearchRequest(uri: String): ListBuffer[String] = {
    var buf = new mutable.ListBuffer[String]
    val term = uri.split("=")(0)
    val name = uri.split("=")(1)
    if (term.equals("isbn")) {
      buf += term
      buf += name
    }
    else {
      buf += "term"
      buf += name
    }
  }

  def lengthUri(uri: String): Int = {
    uri.split("=").length
  }
}

