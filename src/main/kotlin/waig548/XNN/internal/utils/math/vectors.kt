package waig548.XNN.internal.utils

import waig548.XNN.internal.utils.math.sum
import java.math.BigDecimal

fun add(a: List<BigDecimal>, b: List<BigDecimal>): List<BigDecimal>
{
    return a.zip(b).map {it.first+it.second}
}

fun sub(a: List<BigDecimal>, b: List<BigDecimal>): List<BigDecimal>
{
    return a.zip(b).map {it.first-it.second}
}

fun mul(a: List<BigDecimal>, b: List<BigDecimal>): List<BigDecimal>
{
    return a.zip(b).map {it.first*it.second}
}

fun div(a: List<BigDecimal>, b: List<BigDecimal>): List<BigDecimal>
{
    return a.zip(b).map {it.first/it.second}
}

fun pow(a: List<BigDecimal>, b: Int): List<BigDecimal>
{
    return a.map {it.pow(b)}
}
fun scale(a: List<BigDecimal>, b: BigDecimal): List<BigDecimal>
{
    return a.map {it*b}
}

fun dot(a: List<BigDecimal>, b: List<BigDecimal>): BigDecimal
{
    return a.zip(b).map {it.first*it.second}.sum()
}

fun applyFunction(function: ((BigDecimal) -> BigDecimal), a: List<BigDecimal>): List<BigDecimal>
{
    return a.map(function)
}

fun applyMatrix(m: List<List<BigDecimal>>, a: List<BigDecimal>): List<BigDecimal>
{
    return m.map {dot(it, a)}
}

fun matrixMul(a: List<BigDecimal>, b: List<BigDecimal>): List<List<BigDecimal>>
{
    return List(a.size) {scale(b, a[it])}
}

fun transpose(m: List<List<BigDecimal>>): List<List<BigDecimal>>
{
    return List(m[0].size) {i -> List(m.size) {j-> m[j][i]} }
}

fun clamp(a: List<BigDecimal>, lowerBound: BigDecimal, upperBound: BigDecimal): List<BigDecimal>
{
    return a.map {lowerBound.min(upperBound.max(it))}
}

fun addMatrix(a: List<List<BigDecimal>>, b: List<List<BigDecimal>>): List<List<BigDecimal>>
{
    return a.zip(b).map {add(it.first, it.second)}
}

/* dunno how but just in case
fun cross(a: List<BigDecimal>, b: List<BigDecimal>)
{

}
 */