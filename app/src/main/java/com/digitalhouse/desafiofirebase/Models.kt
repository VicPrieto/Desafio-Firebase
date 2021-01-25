package com.digitalhouse.desafiofirebase

import java.io.Serializable

data class Game (val name: String, val description: String, val year: Int, val img: String): Serializable

data class User (val name: String, val email: String, val password: String, val id: String = ""): Serializable