package com.rwitczyk.plugins

import com.rwitczyk.UserService
import com.rwitczyk.dto.LoginUserDTO
import com.rwitczyk.dto.UpdateUserDataDTO
import com.rwitczyk.dto.UserDTO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Application.configureRouting() {

    val userService = UserService();

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/login") {
            val userLoginDTO = call.receive<LoginUserDTO>()
            userService.login(userLoginDTO)
            call.respond(HttpStatusCode.OK)
        }

        post("/users") {
            val userDto = call.receive<UserDTO>()
            userService.addUserAccount(userDto);
            call.respond(HttpStatusCode.Created)
        }

        put("/users/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            val userDto = call.receive<UpdateUserDataDTO>()
            userService.updateUserData(userDto, id);
            call.respond(HttpStatusCode.OK)
        }
    }
}
