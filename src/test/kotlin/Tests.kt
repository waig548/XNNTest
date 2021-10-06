import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import waig548.XNN.internal.functions.Sigmoid
import waig548.XNN.internal.network.Network
import waig548.XNN.internal.network.Neuron
import waig548.XNN.internal.utils.data.NetworkSerializer
import java.io.FileInputStream
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
        println(Sigmoid(2.0))
        println(Sigmoid.derivative(2.0))
        println(Sigmoid(-2.0))
        println(Sigmoid.derivative(-2.0))
    }

    @Test
    fun `Test serialize Neuron`()
    {
        val n = Neuron(mutableListOf(0.0,0.0), 0.0, Sigmoid)
        println(Neuron.serializer().descriptor)
        val s = Json.encodeToString(n)
        println(s)
    }
    @Test
    fun `Test deserialize Neuron`()
    {
        val s = """
            {"weight":[0.0,0.0],"bias":0.0,"activate":"ReLU"}
        """.trimIndent()
        val n = Json.decodeFromString<Neuron>(s)
        println()
    }

    @Test
    fun `Test0 read model`()
    {
        val s = GZIPInputStream(FileInputStream("mnist/MNIST.model")).bufferedReader().lines().toArray()
        println()
    }
}