package jp.harashio.image_api.repository

import jp.harashio.image_api.domain.db.Image
import jp.harashio.image_api.domain.ImageVisibility
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
@Transactional
interface ImageRepository : JpaRepository<Image, Long> {

    @Modifying
    @Query("UPDATE Image i SET i.visibility = :visibility, i.updatedAt = CURRENT_TIMESTAMP WHERE i.id = :id")
    fun updateVisibility(
        @Param("id") id: Long,
        @Param("visibility") visibility: ImageVisibility
    ): Int

    fun findByIdAndUserId(id: Long, userId: Long): Optional<Image>

    fun findByFilename(filename: String): Optional<Image>

    @Query(
        "SELECT i FROM Image i WHERE i.userId = :userId AND i.visibility = :visibility ORDER BY i.id DESC"
    )
    fun findByUserIdAndVisibilityOrderByIdDesc(
        @Param("userId") userId: Long,
        @Param("visibility") visibility: ImageVisibility,
        pageable: Pageable
    ): List<Image>
}