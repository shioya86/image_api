package jp.harashio.image_api.domain.db

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jp.harashio.image_api.domain.ImageVisibility
import java.sql.Timestamp

@Entity
@Table(name = "\"image\"")
data class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "filename", nullable = false, unique = true)
    val filename: String = "",

   @Column(name = "media")
   val media: String = "",

    @Column(nullable = false, unique = true)
    val path: String = "",

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long? = null,

    @Column(name = "visibility", nullable = false)
    @Convert(converter = VisibilityConverter::class)
    val visibility: ImageVisibility = ImageVisibility.CLOSE,

    @Column(name = "created_at", insertable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at", insertable = false)
    val updatedAt: Timestamp? = null
) {
    @Converter(autoApply = true)
    class VisibilityConverter : AttributeConverter<ImageVisibility, Int> {
        override fun convertToDatabaseColumn(attribute: ImageVisibility): Int = attribute.ordinal
        override fun convertToEntityAttribute(dbData: Int): ImageVisibility = ImageVisibility.fromCode(dbData)
    }
}
