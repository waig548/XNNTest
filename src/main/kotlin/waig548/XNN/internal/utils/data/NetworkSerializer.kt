package waig548.XNN.internal.utils.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.encodeToStream
import org.jetbrains.kotlin.konan.file.use
import waig548.XNN.internal.network.Network
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object NetworkSerializer
{
    fun serializeToFile(obj: Network, path: String? = null)
    {
        GZIPOutputStream(FileOutputStream("${path?.let {"$it/"} ?: ""}${obj.name}.model")).use {Json.encodeToStream(obj, it)}
        println("Network saved to ${path ?:""}/${obj.name}.model")
    }

    fun deserializeFromFile(path: String): Network
    {
        val obj = GZIPInputStream(FileInputStream(path)).use {Json.decodeFromStream(Network.serializer(), it)}
        println("Network loaded from $path")
        return obj
    }
}