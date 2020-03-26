package no.group15.playmagic.ui.views

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.collections.*
import ktx.graphics.use
import no.group15.playmagic.ui.views.widgets.MessagesWidget
import no.group15.playmagic.ui.views.widgets.VirtualStickWidget
import no.group15.playmagic.ui.views.widgets.Widget
import no.group15.playmagic.utils.assets.*


class GameView(assetManager: AssetManager, inputMultiplexer: InputMultiplexer) {

	private val widgets = gdxArrayOf<Widget>()
	private val viewHeight = 720f
	private val	viewport = ExtendViewport(
		4 / 3f * viewHeight, viewHeight, 21 / 9f * viewHeight, viewHeight
	)

	private var font: BitmapFont? = null


	init {
	    // Setup widgets based on platform and config
		val stick = VirtualStickWidget(
			viewport,
			textureRegionFactory(assetManager, VirtualStickAssets.PAD_REGION),
			textureRegionFactory(assetManager, VirtualStickAssets.HANDLE_REGION),
			170f,
			inputMultiplexer
		)
		font = BitmapFont()
		stick.stickValueFont = font
		widgets.add(stick)
		widgets.add(MessagesWidget(
			33f,
			assetManager.get(FontAssets.DRAGONFLY_25.desc.fileName)
		))
		widgets.shrink()
	}

	fun update(deltaTime: Float) {
		for (widget in widgets) {
			widget.update(deltaTime)
		}
	}

	fun render(batch: SpriteBatch) {
		viewport.apply()
		batch.use(viewport.camera) {
			for (widget in widgets) {
				widget.render(batch)
			}
		}
	}

	fun resize(width: Int, height: Int) {
		viewport.update(width, height, true)
		for (widget in widgets) {
			widget.resize(viewport.worldWidth, viewport.worldHeight)
		}
	}

	fun dispose() {
		for (widget in widgets) {
			widget.dispose()
		}
		font?.dispose()
	}
}
