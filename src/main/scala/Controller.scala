import java.util

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Controller {

  def getMessage(method: String, uri: String, body: String): ListBuffer[String] = {
    if (method.equals("GET") && Parser.lengthUri(uri) > 1) {
      println(uri)
      Parser.parseSearchRequest(uri)
    }
    else if (method.equals("POST")) {
      var buf = new mutable.ListBuffer[String]
      buf += "add"
      buf += body
    }
    else {
      new mutable.ListBuffer[String]
    }
  }

  def checkMessage(listBuffer: ListBuffer[String]): Unit = {
    if (listBuffer.isEmpty) System.err.println("Invalid Request")
  }

  def checkResult(result: util.List[Book]): Unit = {
    if (result.isEmpty) println("No entry was found")
    else println(Parser.toJson(result))
  }

}

