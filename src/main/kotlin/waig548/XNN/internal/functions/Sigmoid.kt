package waig548.XNN.internal.functions

import waig548.XNN.internal.utils.math.bdFromDouble
import waig548.XNN.internal.utils.math.unlimited
import java.math.BigDecimal
import kotlin.math.exp

object Sigmoid: ActivationFunction
{
    override val name: String get() = this::class.java.simpleName
    override fun invoke(z: BigDecimal): BigDecimal
    {
        return BigDecimal.ONE.unlimited()/ bdFromDouble(1.0+exp(-z.toDouble()))
    }

    override fun derivative(z: BigDecimal): BigDecimal
    {
        return this(z)*(BigDecimal.ONE.unlimited()-this(z))
    }
}