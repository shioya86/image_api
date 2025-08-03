package jp.harashio.image_api.domain

enum class ImageVisibility(i: Int) {
    CLOSE(0),
    INTERNAL(1),
    OPEN(2);

    companion object {
        fun fromCode(code: Int): ImageVisibility =
            values().find { it.ordinal == code }
                ?: throw IllegalArgumentException("Unknown image visibility code: $code")
        fun fromString(name: String): ImageVisibility =
            values().find { it.name.equals(name, ignoreCase = true) }
                ?: throw java.lang.IllegalArgumentException("Unknown image visibility: $name")
    }
}