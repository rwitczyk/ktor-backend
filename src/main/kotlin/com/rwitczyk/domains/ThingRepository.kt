package com.rwitczyk.domains

import org.jetbrains.exposed.sql.Table

object Things : Table() {
    val id = uuid("id").primaryKey()
    val name = text("name")
    val userId = (uuid("userId") references Users.id).nullable() // relacja manyToOne
}
