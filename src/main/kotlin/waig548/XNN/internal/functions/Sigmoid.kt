package waig548.XNN.internal.functions

import kotlin.math.exp

object Sigmoid: ActivationFunction
{
    override val name: String get() = this::class.java.simpleName
    override fun invoke(z: Double): Double
    {
        return 1.0/(1.0+exp(-z))
    }

    override fun derivative(z: Double): Double
    {
        return this(z)*(1-this(z))
    }
}