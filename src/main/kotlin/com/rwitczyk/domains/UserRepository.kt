package com.rwitczyk.domains

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = uuid("id").primaryKey()
    val login = text("login")
    val password = text("password")
    val firstname = text("firstname")
    val lastname = text("lastname")
    val age = integer("age")
}
