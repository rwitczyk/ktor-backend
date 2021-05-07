package com.rwitczyk;

import com.rwitczyk.domains.Users
import com.rwitczyk.dto.UpdateUserDataDTO
import com.rwitczyk.dto.UserDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class UserService {

    fun addUserAccount(user: UserDTO) {
        if (!isLoginUnique(user.login)) {
            throw Throwable("User already exists, login: " + user.login)
        }

        if (!isPasswordStrongEnough(user.password)) {
            throw Throwable("Password weak: " + user.password)
        }

        transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[login] = user.login
                it[password] = user.password
                it[age] = user.age
                it[firstname] = user.firstName
                it[lastname] = user.lastName
            }
        }
    }

    private fun isPasswordStrongEnough(password: String): Boolean {
        if (password.length < 5) {
            return false
        }

        if (!password.contains(Regex("[A-Z]+"))) {
            return false
        }

        val specialCharacters: Set<Char> = HashSet(
            listOf(
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'
            )
        )
        var hasAnySpecialChar = false;
        for (i in password.toCharArray()) {
            if (specialCharacters.contains(i)) {
                hasAnySpecialChar = true;
            }
        }

        return hasAnySpecialChar;
    }

    private fun isLoginUnique(login: String): Boolean {
        var loginUnique = true;
        transaction {
            val query = Users.selectAll()
            query.forEach {
                if (it[Users.login] == login) {
                    loginUnique = false;
                }
            }
        }

        return loginUnique;
    }

    fun updateUserData(userDataDTO: UpdateUserDataDTO, id: UUID) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[age] = userDataDTO.age
                it[firstname] = userDataDTO.firstName
                it[lastname] = userDataDTO.lastName
            }
        }
    }
}
