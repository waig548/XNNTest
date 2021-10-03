package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.add
import waig548.XNN.internal.utils.applyFunction
import waig548.XNN.internal.utils.applyMatrix
import waig548.XNN.internal.utils.matrixMul
import waig548.XNN.internal.utils.mul
import waig548.XNN.internal.utils.scale
import waig548.XNN.internal.utils.sub
import waig548.XNN.internal.utils.transpose
import kotlin.random.Random

@Serializable
class Layer private constructor(
    val id: Int,
    var inputSize: Int,
    private val activationFunction: ActivationFunction,
    val neurons: MutableList<Neuron>,
    var nextLayerID: Int? = null
): NetworkBase()
{
    constructor(
        id: Int,
        rand: Random,
        size: Int,
        inputSize: Int,
        activationFunction: ActivationFunction
    ): this(id, inputSize, activationFunction, MutableList(size) {Neuron(MutableList(inputSize) {rand.nextDouble(-1.0,1.0)}, 0.0, activationFunction)})


    //var previousLayerID: Int? = null

    //private var previousLayer: Layer? = null
    @Transient var nextLayer: Layer? = null
    var zs = listOf<Double>()

    override val size get() = neurons.size

    override fun forward(input: List<Double>): List<Double>
    {
        x = input
        output = neurons.map {it.forward(input)}
        return output
    }

    override fun backprop(desired: List<Double>): List<Double>
    {
        y = desired
        val db = mul(scale(sub(output, desired), -2.0), applyFunction(activationFunction::derivative, output))
        val dW = matrixMul(db, x)
        val da = applyMatrix(transpose(neurons.map {it.weight.toList()}), db)
        for((i, n) in neurons.withIndex())
        {
            n.bias+=db[i]
            n.weight= add(n.weight, dW[i]).toMutableList()
        }
        return add(x, da)
    }

    fun addNeuron(rand: Random, index: Int = size)
    {
        neurons.add(
            index,
            Neuron(
                MutableList(inputSize) {rand.nextDouble(-1.0, 1.0)},
                0.0,
                activationFunction
            )
        )
        nextLayer?.inputSize=size
        nextLayer?.neurons?.forEach {it.weight.add(index, rand.nextDouble(-1.0, 1.0))}
    }

    fun removeNeuron(index: Int)
    {
        neurons.removeAt(index)
        nextLayer?.inputSize=size
        nextLayer?.neurons?.forEach {it.weight.removeAt(index)}
    }

    override fun equals(other: Any?): Boolean
    {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Layer

        if(neurons != other.neurons) return false

        return true
    }

    override fun hashCode(): Int
    {
        return neurons.hashCode()
    }
}