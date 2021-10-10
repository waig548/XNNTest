package waig548.XNN.internal.utils.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import waig548.XNN.internal.utils.math.context
import waig548.XNN.internal.utils.math.unlimited
import java.math.BigDecimal

@ExperimentalSerializationApi
@Serializer(forClass = BigDecimal::class)
object BigDecimalSerializer
{

    override val descriptor: SerialDescriptor
        get() = SerialDescriptor("java.math.BigDecimal", ListSerializer(String.serializer()).descriptor)


    override fun deserialize(decoder: Decoder): BigDecimal
    {
        return decoder.decodeStructure(descriptor) {
            BigDecimal(
                decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer),
                decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer).toInt()
            )
        }
    }

    override fun serialize(encoder: Encoder, value: BigDecimal)
    {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, BigIntegerSerializer, value.unscaledValue())
            encodeSerializableElement(descriptor, 1, BigIntegerSerializer, value.scale().toBigInteger())
        }
    }

}