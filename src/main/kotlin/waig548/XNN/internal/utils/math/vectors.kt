package waig548.XNN.internal.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

fun add(a: List<Double>, b: List<Double>): List<Double>
{
    return a.zip(b).map {it.first+it.second}
}

fun sub(a: List<Double>, b: List<Double>): List<Double>
{
    return a.zip(b).map {it.first-it.second}
}

fun mul(a: List<Double>, b: List<Double>): List<Double>
{
    return a.zip(b).map {it.first*it.second}
}

fun div(a: List<Double>, b: List<Double>): List<Double>
{
    return a.zip(b).map {it.first/it.second}
}

fun pow(a: List<Double>, b: Double): List<Double>
{
    return a.map {it.pow(b)}
}
fun scale(a: List<Double>, b: Double): List<Double>
{
    return a.map {it*b}
}

fun dot(a: List<Double>, b: List<Double>): Double
{
    return a.zip(b).map {it.first*it.second}.sum()
}

fun applyFunction(function: ((Double) -> Double), a: List<Double>): List<Double>
{
    return a.map(function)
}

fun applyMatrix(m: List<List<Double>>, a: List<Double>): List<Double>
{
    return m.map {dot(it, a)}
}

fun matrixMul(a: List<Double>, b: List<Double>): List<List<Double>>
{
    return List(a.size) {scale(b, a[it])}
}

fun transpose(m: List<List<Double>>): List<List<Double>>
{
    return List(m[0].size) {i -> List(m.size) {j-> m[j][i]} }
}

fun clamp(a: List<Double>, lowerBound: Double, upperBound: Double): List<Double>
{
    return a.map {min(max(lowerBound, it), upperBound)}
}

fun addMatrix(a: List<List<Double>>, b: List<List<Double>>): List<List<Double>>
{
    return a.zip(b).map {add(it.first, it.second)}
}

/* dunno how but just in case
fun cross(a: List<Double>, b: List<Double>)
{

}
 */