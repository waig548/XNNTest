package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.dot
import waig548.XNN.internal.utils.scale

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

    fun backprop(i: List<Double>,o: Pair<Double,Double>, desired: Double?): Pair<List<Double>, Pair<List<Double>, Double>>
    {
        val db = activate.derivative(o.first)*2*(o.second-(desired ?:0.0))
        val dw = scale(i, db)
        val da = scale(weight, db)
        return Pair(da, Pair(dw, db))
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