package no.group15.playmagic.ecs.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.mapperFor
import no.group15.playmagic.ecs.components.TextureComponent
import no.group15.playmagic.ecs.components.TransformComponent


class RenderingSystem(
	priority: Int,
	private val batch: SpriteBatch
) : EntitySystem(
	priority
) {

	private lateinit var entities: ImmutableArray<Entity>
	private val transformMapper = mapperFor<TransformComponent>()
	private val textureMapper = mapperFor<TextureComponent>()

	override fun addedToEngine(engine: Engine) {
		// May need to re-fetch on update if list changes
		entities = engine.getEntitiesFor(
			Family.all(TransformComponent::class.java, TextureComponent::class.java).get()
		)
	}

	override fun update(deltaTime: Float) {
		// TODO draw level

		// Draw entities
		for (entity in entities) {
			val transform = transformMapper.get(entity)
			val texture = textureMapper.get(entity)

			batch.draw(
				texture.src,
				transform.position.x, transform.position.y,
				texture.origin.x, texture.origin.y,
				texture.size.x, texture.size.y,
				transform.scale.x, transform.scale.y,
				transform.rotation
			)
		}
	}
}
