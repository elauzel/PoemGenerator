package poem

import common.AbstractTestBase
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test
import scala.collection.immutable.Map

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
  def unpackTopLevelRule_justWords_returnsPoem(): Unit = {
    val rule = PoemRule("Poem", List("just", "plain", "words"))
    assertEquals("just plain words", Poem.unpackTopLevelRule(rule, Map.empty))
  }

  @Test
  def unpackTopLevelRule_wordsWithChoices_returnsPoem(): Unit = {
    val rule = PoemRule("Poem", List("I|You", "love", "cake|fruit"))
    val actual = Poem.unpackTopLevelRule(rule, Map.empty)
    assertTrue(List("I love cake", "I love fruit", "You love cake", "You love fruit").contains(actual))
  }

  @Test
  def unpackTopLevelRule_wordsWithLinebreak_returnsPoem(): Unit = {
    val rule = PoemRule("Poem", List("one", "$LINEBREAK", "two"))
    val actual = Poem.unpackTopLevelRule(rule, Map.empty)
    assertEquals("one" + NEWLINE + "two", actual)
  }

  @Test
  def unpackTopLevelRule_wordsWithEnd_returnsPoem(): Unit = {
    val rule = PoemRule("Poem", List("one", "two", "$END", "three"))
    val actual = Poem.unpackTopLevelRule(rule, Map.empty)
    assertEquals("one two", actual)
  }

  @Test
  def unpackTopLevelRule_wordsWithReference_missingRule_throwsException(): Unit = {
    val rule = PoemRule("Poem", List("I", "jumped", "<PREPOSITION>", "everything"))
    assertThrows[RuntimeException](() => Poem.unpackTopLevelRule(rule, Map.empty))
  }

  @Test
  def unpackTopLevelRule_wordsWithReference_ruleInMap_returnsPoem(): Unit = {
    val rule = PoemRule("Poem", List("I", "jumped", "<PREPOSITION>", "everything"))
    val prepRule = Poem.parseRules(List("PREPOSITION: above|across|against|along|among|around|before|behind|beneath|beside|between|beyond|during|inside|onto|outside|under|underneath")).head
    val actual = Poem.unpackTopLevelRule(rule, Map("PREPOSITION" -> prepRule))
    assertTrue(actual.matches("I jumped [a-z]+ everything"))
  }
}
