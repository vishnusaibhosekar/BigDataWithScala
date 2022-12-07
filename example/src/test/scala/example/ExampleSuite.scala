package example

import munit.*
import scala.util.Random

class ExampleSuite extends FunSuite:

  test("'Example.add' returns 0 when given 0 and 0") {
    assertEquals(Example.add(0,0), 0, "'Example.add(0,0)' did not return 0.")
  }

  test("'Example.add' returns -1 when given 2 and -3") {
    assertEquals(Example.add(2,-3), -1, "'Example.add(2,-3)' did not return -1.")
  }
  
end ExampleSuite