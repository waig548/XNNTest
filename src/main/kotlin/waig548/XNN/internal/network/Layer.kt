package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.add
import waig548.XNN.internal.utils.applyFunction
import waig548.XNN.internal.utils.applyMatrix
import waig548.XNN.internal.utils.clamp
import waig548.XNN.internal.utils.matrixMul
import waig548.XNN.internal.utils.matrixMul2
import waig548.XNN.internal.utils.mul
import waig548.XNN.internal.utils.pow
import waig548.XNN.internal.utils.scale
import waig548.XNN.internal.utils.sub
import waig548.XNN.internal.utils.transpose
import kotlin.math.exp
import kotlin.random.Random
import kotlin.random.asJavaRandom

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
    ): this(id, inputSize, activationFunction, MutableList(size) {Neuron(MutableList(inputSize) {/*if(id==0) 0.0 else */rand.asJavaRandom().nextDouble()*2.0-1.0}, 0.0, activationFunction)})


    //var previousLayerID: Int? = null

    //private var previousLayer: Layer? = null
    @Transient var nextLayer: Layer? = null
    @Transient var zs = listOf<Double>()
    @Transient var clamped: List<Double> = listOf()
    @Transient var isLastLayer = false
    override val size get() = neurons.size

    fun forward(input: List<Double>): List<Double>
    {
        x = input
        val o = neurons.map {it.forward(input)}.unzip()
        zs = o.first; output = o.second
        if (isLastLayer)
        {
            val tmp = applyFunction(::exp, zs)
            output = scale(tmp, 1.0/tmp.sum())
        }
        return output
        //clamped = clamp(output, 0.0, 1.0)
        //return clamped
    }

    fun backprop(desired: List<Double>): Pair<List<Double>, Pair<List<List<Double>>, List<Double>>>
    {
        //val bp = neurons.mapIndexed { i, it-> it.backprop(x, Pair(zs[i], output[i]), if(isLastLayer) desired[i] else null) }.unzip()
        //return Pair(transpose(bp.first).map { it.sum()/it.size }, bp.second.unzip())
        //if(id==0)
        //    return x
        y = desired
        val dCdA = scale(sub(output, desired), 1.0)
        val dAdZ = applyFunction(activationFunction::derivative, zs)
        val dZdB = 1.0
        val dZdW = x
        val dZda = transpose(neurons.map { it.weight.toList() })
        val db = scale(scale(mul(dAdZ,dCdA),dZdB), 1.0)
        val dW = matrixMul2((dZdW), (db))
        val da = applyMatrix(dZda, db)
        //val db = mul(scale(sub(output, desired), 1.0), applyFunction(activationFunction::derivative, zs))
        //val dW = matrixMul(db, x)
        //val da = applyMatrix(transpose(neurons.map {it.weight.toList()}), db)
        /*for((i, n) in neurons.withIndex())
        {
            n.bias+=db[i]
            n.weight= add(n.weight, dW[i]).toMutableList()
        }*/
        return Pair(add(x, da), Pair(dW, db))
    }

    fun addNeuron(rand: Random, index: Int = size)
    {
        neurons.add(
            index,
            Neuron(
                MutableList(inputSize) {/*if(id==0) 0.0 else */rand.asJavaRandom().nextGaussian()},
                rand.asJavaRandom().nextGaussian(),
                activationFunction
            )
        )
        nextLayer?.inputSize=size
        nextLayer?.neurons?.forEach {it.weight.add(index, rand.asJavaRandom().nextGaussian())}
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