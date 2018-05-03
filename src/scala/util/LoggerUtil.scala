package util

import java.time.LocalDateTime

object LoggerUtil {
  private val NEW_LINE = System.lineSeparator

  def logError(message: String): Unit = {
    System.err.println(LocalDateTime.now + "\t" + message + NEW_LINE)
  }
}
