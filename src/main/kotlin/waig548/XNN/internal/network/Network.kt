package waig548.XNN.internal.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import waig548.XNN.internal.functions.ActivationFunction
import waig548.XNN.internal.utils.add
import waig548.XNN.internal.utils.addMatrix
import waig548.XNN.internal.utils.math.sum
import waig548.XNN.internal.utils.math.unlimited
import waig548.XNN.internal.utils.pow
import waig548.XNN.internal.utils.scale
import waig548.XNN.internal.utils.sub
import java.math.BigDecimal
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

    fun iterate(input: List<BigDecimal>, desired: List<BigDecimal>): List<BigDecimal>
    {
        x = input
        y = desired
        output = forward(x)
        return output
        //if(training)
        //    backprop(input, desired)
    }

    fun forward(input: List<BigDecimal>): List<BigDecimal>
    {
        var curData = input
        val iterator = layers.iterator()
        while(iterator.hasNext())
            curData = iterator.next().forward(curData)
        return curData
    }

    fun backprop(input: List<BigDecimal>, desired: List<BigDecimal>): List<Pair<List<List<BigDecimal>>, List<BigDecimal>>>
    {
        iterate(input, desired)
        val iterator = layers.reversed().iterator()
        var cur = desired
        val d = mutableListOf<Pair<List<List<BigDecimal>>, List<BigDecimal>>>()
        while(iterator.hasNext())
        {
            val tmp = iterator.next().backprop(cur)
            cur = tmp.first
            d.add(tmp.second)
        }
        return d.reversed()
    }

    fun SGD(trainingData: List<Pair<List<BigDecimal>, List<BigDecimal>>>, batchSize: Int, eta: BigDecimal, testData: List<Pair<List<BigDecimal>, List<BigDecimal>>>? = null)
    {
        val trainChunks = trainingData.shuffled().chunked(batchSize)
        for((i, v) in trainChunks.withIndex())
        {
            //model.iterate(List(28 * 28) {1.0}, List(10) {0.0})
            println("Epoch ${i + 1} of ${trainChunks.size} started")
            updateBatch(v, eta)

            if(testData!=null)
            {
                var diff = 0.0.toBigDecimal()
                var success = 0
                for((x, y) in testData.shuffled().slice(0..1000))
                {
                    diff += pow(sub(iterate(x, y), y), 2).sum()
                    val prediction = output.withIndex().map {it.index to it.value}.filter {it.second >= 0.80.toBigDecimal()}
                    if(prediction.size == 1 && prediction.first().first == y.indexOf(1.0.toBigDecimal()))
                        success++
                }
                println("Test ${i+1} of ${trainChunks.size}, avg. diff: ${diff/(1000).toBigDecimal()}, success rate: ${success/1000.0*100}%")
            }
        }
        println("Epoch ${trainChunks.size} ended.")
    }

    fun updateBatch(batch: List<Pair<List<BigDecimal>, List<BigDecimal>>>, eta: BigDecimal)
    {
        val dW = MutableList(size) {id-> List(layers[id].size) {List(layers[id].inputSize) {BigDecimal.ZERO.unlimited()} } }
        val dB = MutableList(size) {id-> List(layers[id].size) {BigDecimal.ZERO.unlimited()} }
        for((x, y) in batch)
        {
            val d = backprop(x, y)
            for((i, v) in d.withIndex())
            {
                dW[i]=addMatrix(dW[i], v.first)
                dB[i]=add(dB[i], v.second)
            }
        }
        for((i, l) in layers.withIndex())
        {
            for((j, n) in l.neurons.withIndex())
            {
                n.bias=(n.bias-eta/batch.size.toBigDecimal()*dB[i][j])
                n.weight=scale(sub(n.weight, dW[i][j]), eta/batch.size.toBigDecimal()).toMutableList()
            }
        }
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