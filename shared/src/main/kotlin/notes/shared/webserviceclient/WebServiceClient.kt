// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.webserviceclient

/**
 * Sprint Client Demo
 * Copyright (c) 2022 Jeff Avery <jeffery.avery@uwaterloo.ca>
 *
 * This file may be freely distributed as long as this header remains intact.
 *
 * Demo for CS 346 that demonstrates the use of POST and GET methods.
 * These are pure HTTP requests and could work against any server that can
 * to handle this message class encoded as JSON.
 */

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

const val SERVER_ADDRESS = "http://127.0.0.1:8080/notes"
class WebServiceClient {
    fun get(): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_ADDRESS))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun get(id: Long): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.fromUriString(SERVER_ADDRESS).path("/{id}").buildAndExpand(id).toUri())
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun post(note: String): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_ADDRESS))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(note))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun put(id: Int, note: String): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.fromUriString(SERVER_ADDRESS).path("/{id}").buildAndExpand(id).toUri())
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(note))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun delete(id: Int): String {
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.fromUriString(SERVER_ADDRESS).path("/{id}").buildAndExpand(id).toUri())
            .header("Content-Type", "application/json")
            .DELETE()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}