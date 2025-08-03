package jp.harashio.image_api.repository

import jp.harashio.image_api.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUid(uid: String): Optional<User>
}