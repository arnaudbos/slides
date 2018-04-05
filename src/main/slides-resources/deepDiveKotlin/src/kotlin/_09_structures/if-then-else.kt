package _09_structures

import astronomy.AstronomicalBody
import astronomy.Planet


fun handleAstronomicalBody(body: AstronomicalBody) {
    val message = if (body is Planet && body.name == "Earth") {
        "Welcome Earth"
    } else {
        "Welcome martian"
    }

    println(message)
}

