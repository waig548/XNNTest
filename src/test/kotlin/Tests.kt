import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import waig548.XNN.internal.functions.Sigmoid
import waig548.XNN.internal.network.Network
import waig548.XNN.internal.network.Neuron
import waig548.XNN.internal.utils.data.NetworkSerializer
import waig548.XNN.internal.utils.data.json
import waig548.XNN.internal.utils.math.bdFromDouble
import waig548.XNN.internal.utils.math.bdFromInt
import waig548.XNN.internal.utils.math.unlimited
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.zip.GZIPInputStream
import kotlin.random.Random

class Tests
{
    @Test
    fun `Test build save load model`()
    {
        val rand = Random(System.currentTimeMillis())
        val network = Network("Test1", rand, 10, listOf(5, 2, 3), Sigmoid)
        println()
        NetworkSerializer.serializeToFile(network)
        val network2 = NetworkSerializer.deserializeFromFile("Test1.model")
        assert(network == network2)
    }

    @Test
    fun `Test Sigmoid`()
    {
        println(Sigmoid(2.0.toBigDecimal()))
        println(Sigmoid.derivative(2.0.toBigDecimal()))
        println(Sigmoid((-2.0).toBigDecimal()))
        println(Sigmoid.derivative((-2.0).toBigDecimal()))
    }

    @Test
    fun `Test serialize Neuron`()
    {
        val n = Neuron(mutableListOf(BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO, Sigmoid)
        println(Neuron.serializer().descriptor)
        val s = json.encodeToString(n)
        println(s)
    }
    @Test
    fun `Test deserialize Neuron`()
    {
        val s = """
            {"weight":["0.0","0.0"],"bias":"0.0","activate":"ReLU"}
        """.trimIndent()
        val n = json.decodeFromString<Neuron>(s)
        println()
    }

    @Test
    fun `Test0 read model`()
    {
        val s = GZIPInputStream(FileInputStream("mnist/MNIST.model")).bufferedReader().lines().toArray()
        println()
    }

    @Test
    fun `Test serialize BigDecimal`()
    {
        val t = bdFromDouble(3.0)
        val a = BigDecimal.ONE.unlimited().setScale(10)
        val b = a.div(t)
        val k = json.encodeToString(b)
        val l = json.encodeToString(BigDecimal.ZERO)
        val m = json.encodeToString(BigDecimal.TEN)
        println()
    }
}