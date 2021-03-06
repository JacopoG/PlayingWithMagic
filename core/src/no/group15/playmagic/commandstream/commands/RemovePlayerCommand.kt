package no.group15.playmagic.commandstream.commands

import no.group15.playmagic.commandstream.Command


class RemovePlayerCommand() : Command {

	override val type: Command.Type = Command.Type.REMOVE_PLAYER

	var playerId = 0


	constructor(id: Int) : this() {
		playerId = id
	}

	override fun free() {
		// Not pooled
	}

	override fun reset() {
		// Not pooled
	}
}
