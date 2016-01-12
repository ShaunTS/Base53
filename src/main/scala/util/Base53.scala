package org.sts.util


/**
 * Base53 digits[0 - 52]:
 *  0,
 *  a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z,
 *  A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
 *
 *
 * eg:
 *  (0, a, b, c, A, B, C) = (0, 1, 2, 3, 27, 28, 29)
 *
 *  53 (base 10) = "a0" (base 53)
 */
object Base53 {

    val minLength: Int = 1

    val base: Double = 53D

    val radix: Map[Long, Char] =
        (List.range(97, 123) ++ List.range(65, 91)).map(_.toChar).zipWithIndex
        .map { case (digit, i) => (i+1).toLong -> digit }
        .toMap + (0L -> '0')

    val legalChar = "[[A-za-z]]"

    def apply(n: Long): String = fromLong(n)

    def apply(str: String): Long = fromString(str)

    /** Converts a base 10 number value to a base 53 string
     *  @param id A long value that will be converted to Base53
     *  @return A String representing the given value in Base53
     */
    def fromLong(id: Long): String = {
        if(id > 0) {
            val steps: Seq[Long] =
            Seq.range(0, scala.math.floor(log53(id)).toLong + 1).reverse

            var carry: Long = id

            steps.map(java.lang.Math.pow(base, _)).map { x =>
                val digit = carry / x
                carry = carry % x.toLong
                digit.toLong
            }.map(radix(_)).mkString
        }
        else "0"
    }

    /** Maps a single char to the its corresponding digit value.
     *  @param digit A char value 
     */
    def fromChar(digit: Char): Long = Seq('Z', digit).sorted match {
        case Seq('0', _) => 0L
        case Seq(`digit`, lower) => (digit.toLong - 38L)
        case Seq(upper, `digit`) => (digit.toLong - 96L)
    }

    private def radixCheck(arg: String): String = {
        arg.map(_.toInt).find {
            case zero @ 48 => false
            case upper if(64 < upper && upper < 91) => false
            case lower if(96 < lower && lower < 123) => false
            case _ => true
        }.foreach {
            illegal => throw new java.lang.NumberFormatException(
                s"'${illegal.toChar}' is not contained in base-53 radix [0, a-z, A-Z]"
            )
        }
        arg
    }

    /** Decodes the given base 53 string to its corresponding base 10
     *  value as a Long
     *  @param id A string representing a numeric value in base 53.
     *  @return The corresponding numeric base 10 value.
     */
    def fromString(id: String): Long = radixCheck(id).reverse.toList
        .map(fromChar).zipWithIndex
        .map { case (x, i) => x * java.lang.Math.pow(base, i) }
        .sum.toLong

    def log53(x: Double): Double = (
        scala.math.log(x)/scala.math.log(base)
    )

    /** Returns the given base 53 string, incremented by 1
     *  @param id A string representing a base 53 value
     *  @return The given value incremented by 1
     *
     *  @note this method works by parsing through the digits of the corresponding
     *  base 10 number value, without ever setting it to a variable. This means this
     *  method could be used to generate base 53 values well in excess of the MaxValue
     *  allowed by the Long data type.
     */
    def nextAfter(id: String): String = {
        val digits: List[Long] = radixCheck(id).toList.reverse.map(fromChar)

        val (pos, digit) = digits.zipWithIndex
            .collectFirst { case (x, i) if x < (base-1) => (i, x) }
            .getOrElse(id.length -> 0L)

        val (lower, higher) = digits.splitAt(pos)



        (List.fill(lower.length)(0L) ++ List(digit + 1L) ++ higher.drop(1))
            .map(radix).reverse.mkString
    }
}