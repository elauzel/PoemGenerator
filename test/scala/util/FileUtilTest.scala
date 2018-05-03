package util

import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test

class FileUtilTest {
  private val VALID_FILE = "rules.txt"

  @Test
  def readResource_invalidFiles_returnEmpty(): Unit = {
    assertTrue(FileUtil.readResource("*").isEmpty)
    assertTrue(FileUtil.readResource("nonexistant.file").isEmpty)
    assertTrue(FileUtil.readResource("empty.txt").isEmpty)
  }

  @Test
  def readResource_validFile_returnsLines(): Unit = {
    assertEquals(7, FileUtil.readResource(VALID_FILE).size)
  }
}
