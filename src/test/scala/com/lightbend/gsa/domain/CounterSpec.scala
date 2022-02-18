package com.lightbend.gsa.domain

import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.google.protobuf.empty.Empty
import com.lightbend.gsa
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.lightbend.gsa.{ DecreaseValue, IncreaseValue, ResetValue }

class CounterSpec
    extends AnyWordSpec
    with Matchers {

  "Counter" must {

    "handle command Increase" in {
      val testKit = CounterTestKit(new Counter(_))

      val result1 = testKit.increase(IncreaseValue(value = 1))
      result1.reply shouldBe Empty.defaultInstance

      // one more time
      val result2 = testKit.increase(IncreaseValue(value = 1))
      result2.reply shouldBe Empty.defaultInstance
      testKit.currentState().value shouldBe 2
    }
    // end::sample-unit-test[]

    "handle command Decrease" in {
      val testKit = CounterTestKit(new Counter(_))

      val result1 = testKit.increase(IncreaseValue(value = 1))
      result1.reply shouldBe Empty.defaultInstance
      testKit.currentState().value shouldBe 1

      val result2 = testKit.decrease(DecreaseValue(value = 1))
      result2.reply shouldBe Empty.defaultInstance
      testKit.currentState().value shouldBe 0
    }

    "handle command Reset" in {
      val testKit = CounterTestKit(new Counter(_))

      val result1 = testKit.increase(IncreaseValue(value = 1))
      result1.reply shouldBe Empty.defaultInstance
      testKit.currentState().value shouldBe 1

      val resetResult = testKit.reset(ResetValue())
      resetResult.reply shouldBe Empty.defaultInstance
      testKit.currentState().value shouldBe 0
    }

  }
}
