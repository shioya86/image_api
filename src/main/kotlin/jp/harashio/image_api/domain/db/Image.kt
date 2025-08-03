package jp.harashio.image_api.domain.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Timestamp

@Entity
@Table(name = "\"image\"")
data class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val path: String = "",

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long? = null,

    @Column(name = "created_at", insertable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at", insertable = false)
    val updatedAt: Timestamp? = null
)