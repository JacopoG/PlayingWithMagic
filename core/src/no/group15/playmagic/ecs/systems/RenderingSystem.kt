package no.group15.playmagic.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.*
import ktx.graphics.*
import no.group15.playmagic.ecs.components.TextureComponent
import no.group15.playmagic.ecs.components.TransformComponent


class RenderingSystem(
	priority: Int,
	private val viewport: Viewport,
	private val batch: SpriteBatch
) : EntitySystem(
	priority
) {

	private lateinit var entities: ImmutableArray<Entity>
	private val transformMapper = mapperFor<TransformComponent>()
	private val textureMapper = mapperFor<TextureComponent>()


	override fun addedToEngine(engine: Engine) {
		entities = engine.getEntitiesFor(
			allOf(TransformComponent::class, TextureComponent::class).get()
		)
	}

	override fun update(deltaTime: Float) {
		viewport.apply()
		batch.use(viewport.camera) {

			// Draw entities
			for (entity in entities) {
				val transform = transformMapper[entity]
				val texture = textureMapper[entity]

				batch.draw(
					texture.src,
					transform.boundingBox.x, transform.boundingBox.y,
					texture.origin.x, texture.origin.y,
					transform.boundingBox.width, transform.boundingBox.height,
					transform.scale.x, transform.scale.y,
					transform.rotation
				)
			}
		}
	}
}
