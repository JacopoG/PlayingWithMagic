package no.group15.playmagic.ui.controllers

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.freetype.*
import no.group15.playmagic.PlayMagic
import no.group15.playmagic.ecs.engineFactory
import no.group15.playmagic.ui.AppState
import no.group15.playmagic.ui.views.GameView
import no.group15.playmagic.ui.views.MainMenuView
import no.group15.playmagic.utils.assets.*


class GamePresenter(
	private val appContext: PlayMagic,
	private val batch: SpriteBatch,
	private val inputMultiplexer: InputMultiplexer
) : AppState {

	private val engineViewHeight = 10f
	private val engineViewport = ExtendViewport(
		4 / 3f * engineViewHeight, engineViewHeight, 21 / 9f * engineViewHeight, engineViewHeight
	)
	private val assetManager = AssetManager()
	private lateinit var engine: Engine
	private lateinit var gameView: GameView


	override fun create() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)

		assetManager.registerFreeTypeFontLoaders()
		assetManager.load(FontAssets.DRAGONFLY_25.desc)
		loadAssets<GameAssets>(assetManager)
		loadAssets<VirtualStickAssets>(assetManager)
		assetManager.finishLoading()

		engine = engineFactory(engineViewport, batch, assetManager)
		gameView = GameView(assetManager, inputMultiplexer)
	}

	override fun update(deltaTime: Float) {
		gameView.update(deltaTime)

		engine.update(deltaTime)

		gameView.render(batch)
	}

	override fun resize(width: Int, height: Int) {
		engineViewport.update(width, height, false)
		gameView.resize(width, height)
	}

	override fun back() {
		appContext.appState = MainMenuView(appContext, batch, inputMultiplexer)
	}

	override fun pause() {
	}

	override fun resume() {
	}

	override fun dispose() {
		gameView.dispose()
		assetManager.dispose()
	}
}
