package poem

import util.LoggerUtil

import scala.util.Random

case class Poem() {
}

object Poem {
  private val WordLiteralRegex = "[A-Za-z]+"
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

  def buildFromText(lines: List[String]): String = {
    val rules = parseRules(lines)
    val poemStructure = rules.filter(_.ruleName == "POEM").head
    val nameRuleMap = rules.filter(_ != poemStructure).map(r => r.ruleName -> r).toMap
    unpackRuleWithReferences(poemStructure, nameRuleMap)
      .split(System.lineSeparator())
      .map(_.trim)
      .mkString(System.lineSeparator())
  }

  private def translateSubpartIntoWord(rulePart: String, rulesByName: Map[String, PoemRule]): String = {
    val choice = Random.shuffle(rulePart.split("[|]").toList).head
    if (choice.matches("<[A-Z]+>")) {
      val referencedParts = rulesByName.get(choice.substring(1, choice.length - 1))
      unpackRuleWithReferences(referencedParts.head, rulesByName)
    }
    else if (choice.matches("[A-Za-z]+")) choice
    else if (choice == "$LINEBREAK") System.lineSeparator()
    else if (choice == "$END") ""
    else throw new RuntimeException(s"Error during rule conversion: bad subpart $choice in rule part $rulePart")
  }

  private def convertRulePartToWords(subpartPrevious: String, subpartCurrent: String, subpartsRemaining: List[String], rulesByName: Map[String, PoemRule]): List[String] =
    if (subpartCurrent.trim.isEmpty) List("")
    else {
      val tempWord = translateSubpartIntoWord(subpartCurrent, rulesByName)
      if (subpartsRemaining.isEmpty || subpartsRemaining.head == "$END")
        List(tempWord)
      else {
        val finalWord =
          if (subpartPrevious != "$LINEBREAK" && subpartCurrent != "$LINEBREAK" && subpartsRemaining.head != "$LINEBREAK" && subpartPrevious != "$END" && subpartCurrent != "$END"
            && subpartsRemaining.head != "$END")
            List(tempWord + ' ')
          else
            List(tempWord)
        finalWord ::: convertRulePartToWords(subpartCurrent, subpartsRemaining.head, subpartsRemaining.slice(1, subpartsRemaining.size), rulesByName)
      }
    }

  private def unpackRuleWithReferences(rule: PoemRule, rulesByName: Map[String, PoemRule]): String =
    convertRulePartToWords("", rule.ruleParts.head, rule.ruleParts.slice(1, rule.ruleParts.size), rulesByName).mkString("")
}
