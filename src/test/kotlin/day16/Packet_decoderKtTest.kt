package day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class Packet_decoderKtTest {
    val sample = "D2FE28"

    @Test
    fun `should build binary string`() {

        val got = toBinaryString(sample)

        assertThat(got).isEqualTo("110100101111111000101000")
    }

    @Nested
    inner class Companion {
        @Test
        fun `should read PacketValue and return remain`() {
            val given = "101111111000101000"
            val (gotValue, gotRemain) = Packet.readValue(given)

            assertThat(gotValue).isEqualTo(2021)
            assertThat(gotRemain).isEqualTo("000")
        }

        @Test
        fun `1 should read PacketOperator and return remain`() {
            val given = "00111000000000000110111101000101001010010001001000000000"
            val (gotValue, gotRemain) = Packet.readNext(given)!!

            val expected = PacketOperator(
                version = 1,
                id = 6,
                subPackets = listOf(
                    PacketValue(
                        version = 6,
                        value = 10
                    ),
                    PacketValue(
                        version = 2,
                        value = 20
                    ),
                )
            )
            assertThat(gotValue).isEqualTo(expected)
            assertThat(gotRemain).isEqualTo("0000000")
        }

    }

    @Test
    fun `2 should read PacketOperator and return remain`() {
        val given = "11101110000000001101010000001100100000100011000001100000"
        val (gotValue, gotRemain) = Packet.readNext(given)!!

        val expected = PacketOperator(
            version = 7,
            id = 3,
            subPackets = listOf(
                PacketValue(
                    version = 2,
                    value = 1
                ),
                PacketValue(
                    version = 4,
                    value = 2
                ),
                PacketValue(
                    version = 1,
                    value = 3
                ),
            )
        )
        assertThat(gotValue).isEqualTo(expected)
        assertThat(gotRemain).isEqualTo("00000")
    }

    @Nested
    inner class SumVersion(){
        @Test
        fun `8A004A801A8002F478`(){
            val got = Packet.build("8A004A801A8002F478").sumVersions()
            assertThat(got).isEqualTo(16)
        }

        @Test
        fun `620080001611562C8802118E34`(){
            val got = Packet.build("620080001611562C8802118E34").sumVersions()
            assertThat(got).isEqualTo(12)
        }

        @Test
        fun `C0015000016115A2E0802F182340`(){
            val got = Packet.build("C0015000016115A2E0802F182340").sumVersions()
            assertThat(got).isEqualTo(23)
        }

        @Test
        fun `A0016C880162017C3686B18A3D4780`(){
            val got = Packet.build("A0016C880162017C3686B18A3D4780").sumVersions()
            assertThat(got).isEqualTo(31)
        }

    }

    @Nested
    inner class Compute(){
        @Test
        fun `C200B40A82`(){
            val got = Packet.build("C200B40A82").compute()
            assertThat(got).isEqualTo(3)
        }

        @Test
        fun `04005AC33890`(){
            val got = Packet.build("04005AC33890").compute()
            assertThat(got).isEqualTo(54)
        }

        @Test
        fun `880086C3E88112`(){
            val got = Packet.build("880086C3E88112").compute()
            assertThat(got).isEqualTo(7)
        }

        @Test
        fun `CE00C43D881120`(){
            val got = Packet.build("CE00C43D881120").compute()
            assertThat(got).isEqualTo(9)
        }

        @Test
        fun `D8005AC2A8F0`(){
            val got = Packet.build("D8005AC2A8F0").compute()
            assertThat(got).isEqualTo(1)
        }

        @Test
        fun `F600BC2D8F`(){
            val got = Packet.build("F600BC2D8F").compute()
            assertThat(got).isEqualTo(0)
        }

        @Test
        fun `9C005AC2F8F0`(){
            val got = Packet.build("9C005AC2F8F0").compute()
            assertThat(got).isEqualTo(0)
        }
        @Test
        fun `9C0141080250320F1802104A08`(){
            val got = Packet.build("9C0141080250320F1802104A08").compute()
            assertThat(got).isEqualTo(1)
        }


    }
}
