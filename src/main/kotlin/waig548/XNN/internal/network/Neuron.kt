package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.dot

@Serializable
class Neuron(
    var weight: MutableList<Double>,
    var bias: Double,
    var activate: ActivationFunction
)
{
    fun forward(input: List<Double>): Pair<Double, Double>
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