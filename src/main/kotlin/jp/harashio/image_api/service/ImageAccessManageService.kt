package jp.harashio.image_api.service

import jp.harashio.image_api.domain.ImageVisibility
import jp.harashio.image_api.domain.db.Image
import jp.harashio.image_api.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

@Service
class ImageAccessManageService {

    @Autowired
    private lateinit var imageRepository: ImageRepository

    fun getUserPublicImagesLatest(userId: Long, limit: Int = 50): List<Image> {
        // ユーザの最新の画像を取得
        return imageRepository.findByUserIdAndVisibilityOrderByIdDesc(userId, ImageVisibility.OPEN, Pageable.ofSize(limit))
    }

    fun updateImageAccessStatus(userId: String, imageId: String, status: String) {
        // 編集する画像IDがユーザが権限を持つものかチェック
        val image: Image
        // ステータスが有効な値かどうかをチェック
        val visibility: ImageVisibility
        try {
            image = imageRepository.findByIdAndUserId(imageId.toLong(), userId.toLong())
                .orElseThrow { IllegalArgumentException("Image not found or access denied") }
            visibility = ImageVisibility.fromString(status)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid visibility status: $status", e)
        }

        if (image.visibility == visibility) {
            // 変更がない場合は何もしない
            return
        }
        imageRepository.updateVisibility(imageId.toLong(), visibility)
    }
}