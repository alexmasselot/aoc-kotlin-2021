import java.io.File

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun inputLines(day:String, isSample:Boolean):List<String>{
     val tag = if (isSample) {
        "-sample"
    } else {
        ""
    }

    val filename = "src/main/kotlin/day$day/input$tag.txt";

    return File(filename).readLines()
}

fun CharSequence.splitIgnoreEmpty(vararg delimiters: String): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}
