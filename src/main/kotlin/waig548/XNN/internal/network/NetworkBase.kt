package waig548.XNN.internal.network

sealed class NetworkBase
{
    abstract val size: Int

    var x: List<Double> = listOf()
    var y: List<Double> = listOf()
    var output: List<Double> = listOf()

    //abstract fun forward(input: List<Double>): List<Double>
    //abstract fun backprop(desired: List<Double>): List<Double>
}
