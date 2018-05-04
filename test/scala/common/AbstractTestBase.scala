package common

import org.junit.Assert

abstract class AbstractTestBase {
  def assertThrows[T <: Throwable](func: () => Any): Unit = {
    try {
      func()
      Assert.fail()
    } catch {
      case _: T =>
    }
  }
}
