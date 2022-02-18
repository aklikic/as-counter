package com.lightbend.gsa.domain

import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.akkaserverless.scalasdk.valueentity.ValueEntityContext
import com.google.protobuf.empty.Empty
import com.lightbend.gsa
import com.lightbend.gsa.CurrentCounter

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

/** A value entity. */
class Counter(context: ValueEntityContext) extends AbstractCounter {
  override def emptyState: CounterState = CounterState()

  override def increase(currentState: CounterState, command: gsa.IncreaseValue): ValueEntity.Effect[Empty] =
    if (command.value < 0) // <1>
      effects.error(s"Increase requires a positive value. It was [${command.value}].")
    else {
      val newState = currentState.copy(value = currentState.value + command.value) // <2>
      effects
        .updateState(newState) // <3>
        .thenReply(Empty.defaultInstance) // <4>
    }

  override def decrease(currentState: CounterState, command: gsa.DecreaseValue): ValueEntity.Effect[Empty] =
    if (command.value < 0) effects.error(s"Increase requires a positive value. It was [${command.value}].")
    else
      effects
        .updateState(currentState.copy(value = currentState.value - command.value))
        .thenReply(Empty.defaultInstance)

  override def reset(currentState: CounterState, resetValue: gsa.ResetValue): ValueEntity.Effect[Empty] =
    effects.updateState(CounterState()).thenReply(Empty.defaultInstance)

  override def getCurrentCounter(currentState: CounterState, getCounter: gsa.GetCounter): ValueEntity.Effect[gsa.CurrentCounter] =
    effects.reply(CurrentCounter(currentState.value))

}

