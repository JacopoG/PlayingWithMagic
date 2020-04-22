package no.group15.playmagic.commands

import com.badlogic.gdx.utils.Pool


class MessageCommand() : Command {

	@Transient private var pool: Pool<MessageCommand>? = null

	override val type: Command.Type = Command.Type.MESSAGE

	var text = ""
	var timestamp = 0f


	constructor(pool: Pool<MessageCommand>) : this() {
		this.pool = pool
	}

	override fun free() {
		pool?.free(this)
	}

	override fun reset() {
		text = ""
		timestamp = 0f
	}
}
