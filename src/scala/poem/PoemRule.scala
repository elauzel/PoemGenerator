package poem

case class PoemRule(ruleName: String, ruleParts: List[String]) {
}

object PoemRule {
  def create(ruleText: String): PoemRule = if (ruleText.matches("[A-Za-z]+: (.*[ ]?)+")) {
    PoemRule(ruleText.substring(0, ruleText.indexOf(':')), ruleText.substring(ruleText.indexOf(':') + 2).split(' ').toList)
  } else {
    throw new RuntimeException("Improperly formatted text for poem rule:\t" + ruleText)
  }
}
