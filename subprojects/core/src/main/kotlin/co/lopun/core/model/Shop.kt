package co.lopun.core.model

import java.time.LocalDateTime

data class Shop(
  val id: Int? = null,
  val name: String,
  val url: String,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)
