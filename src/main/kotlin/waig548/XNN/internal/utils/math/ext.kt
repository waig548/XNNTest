package waig548.XNN.internal.utils.math

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun Collection<BigDecimal>.sum(): BigDecimal = this.reduce(BigDecimal::add)

val context = MathContext(10000, RoundingMode.HALF_UP)

fun bdFromString(value: String): BigDecimal
{
    return BigDecimal(value, context).unlimited()
}

fun bdFromInt(value: Int): BigDecimal
{
    return BigDecimal(value, context).unlimited()
}

fun bdFromLong(value: Long): BigDecimal
{

    return BigDecimal(value, context).unlimited()
}

fun bdFromDouble(value: Double): BigDecimal
{
    return BigDecimal(value, context).unlimited()
}

fun BigDecimal.unlimited(): BigDecimal
{
    return this.plus(context).setScale(this.scale()+2)
}

inline operator fun BigDecimal.plus(other: BigDecimal): BigDecimal = this.add(other, context)

inline operator fun BigDecimal.minus(other: BigDecimal): BigDecimal = this.subtract(other, context)

inline operator fun BigDecimal.times(other: BigDecimal): BigDecimal = this.multiply(other, context)

inline operator fun BigDecimal.div(other: BigDecimal): BigDecimal = this.divide(other, context)