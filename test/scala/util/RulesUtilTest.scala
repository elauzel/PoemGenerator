package util

import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test
import poem.PoemRule

class RulesUtilTest {
  private val validRules = List("TEST: blahblah", "THIS: that|other", "SOME: <THING>", "PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>")

  @Test
  def parseRules_emptyLines_returnsEmpty(): Unit = assertTrue(RulesUtil.parseRules(List()).isEmpty)

  @Test
  def parseRules_someLinesInvalid_returnsRulesForGoodLines(): Unit = {
    val invalidRule1 = ""
    val invalidRule2 = "pronoun: my|your|his|her <NOUN>|<ADJECTIVE>"
    val invalidRule3 = "PRONOUN: my|your|his|her <NOUN>||<ADJECTIVE>"
    val invalidRule4 = "PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>&BAD&"
    val invalidRule5 = "PRONOUN: "
    assertEquals(validRules.map(PoemRule), RulesUtil.parseRules(validRules ::: List(invalidRule1, invalidRule2, invalidRule3, invalidRule4, invalidRule5)))
  }

  @Test
  def parseRules_allLinesValid_returnsRules(): Unit = assertEquals(validRules.map(PoemRule), RulesUtil.parseRules(validRules))
}
