package waig548.XNN.internal.network

import java.math.BigDecimal

sealed class NetworkBase
{
    abstract val size: Int

    var x: List<BigDecimal> = listOf()
    var y: List<BigDecimal> = listOf()
    var output: List<BigDecimal> = listOf()

    //abstract fun forward(input: List<BigDecimal>): List<BigDecimal>
    //abstract fun backprop(desired: List<BigDecimal>): List<BigDecimal>
}
