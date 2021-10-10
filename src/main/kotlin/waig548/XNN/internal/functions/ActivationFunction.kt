package waig548.XNN.internal.functions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable(with = ActivationFunction.Companion::class)
interface ActivationFunction
{
    val name: String

    operator fun invoke(z: BigDecimal): BigDecimal
    fun derivative(z: BigDecimal): BigDecimal

    companion object: KSerializer<ActivationFunction>
    {
        private val map: Map<String, ActivationFunction> = mapOf(
            Sigmoid.name to Sigmoid,
            ReLU.name to ReLU
        )
        override val descriptor: SerialDescriptor get() = PrimitiveSerialDescriptor(ActivationFunction::class.java.name, PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ActivationFunction
        {
            return map[decoder.decodeString()] ?: throw IllegalArgumentException("Invalid ActivationFunction identifier")
        }

        override fun serialize(encoder: Encoder, value: ActivationFunction)
        {
            encoder.encodeString(value.name)
        }
    }
}