package util

import poem.PoemRule

object RulesUtil {
  private val WordLiteralRegex = "([a-z][|]?)+"
  private val RuleReferenceRegex = "(<[A-Z]+>[|]?)+"
  private val PoemRuleRegex1 = s"[A-Z]+: $WordLiteralRegex"
  private val PoemRuleRegex2 = s"[A-Z]+: $RuleReferenceRegex"
  private val PoemRuleRegex3 = s"[A-Z]+: ($WordLiteralRegex )?$RuleReferenceRegex"

  def parseRules(lines: List[String]): List[PoemRule] = {
    val badLines = lines.filter(l => l.isEmpty || (!l.matches(PoemRuleRegex1) && !l.matches(PoemRuleRegex2) && !l.matches(PoemRuleRegex3)))
    badLines.foreach(l => LoggerUtil.logError("Bad Rule from source file: " + l))
    lines.diff(badLines).map(PoemRule)
  }
}
