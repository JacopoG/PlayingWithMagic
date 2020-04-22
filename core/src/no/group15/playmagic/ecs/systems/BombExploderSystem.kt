package no.group15.playmagic.ecs.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import no.group15.playmagic.ecs.components.ExploderComponent
import no.group15.playmagic.ecs.components.TextureComponent
import no.group15.playmagic.ecs.components.TimerComponent
import no.group15.playmagic.ecs.components.TransformComponent
import no.group15.playmagic.utils.assets.GameAssets


class BombExploderSystem(
	priority: Int,
	private val assetManager: AssetManager
): EntitySystem(
	priority
) {

	private lateinit var entities: ImmutableArray<Entity>
	private val timer = mapperFor<TimerComponent>()
	private val texture = mapperFor<TextureComponent>()
	private val transform = mapperFor<TransformComponent>()
	private val exploder = mapperFor<ExploderComponent>()

	override fun addedToEngine (engine: Engine) {
		entities = engine.getEntitiesFor(
			allOf(ExploderComponent::class, TimerComponent::class, TransformComponent::class, TextureComponent::class).get()
		)
	}

	override fun update(deltaTime: Float) {
		var explosionTexture: TextureRegion = TextureRegion(assetManager.get<Texture>(GameAssets.EXPLOSION.desc.fileName))

		for (entity in entities) {
			if (entity[timer]!!.timeLeft <= 0) {

				entity[texture]!!.src = explosionTexture
				entity[exploder]!!.isExploded = true

			}
		}
	}
}

// BombExploder System Test code
fun testBomb(engine: PooledEngine, assetManager: AssetManager) {
	val bomb = createBomb(engine, assetManager)
	engine.addEntity(bomb)
	engine.addSystem(BombExploderSystem(0, assetManager))
	engine.addSystem(TimerSystem(0))
}

// BombExploder System Test code
fun createBomb(engine: PooledEngine, assetManager: AssetManager): Entity {
	val entity = engine.createEntity()
	val transform = engine.createComponent(TransformComponent::class.java)
	val exploder = engine.createComponent(ExploderComponent::class.java)
	val timer = engine.createComponent(TimerComponent::class.java)
	val texture = engine.createComponent(TextureComponent::class.java)
	//val assetManager: AssetManager = AssetManager()

	transform.position.set(0f, 0f)
	//transform.scale. = ImmutableVector2(2f, 2f)

	timer.timeLeft = 3f

	exploder.range = 5f

	texture.src = TextureRegion(assetManager.get<Texture>(GameAssets.BOMB.desc.fileName))

	entity.add(transform)
	entity.add(timer)
	entity.add(exploder)
	entity.add(texture)

	return entity
}
