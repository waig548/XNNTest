import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.jetbrains.kotlin.konan.file.use
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import waig548.XNN.internal.functions.ReLU
import waig548.XNN.internal.functions.Sigmoid
import waig548.XNN.internal.network.Network
import waig548.XNN.internal.utils.data.NetworkSerializer
import waig548.XNN.internal.utils.data.json
import waig548.XNN.internal.utils.math.bdFromDouble
import waig548.XNN.internal.utils.math.bdFromInt
import waig548.XNN.internal.utils.math.sum
import waig548.XNN.internal.utils.math.unlimited
import waig548.XNN.internal.utils.sub
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigDecimal
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.random.Random

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName::class)
class MNIST
{
    private val relu = false

    @Test
    @Disabled
    fun `1-Build and save model`()
    {
        val model = Network(
            "MNIST${if(relu) "-ReLU" else ""}",
            Random(System.currentTimeMillis()),
            28*28,
            listOf(16, 16, 10),
            if(relu) ReLU else Sigmoid
        )
        NetworkSerializer.serializeToFile(model, "mnist")
    }

    @Test
    @Disabled
    fun `2-Sanitize data`()
    {
        val rawTrainImages = GZIPInputStream(FileInputStream("mnist/train-images-idx3-ubyte.gz")).use {it.readBytes()}
        val rawTrainLabels = GZIPInputStream(FileInputStream("mnist/train-labels-idx1-ubyte.gz")).use {it.readBytes()}

        val strainSet = rawTrainImages.drop(16).map {bdFromInt(it.toUByte().toInt()).setScale(10) / bdFromDouble(255.0).setScale(10)}.chunked(28*28).zip(rawTrainLabels.drop(8).map {List(10){index -> if(index==it.toInt()) BigDecimal.ONE.unlimited() else BigDecimal.ZERO.unlimited() } })
        val trainSet = DataSet(
            "MNIST-train-bd",
            strainSet.size,
            strainSet.map {DataSet.Entry(it.first, it.second)}
        )
        DataSetSerializer.serializeToFile(trainSet, "mnist")
        //System.gc()
        val rawTestImages = GZIPInputStream(FileInputStream("mnist/t10k-images-idx3-ubyte.gz")).use {it.readBytes()}
        val rawTestLabels = GZIPInputStream(FileInputStream("mnist/t10k-labels-idx1-ubyte.gz")).use {it.readBytes()}
        val stestSet = rawTestImages.drop(16).map {bdFromInt(it.toUByte().toInt()) / bdFromDouble(255.0)}.chunked(28*28).zip(rawTestLabels.drop(8).map {List(10){index -> if(index==it.toInt()) BigDecimal.ONE.unlimited() else BigDecimal.ZERO.unlimited() } })
        val testSet = DataSet(
            "MNIST-test-bd",
            stestSet.size,
            stestSet.map {DataSet.Entry(it.first, it.second)}
        )
        DataSetSerializer.serializeToFile(testSet, "mnist")
        println()

    }

    @Test
    fun `3-Load and train`()
    {
        val test = false
        val model = NetworkSerializer.deserializeFromFile("mnist/MNIST${if(relu) "-ReLU" else ""}.model")
        val trainSet = DataSetSerializer.deserializeFromFile("mnist/MNIST-train-bd.data")
        //val testSet = DataSetSerializer.deserializeFromFile("mnist/MNIST-test-bd.data")

        model.SGD(
            trainSet.map {Pair(it.input, it.output)},
            100,
            bdFromDouble(3.0),
            //testSet.map {Pair(it.input, it.output)}
        )
/*
        val trainChunks = trainSet.shuffled().chunked(100)
        model.training = true
        for((i, v) in trainChunks.withIndex())
        {
            model.iterate(List(28*28) {1.0}, List(10) {0.0})
            println("Epoch ${i+1} of ${trainChunks.size} started")
            val o = mutableListOf<List<BigDecimal>>()
            for(e in v)
            {
                model.iterate(e.input, e.output)
            }
            var diff = 0.0
            if(test)
            {
                for(e in testSet.shuffled().slice(0..1000))
                    diff += pow(sub(model.forward(e.input), e.output),2.0).sum()
                println("Test ${i+1} of ${trainChunks.size}, avg. diff: ${diff/100}")
            }
        }


        println("Epoch ${trainChunks.size} ended.")*/
        NetworkSerializer.serializeToFile(model, "mnist")

    }

    @Test
    fun `4-Load and test`()
    {
        val model = NetworkSerializer.deserializeFromFile("mnist/MNIST${if(relu) "-ReLU" else ""}.model")
        val testSet = DataSetSerializer.deserializeFromFile("mnist/MNIST-test-bd.data")
        var tmp = BigDecimal.ZERO.setScale(100)
        var success = 0
        for(e in testSet)
        {
            model.iterate(e.input, e.output)
            val diff=sub(model.output, e.output)
            tmp += diff.sum()
            val prediction = model.output.withIndex().map {it.index to it.value}.filter {it.second>=0.80.toBigDecimal()}
            if(prediction.size == 1 && prediction.first().first == e.output.indexOfFirst {it.toInt() == BigDecimal.ONE.toInt()})
                success++
        }
        println("Avg. diff = ${tmp/testSet.size.toBigDecimal()}, success = $success")
        NetworkSerializer.serializeToFile(model, "mnist")
    }

    @Serializable
    data class DataSet(
        val name: String,
        override val size: Int,
        val data: List<Entry>
    ): Collection<DataSet.Entry>
    {
        override fun contains(element: Entry): Boolean
        {
            return data.contains(element)
        }

        override fun containsAll(elements: Collection<Entry>): Boolean
        {
            return data.containsAll(elements)
        }

        override fun isEmpty(): Boolean
        {
            return data.isEmpty()
        }

        override fun iterator(): Iterator<Entry>
        {
            return data.iterator()
        }

        @Serializable
        data class Entry(
            val input: List<@Contextual BigDecimal>,
            val output: List<@Contextual BigDecimal>
        )
    }

    object DataSetSerializer
    {
        fun serializeToFile(obj: DataSet, path: String? = null)
        {
            GZIPOutputStream(FileOutputStream("${path?.let {"$it/"} ?: ""}${obj.name}.data")).use {json.encodeToStream(obj, it)}
            println("Data saved to ${obj.name}.data")
        }

        fun deserializeFromFile(path: String): DataSet
        {
            val obj = GZIPInputStream(FileInputStream(path)).use {json.decodeFromStream(DataSet.serializer(), it)}
            println("Data loaded from $path")
            return obj
        }
    }
}