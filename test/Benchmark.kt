import mc.xalitoria.color.*
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.assertTrue

class Benchmark {

    val initSize = 10_000
    val size: Int = initSize / 2
    val stringSize = 128

    fun benchmark(array: Array<String>, outlier: Long, doThing: (String).() -> String) {
        var fastest = outlier
        var slowest = 0L
        println("Allocating $size strings of $stringSize characters")
        repeat(initSize) { array.random().doThing() }
        var index = 0
        println("Benchmarking...")
        System.out.flush()
        array.mapNotNull {
            val time = measureNanoTime {
                it.doThing()
            }
            if (time < fastest) {
                if (slowest == 0L) slowest = time
                println("Record: $time ns, Remaining: ${size - index}")
                System.out.flush()
                fastest = time
            }
            index++
            time.takeIf { it <= outlier }//.also { nulled -> nulled ?: println(result) }
        }.also {
            println("Test Complete")
            println("    // Records: ${it.size} / $size")
        }.average().roundToLong().let { println("    // Average: $it" + "ns") }
        println("    // Fastest: $fastest" + "ns")
        println("    // Slowest: $slowest" + "ns")
    }

    val chars: CharArray = listOf(' ').plus('a'..'z').plus('A'..'Z').plus('0'..'9').toCharArray()
    val codes = ('a'..'f').plus('k'..'o').plus('r').toCharArray()
    fun generateColorCodeArray(stringSize: Int = this.stringSize, arraySize: Int = this.size) = Array(arraySize) { buildString {
        var last = ""
        while(toString().stripBukkitCodes().length < stringSize) {
            val inner = buildString {
                if (Random.nextBoolean()) append(("&" + codes.random()).also { last = it })
                repeat(Random.nextInt(0, 5)) { append(chars.random()) }
            }
            //println(inner)
            when (Random.nextInt(0, 2)) {
                0 -> inner
                1 -> last
                else -> ""
            }.let { append(it) }
        }
    }}.also {
        //println("Average Length: ${it.sumOf { it.length } / size}")
    }

    // Records: 44208 / 50000
    // Average: 1858ns
    // Fastest: 1600ns
    // Slowest: 1800ns
    fun applyColorCodes() {
        benchmark(generateColorCodeArray(), 2000) { applyColorsAndDecor() }
    }

    // Records: 43230 / 50000
    // Average: 3053ns
    // Fastest: 2700ns
    // Slowest: 3100ns
    fun `applyColorCodes and minifyColors`() {
        benchmark(generateColorCodeArray(), 3200) { applyColorsAndDecor().minifyColors() }
    }

    fun generateHexCodeArray(stringSize: Int = this.stringSize) = Array(size) { buildString {
        var last = ""
        while(toString().stripHexCodes().stripHexCodes().length < stringSize) {
            val inner = buildString {
                append("&#" + NamedColor.entries.random().color)
                repeat(Random.nextInt(0, 5)) { append(chars.random()) }
            }
            append(if (Random.nextBoolean()) inner else last)
            last = inner
        }
    }}.also {
        //println("Average Length: ${it.sumOf { it.length } / size}")
    }

    // Records: 42201 / 50000
    // Average: 2731ns
    // Fastest: 1700ns
    // Slowest: 2900ns
    // Initial: 3300ns
    fun applyHexCodes() {
        benchmark(generateHexCodeArray(), 13900) { applyBukkitColors() }
    }

    // Records: 42839 / 50000
    // Average: 7000ns
    // Fastest: 4300ns
    // Slowest: 8300ns
    fun `hexCodes to minifyColors`() {
        benchmark(generateHexCodeArray(64).toList().applyBukkitColors().toTypedArray(), 8500) { minifyColors() }
    }

    // Records: 41572 / 50000
    // Average: 9997ns
    // Fastest: 6100ns
    // Slowest: 11100ns
    fun `applyHexCodes and minifyColors`() {
        benchmark(generateHexCodeArray(64), 12000) { applyBukkitColors().minifyColors() }
    }

    lateinit var input: Array<String>
    fun before() {
        input = generateColorCodeArray(arraySize = 10000)
    }

    lateinit var output: Array<String>
    fun run() {
        output = input.applyColorsAndDecor()
        assertTrue(true)
    }

    fun after() {
        println(input.random())
        println(output.random())
    }
}
