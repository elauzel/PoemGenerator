import poem.Poem
import util.FileUtil

object PoemGenerator {
  def main(args: Array[String]): Unit = {
    val lines = FileUtil.readResource("rules.txt")
    val poem = Poem.buildFromText(lines)
    println(poem)
  }
}
