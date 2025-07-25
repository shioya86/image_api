package jp.harashio.image_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ImageApiApplication

fun main(args: Array<String>) {
    runApplication<ImageApiApplication>(*args)
}
