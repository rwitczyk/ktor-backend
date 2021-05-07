package com.rwitczyk

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.rwitczyk.plugins.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.Database
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        initDB()

        install(CORS)
        {
            method(HttpMethod.Options)
            header(HttpHeaders.XForwardedProto)
            anyHost()

            allowCredentials = true
            allowNonSimpleContentTypes = true
            maxAge = Duration.ofDays(1)
        }
    }.start(wait = true)
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
}
