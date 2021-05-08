package com.rwitczyk.plugins

import com.rwitczyk.UserService
import com.rwitczyk.dto.EditUserPasswordDTO
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
        post("/editPassword/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            val editUserPasswordDTO = call.receive<EditUserPasswordDTO>()
            userService.editUserPassword(editUserPasswordDTO, id)
            call.respond(HttpStatusCode.OK)
        }

        post("/login") {
            val userLoginDTO = call.receive<LoginUserDTO>()
            val userId = userService.login(userLoginDTO)
            call.respond(HttpStatusCode.OK, mapOf("userId" to userId))
        }

        post("/users") {
            val userDto = call.receive<UserDTO>()
            userService.addUserAccount(userDto);
            call.respond(HttpStatusCode.Created)
        }

        post("/users/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            val userDto = call.receive<UpdateUserDataDTO>()
            userService.updateUserData(userDto, id);
            call.respond(HttpStatusCode.OK)
        }

        get("/users/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            val userData = userService.getUserData(id);
            call.respond(HttpStatusCode.OK, userData)
        }
    }
}
