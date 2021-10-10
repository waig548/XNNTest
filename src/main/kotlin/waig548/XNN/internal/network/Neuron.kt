package waig548.XNN.internal.network

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.dot
import java.math.BigDecimal


@Serializable

class Neuron(
    var weight: MutableList<@Contextual BigDecimal>,
    var bias: @Contextual BigDecimal,
    var activate: ActivationFunction
)
{
    fun forward(input: List<BigDecimal>): Pair<BigDecimal, BigDecimal>
    {
        return dot(weight, input)+bias to activate(dot(weight, input)+bias)
    }

    override fun equals(other: Any?): Boolean
    {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Neuron

        if(weight != other.weight) return false
        if(bias != other.bias) return false
        if(activate.javaClass != other.activate.javaClass) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = weight.hashCode()
        result = 31 * result + bias.hashCode()
        return result
    }
}