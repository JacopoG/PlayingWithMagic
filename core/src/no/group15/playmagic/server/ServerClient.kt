package no.group15.playmagic.server

import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.TimeUtils
import kotlinx.coroutines.launch
import ktx.collections.*
import ktx.json.*
import ktx.log.*
import ktx.math.ImmutableVector2
import no.group15.playmagic.commands.Command
import no.group15.playmagic.commands.ConfigCommand
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread


/**
 * Server side client
 */
class ServerClient(
	val id: Int,
	private val socket: Socket,
	private val server: Server,
	val spawnPosition: ImmutableVector2
) : Disposable {//, CoroutineScope by CoroutineScope(newSingleThreadAsyncContext()) {

	private val writer = socket.outputStream.bufferedWriter()
	private val reader = socket.inputStream.bufferedReader()
	private val json = server.json
	val receiveQueue = gdxArrayOf<Command>()
	private val logMessage = "Client $id:"


	init {
		sendWelcomeConfig()
		thread {
//			val time = TimeUtils.nanoTime()
//			debug { "$logMessage Trying to start reading from input stream" }
//			try {
//				while (!reader.ready()) {
//					// Do nothing
//				}
//			} catch (e: IOException) {
//				error { "$logMessage Error while waiting for ready: ${e.message}" }
//				server.removeClient(id)
//				return@thread
//			}
			debug { "$logMessage Listening for incoming data" }
			receive()
		}
	}

	private tailrec fun receive() {
		try {
			val line = reader.readLine()
			if (line == null) {
				error { "$logMessage Reached end of stream" }
				server.removeClient(id)
				return
			} else {
				handleMessage(line)
			}
		} catch (e: IOException) {
			error { "$logMessage Error while reading from input stream: ${e.message}" }
			// TODO connection lost?
			server.removeClient(id)
			return
		}
		if (!server.running) return else receive()
	}

	private var receivedFirstData = false
	/**
	 * Handle incoming data
	 */
	private fun handleMessage(string: String) {
		val array = json.fromJson<GdxArray<Command>>(string)
		server.launch {
			receiveQueue.addAll(array)
			if (!receivedFirstData) {
				debug { "$logMessage Received first data: ${array.size} commands" }
				receivedFirstData = true
			}
		}
	}

	private fun sendWelcomeConfig() {
		val command = ConfigCommand()
		// Send id, tick rate, game map, spawn position, etc
		command.playerId = id
		command.tickRate = server.config.tickRate
		command.spawnPosX = spawnPosition.x
		command.spawnPosY = spawnPosition.y
		val array = gdxArrayOf<Command>()
		array.add(command)
		sendCommands(array)
	}

	fun sendCommands(array: GdxArray<Command>) {
		try {
			writer.write(json.toJson(array))
			writer.newLine()
			writer.flush()
		} catch (e: IOException) {
			error { "$logMessage Error while writing to output stream: ${e.message}" }
			// TODO close connection?
			server.removeClient(id)
		}
	}

	override fun dispose() {
		// TODO send goodbye message?
		try { socket.dispose() } catch (e: Exception) {}
		try { reader.close() } catch (e: Exception) {}
		try { writer.close() } catch (e: Exception) {}
	}

	companion object {
		fun rejectClient(socket: Socket, json: String) {
			val writer = socket.outputStream.bufferedWriter()
			writer.write(json)
			writer.close()
			socket.dispose()
		}
	}
}
