package poem

import common.AbstractTestBase
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.Test

class PoemRuleTest extends AbstractTestBase {
  @Test
  def create_invalidText_throwsException(): Unit = {
    assertThrows[RuntimeException](() => PoemRule.create(""))
    assertThrows[RuntimeException](() => PoemRule.create("name"))
    assertThrows[RuntimeException](() => PoemRule.create("part1 part2"))
    assertThrows[RuntimeException](() => PoemRule.create("name part1 part2"))
    assertThrows[RuntimeException](() => PoemRule.create("name part1a|part1b part2"))
    assertThrows[RuntimeException](() => PoemRule.create("name part1a|part1b part2 <part3>"))
    assertThrows[RuntimeException](() => PoemRule.create("name part1a|part1b part2 <part3> $part4"))
  }

  @Test
  def create_validText_returnsRule(): Unit = {
    val rule = PoemRule.create("name: part1a|part1b part2 <part3> $part4")
    assertTrue(rule.ruleName == "name")
    assertEquals(List("part1a|part1b", "part2", "<part3>", "$part4"), rule.ruleParts)
  }
}
