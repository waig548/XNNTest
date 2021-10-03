package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import waig548.XNN.internal.functions.ActivationFunction
import kotlin.random.Random

@Serializable//(with = Network.Companion::class)
class Network private constructor(
    val name: String,
    private var inputSize: Int,
    private val activationFunction: ActivationFunction,
    val layers: MutableList<Layer>
): NetworkBase()
{
    constructor(
        name: String,
        rand: Random,
        inputSize: Int,
        layerSizes: List<Int>,
        activationFunction: ActivationFunction
    ): this(name, inputSize, activationFunction, MutableList(layerSizes.size) {Layer(it, rand, layerSizes[it], if(it==0) inputSize else layerSizes[it-1], activationFunction)})

    @Transient var training: Boolean = false

    init
    {
        layers.forEachIndexed { index, layer ->
            //if(index!=0) layer.previousLayer = layers[index-1]
            if(index!=layers.size-1)
            {
                layer.nextLayer = layers[index+1]
                layer.nextLayerID = index+1
            }
        }
    }
    override val size get() = layers.size

    override fun iterate(input: List<Double>, desired: List<Double>)
    {
        super.iterate(input, desired)
        if(training)
            backprop(desired)
    }

    override fun forward(input: List<Double>): List<Double>
    {
        var curData = input
        val iterator = layers.iterator()
        while(iterator.hasNext())
            curData = iterator.next().forward(curData)
        return curData
    }

    override fun backprop(desired: List<Double>): List<Double>
    {
        val iterator = layers.reversed().iterator()
        var cur = desired
        while(iterator.hasNext())
            cur = iterator.next().backprop(cur)
        return cur
    }

    fun addLayer(index: Int = size)
    {
        TODO("Not yet implemented")
    }

    fun removeLayer(index: Int)
    {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean
    {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Network

        if(name != other.name) return false
        if(layers != other.layers) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = name.hashCode()
        result = 31 * result + layers.hashCode()
        return result
    }
}