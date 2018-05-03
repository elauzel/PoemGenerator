package util

import poem.PoemRule

object RulesUtil {
  private val WordLiteralRegex = "[a-z]+"
  private val RuleReferenceRegex = "(<[A-Z]+>)"
  private val EndRegex = "[$]END"
  private val LinebreakRegex = "[$]LINEBREAK"
  private val PoemPartRegex = s"(($WordLiteralRegex|$RuleReferenceRegex|$EndRegex|$LinebreakRegex)[|]?)+"
  private val PoemRegex = s"[A-Z]+: ($PoemPartRegex[ ]?)+"

  def parseRules(lines: List[String]): List[PoemRule] = {
    val badLines = lines.filter(l => l.isEmpty || !l.matches(PoemRegex))
    badLines.foreach(l => LoggerUtil.logError("Bad Rule from source file: " + l))
    lines.diff(badLines).map(PoemRule.create)
  }
}
