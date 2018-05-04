package poem

import common.AbstractTestBase
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test

class PoemTest extends AbstractTestBase {
  private val NEWLINE = System.lineSeparator()
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
  def parseRules_emptyLines_returnsEmpty(): Unit = assertTrue(Poem.parseRules(List()).isEmpty)

  @Test
  def parseRules_someLinesInvalid_returnsRulesForGoodLines(): Unit = {
    val invalidRule1 = ""
    val invalidRule2 = "pronoun: my|your|his|her <NOUN>|<ADJECTIVE>"
    val invalidRule3 = "PRONOUN: my|your|his|her <NOUN>||<ADJECTIVE>"
    val invalidRule4 = "PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>&BAD&"
    val invalidRule5 = "PRONOUN: "
    assertEquals(validRules.map(PoemRule.create), Poem.parseRules(validRules ::: List(invalidRule1, invalidRule2, invalidRule3, invalidRule4, invalidRule5)))
  }

  @Test
  def parseRules_allLinesValid_returnsRules(): Unit = {
    val expectedRules = validRules.map(PoemRule.create)
    val actualRules = Poem.parseRules(validRules)
    assertEquals(expectedRules, actualRules)
    assertEquals(validRules.size, actualRules.size)
  }

  @Test
  def buildFromText_justWords_returnsPoem(): Unit = assertEquals("just plain words", Poem.buildFromText(List("POEM: just plain words")))

  @Test
  def buildFromText_wordsWithChoices_returnsPoem(): Unit =
    assertTrue(List("I love cake", "I love fruit", "You love cake", "You love fruit").contains(Poem.buildFromText(List("POEM: I|You love cake|fruit"))))

  @Test
  def buildFromText_wordsWithLinebreak_returnsPoem(): Unit = assertEquals("one" + NEWLINE + "two", Poem.buildFromText(List("POEM: one $LINEBREAK two")))

  @Test
  def buildFromText_wordsWithEnd_returnsPoem(): Unit = assertEquals("one two", Poem.buildFromText(List("POEM: one two $END three")))

  @Test
  def buildFromText_wordsWithReference_missingRule_throwsException(): Unit = assertThrows[RuntimeException](() => Poem.buildFromText(List("POEM: I jumped <PREPOSITION> everything")))

  @Test
  def buildFromText_wordsWithReference_ruleInMap_returnsPoem(): Unit = {
    val rules = List("POEM: I jumped <PREPOSITION> everything $LINEBREAK EVERYTHING", "PREPOSITION: above|across|against|along|among|around|before|behind|beneath|beside|between|beyond")
    assertTrue(Poem.buildFromText(rules).matches("I jumped [a-z]+ everything" + NEWLINE + "EVERYTHING"))
  }
}
