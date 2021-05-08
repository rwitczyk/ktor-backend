package com.rwitczyk;

import com.rwitczyk.domains.Users
import com.rwitczyk.dto.*
import com.rwitczyk.utils.PasswordValidator
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserService {
    val passwordValidator = PasswordValidator();

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

    fun getUserData(id: UUID): UserDataDTO {
        val userData: UserDataDTO = UserDataDTO("", "", "", 0);

        transaction {
            Users.select { Users.id eq id }.forEach {
                userData.login = it[Users.login]
                userData.firstName = it[Users.firstname]
                userData.lastName = it[Users.lastname]
                userData.age = it[Users.age]
            }
        }

        return userData;
    }

    fun editUserPassword(editUserPasswordDTO: EditUserPasswordDTO, id: UUID) {
        if (!passwordValidator.isPasswordStrongEnough(editUserPasswordDTO.password)) {
            throw Throwable("Password weak: " + editUserPasswordDTO.password)
        }

        transaction {
            Users.update({ Users.id eq id }) {
                it[password] = BCrypt.hashpw(editUserPasswordDTO.password, BCrypt.gensalt())
            }
        }
    }
}
