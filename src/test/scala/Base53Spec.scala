package test.unit.util

import org.specs2.mutable.Specification
import org.sts.util.Base53


object Base53Spec extends Specification with Base53Expected {

    "Base53 utility" should { br
        t
        "Convert number values [0-52] to corresponding digits" in {
            val digitValues: List[Long] = List.range(0L, 53L)

            val test: List[String] = digitValues.map(Base53.fromLong)

            test foreach {
                _ must have length(1)
            }

            val testDigits: List[Char] = test.map(_.head)

            testDigits must contain(exactly(allDigits: _ *).inOrder)
        }


        br;t
        "Convert number values over 52 to corresponding Base 53 strings" >> {

            "53" in { Base53.fromLong(53L) must equalTo("a0")}

            "999" in { Base53.fromLong(999L) must equalTo("rS") }

            "77546" in { Base53.fromLong(77546L) must equalTo("AFg") }

            "4294967298" in { Base53.fromLong(4294967298L) must equalTo("jnqelR") }
            bt
        }

        br;t
        "Increment base 53 string values by 1" >> {

            "a0 + 1" in {
                Base53.nextAfter("a0") must equalTo("aa")
            }

            "rS + 1" in {
                Base53.nextAfter("rS") must equalTo("rT")
            }

            "AFg + 1" in {
                Base53.nextAfter("AFg") must equalTo("AFh")
            }

            "jnqelR + 1" in {
                Base53.nextAfter("jnqelR") must equalTo("jnqelS")
            }
            bt
        }
        bt
    }
}


trait Base53Expected {

    val lowerChr: List[Char] = List('a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z')


    val upperChr: List[Char] = List('A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z')

    def allDigits: List[Char] = (List('0') ++ lowerChr ++ upperChr)
}