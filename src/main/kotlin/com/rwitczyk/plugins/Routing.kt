package com.rwitczyk.plugins

import com.rwitczyk.UserController
import com.rwitczyk.domains.UserDTO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    val userController = UserController();

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/user") {
            val userDto = call.receive<UserDTO>()
            userController.insert(userDto);
            call.respond(HttpStatusCode.Created)
        }
    }
}
