package poem

case class PoemRule(ruleName: String, ruleParts: List[String]) {
}

object PoemRule {
  def create(ruleText: String): PoemRule = PoemRule(ruleText.substring(0, ruleText.indexOf(':')), ruleText.substring(ruleText.indexOf(':') + 2).split(' ').toList)
}
