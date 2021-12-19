package day19

enum class Permutation(val label: String) {
    X_PLUS("+x"),
    X_MINUS("-x"),
    Y_PLUS("+y"),
    Y_MINUS("-y"),
    Z_PLUS("+z"),
    Z_MINUS("-z");

    fun invert() =
        when (this) {
            X_PLUS -> X_MINUS
            Y_PLUS -> Y_MINUS
            Z_PLUS -> Z_MINUS
            X_MINUS -> X_PLUS
            Y_MINUS -> Y_PLUS
            Z_MINUS -> Z_PLUS
        }

}
