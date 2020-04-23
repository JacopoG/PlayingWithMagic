package no.group15.playmagic.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.*
import no.group15.playmagic.ecs.components.TimerComponent
import no.group15.playmagic.events.BombTimeoutEvent


class TimerSystem(
	priority: Int
) : IteratingSystem(
	Family.all(TimerComponent::class.java).get(),
	priority
) {

	private val timerMapper = mapperFor<TimerComponent>()
	private val signal = Signal<BombTimeoutEvent>()


	override fun processEntity(entity: Entity, deltaTime: Float) {
		val timer = timerMapper.get(entity)

		timer.timeLeft -= deltaTime

		if (timer.timeLeft <= 0) {
			entity.remove(TimerComponent::class.java)
			signal.dispatch(BombTimeoutEvent(entity))
		}
	}

	fun registerListener(listener: Listener<BombTimeoutEvent>) = signal.add(listener)
}
