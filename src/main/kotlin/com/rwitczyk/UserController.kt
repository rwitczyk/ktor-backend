package com.rwitczyk;

import com.rwitczyk.domains.UserDTO
import com.rwitczyk.domains.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserController {

    fun insert(user: UserDTO) {
        transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[age] = user.age
                it[firstname] = user.firstName
                it[lastname] = user.lastName
            }
        }
    }
}
