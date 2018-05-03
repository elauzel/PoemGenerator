package util

import scala.io.Source

object FileUtil {
  def readResource(filename: String): List[String] =
    try
      Source.fromResource(filename).getLines().toList
    catch {
      case e: Exception =>
        LoggerUtil.logError("Error reading file: " + e.getMessage)
        List()
    }
}
