package util

import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test
import poem.PoemRule

class RulesUtilTest {
  private val validRules = List(
    "TEST: blahblah",
    "THIS: that|other",
    "SOME: <THING>",
    "POEM: <LINE> <LINE> <LINE> <LINE> <LINE>",
    "LINE: <NOUN>|<PREPOSITION>|<PRONOUN> $LINEBREAK",
    "ADJECTIVE: black|white|dark|light|bright|murky|muddy|clear <NOUN>|<ADJECTIVE>|$END",
    "NOUN: heart|sun|moon|thunder|fire|time|wind|sea|river|flavor|wave|willow|rain|tree|flower|field|meadow|pasture|harvest|water|father|mother|brother|sister <VERB>|<PREPOSITION>|$END",
    "PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>",
    "VERB: runs|walks|stands|climbs|crawls|flows|flies|transcends|ascends|descends|sinks <PREPOSITION>|<PRONOUN>|$END",
    "PREPOSITION: above|across|against|along|among|around|before|behind|beneath|beside|between|beyond|during|inside|onto|outside|under|underneath|upon|with|without|through <NOUN>|<PRONOUN>|<ADJECTIVE>")

  @Test
  def parseRules_emptyLines_returnsEmpty(): Unit = assertTrue(RulesUtil.parseRules(List()).isEmpty)

  @Test
  def parseRules_someLinesInvalid_returnsRulesForGoodLines(): Unit = {
    val invalidRule1 = ""
    val invalidRule2 = "pronoun: my|your|his|her <NOUN>|<ADJECTIVE>"
    val invalidRule3 = "PRONOUN: my|your|his|her <NOUN>||<ADJECTIVE>"
    val invalidRule4 = "PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>&BAD&"
    val invalidRule5 = "PRONOUN: "
    assertEquals(validRules.map(PoemRule.create), RulesUtil.parseRules(validRules ::: List(invalidRule1, invalidRule2, invalidRule3, invalidRule4, invalidRule5)))
  }

  @Test
  def parseRules_allLinesValid_returnsRules(): Unit = {
    val expectedRules = validRules.map(PoemRule.create)
    val actualRules = RulesUtil.parseRules(validRules)
    assertEquals(expectedRules, actualRules)
    assertEquals(validRules.size, actualRules.size)
  }
}
