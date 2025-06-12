package com.jvdev.com.cep

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Endereco(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String
)

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // ignora campos extras da resposta do ViaCEP
        })
    }
}

suspend fun buscarEndereco(cep: String): Endereco {
    println(cep)
    val url = "https://viacep.com.br/ws/$cep/json/" // see https://viacep.com.br/
    return httpClient.get(url).body()
}