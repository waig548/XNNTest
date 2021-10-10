package waig548.XNN.internal.functions

import waig548.XNN.internal.utils.math.unlimited
import java.math.BigDecimal
import kotlin.math.max

object ReLU: ActivationFunction
{
    override val name: String get() = this::class.java.simpleName
    override fun invoke(z: BigDecimal): BigDecimal
    {
        return BigDecimal.ZERO.unlimited().max(z)
    }

    override fun derivative(z: BigDecimal): BigDecimal
    {
        return /*if(z<=0) 0.0 else */BigDecimal.ONE.unlimited()
    }
}