package com.rwitczyk;

import com.rwitczyk.domains.Users
import com.rwitczyk.dto.EditUserPasswordDTO
import com.rwitczyk.dto.LoginUserDTO
import com.rwitczyk.dto.UpdateUserDataDTO
import com.rwitczyk.dto.UserDTO
import com.rwitczyk.utils.PasswordValidator
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserService {

    fun login(userLoginDTO: LoginUserDTO): String {
        var userId = "";
        transaction {
            val query = Users.selectAll()
            query.forEach {
                if (it[Users.login] == userLoginDTO.login) {
                    if (BCrypt.checkpw(userLoginDTO.password, it[Users.password])) {
                        userId = it[Users.id].toString()
                        return@transaction
                    }
                }
            }
            throw Throwable("Wrong login or password")
        }

        return userId;
    }

    fun addUserAccount(user: UserDTO) {
        if (!isLoginUnique(user.login)) {
            throw Throwable("User already exists, login: " + user.login)
        }

        val passwordValidator = PasswordValidator();
        if (!passwordValidator.isPasswordStrongEnough(user.password)) {
            throw Throwable("Password weak: " + user.password)
        }

        transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[login] = user.login
                it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
                it[age] = user.age
                it[firstname] = user.firstName
                it[lastname] = user.lastName
            }
        }
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

    fun editUserPassword(editUserPasswordDTO: EditUserPasswordDTO, id: UUID) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[password] = BCrypt.hashpw(editUserPasswordDTO.password, BCrypt.gensalt())
            }
        }
    }
}
