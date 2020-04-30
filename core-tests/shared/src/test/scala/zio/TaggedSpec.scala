package zio

import zio.test.Assertion._
import zio.test.TestAspect.exceptDotty
import zio.test._

object TaggedSpec extends ZIOBaseSpec {

  def spec = suite("TaggedSpec")(
    testM("tags can be derived for polymorphic services") {
      val result = typeCheck {
        """
            trait Producer[R, K, V]

            def test[R: Tagged, K: Tagged, V: Tagged]: Boolean = {
              val _ = implicitly[Tagged[Producer[R, K, V]]]
              true
            }
            """
      }
      assertM(result)(isRight(isUnit))
    } @@ exceptDotty,
    zio.test.test("the tag for a self intersection type (X with X) is the same as for the type itself (X)") {
      assert(implicitly[Tagged[String with String]].tag)(equalTo(implicitly[Tagged[String]].tag))
    } @@ exceptDotty
  )
}
