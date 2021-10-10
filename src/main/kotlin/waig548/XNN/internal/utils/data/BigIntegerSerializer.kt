package waig548.XNN.internal.utils.data

import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigInteger

@Serializer(forClass = BigInteger::class)
object BigIntegerSerializer
{
    override val descriptor: SerialDescriptor
        get() = SerialDescriptor("java.math.BigInteger", String.serializer().descriptor)

    override fun deserialize(decoder: Decoder): BigInteger
    {
        return BigInteger(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: BigInteger)
    {
        encoder.encodeString(value.toString(10))
    }
}