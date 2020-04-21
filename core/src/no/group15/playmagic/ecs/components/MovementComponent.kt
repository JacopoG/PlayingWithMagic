package no.group15.playmagic.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool


class MovementComponent : Component, Pool.Poolable {

//	var velocity: Vector2 = Vector2(0F, 0F)
	var moveRight = false
	var moveLeft = false
	var moveUp = false
	var moveDown = false
	var playerId = 0

	// Constants
//	val acceleration = 1f
	val maxSpeed = 2f


	override fun reset() {
		playerId = 0
//		velocity.set(0f, 0f)
	}
}
