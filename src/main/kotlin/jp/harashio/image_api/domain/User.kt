package jp.harashio.image_api.domain

import jakarta.persistence.*

@Entity
@Table(name = "\"user\"")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val uid: String = "",

    @Column(name = "created_at")
    val createdAt: java.sql.Timestamp? = null,

    @Column(name = "updated_at")
    val updatedAt: java.sql.Timestamp? = null
)