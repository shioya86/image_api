package jp.harashio.image_api.repository

import jp.harashio.image_api.domain.db.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>