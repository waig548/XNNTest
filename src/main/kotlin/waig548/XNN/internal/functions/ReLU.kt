package waig548.XNN.internal.functions

import kotlin.math.max

object ReLU: ActivationFunction
{
    override val name: String get() = this::class.java.simpleName
    override fun invoke(z: Double): Double
    {
        return max(z, 0.01*z)
    }

    override fun derivative(z: Double): Double
    {
        return if(z<=0) 0.01 else 1.0
    }
}