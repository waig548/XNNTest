package waig548.XNN.internal.functions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ActivationFunction.Companion::class)
interface ActivationFunction: (Double) -> Double
{
    val name: String

    override operator fun invoke(z: Double): Double
    fun derivative(z: Double): Double

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