import util.{FileUtil, RulesUtil}

object PoemGenerator {
  def main(args: Array[String]): Unit = {
    val lines = FileUtil.readResource("rules.txt")
    val rules = RulesUtil.parseRules(lines)
    print()
  }
}
