package waig548.XNN.internal.utils.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val module = SerializersModule {
    contextual(BigDecimalSerializer)
}

val json = Json {serializersModule = module}

