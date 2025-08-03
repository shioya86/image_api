package jp.harashio.image_api.service

import jp.harashio.image_api.dto.db.User
import jp.harashio.image_api.dto.response.IdentityTookitSigninEmailResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import jp.harashio.image_api.dto.request.FirebaseAuthRequest
import jp.harashio.image_api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FirebaseAuthService {

    @Value("\${services.auth.firebase.apiKey}")
    lateinit var apiKey: String

    @Value("\${services.auth.firebase.baseUri}")
    lateinit var baseUri: String

    @Autowired
    private lateinit var userRepository: UserRepository

    fun authenticate(request: FirebaseAuthRequest): IdentityTookitSigninEmailResponse {
        // Firebase REST APIでemail/password認証
        val uri = "$baseUri:signInWithPassword?key=$apiKey"

        val webClient = WebClient.create()
        val response: IdentityTookitSigninEmailResponse? = webClient.post()
            .uri(uri)
            .bodyValue(
                mapOf(
                    "email" to request.email,
                    "password" to request.password,
                    "returnSecureToken" to true
                )
            )
            .retrieve()
            .bodyToMono(IdentityTookitSigninEmailResponse::class.java)
            .block()

        return response ?: throw IllegalStateException("Failed to authenticate with Firebase")
    }

    fun signUp(email: String, password: String): IdentityTookitSigninEmailResponse {
        // Firebase REST APIでユーザー新規作成
        val uri = "$baseUri:signUp?key=$apiKey"

        val webClient = WebClient.create()
        val response: IdentityTookitSigninEmailResponse? = webClient.post()
            .uri(uri)
            .bodyValue(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "returnSecureToken" to true
                )
            )
            .retrieve()
            .bodyToMono(IdentityTookitSigninEmailResponse::class.java)
            .block()

        if (response?.idToken.isNullOrEmpty()) {
            throw IllegalStateException("Failed to sign up user")
        } else {
            // Firebase でユーザ作成に成功した場合、ユーザ情報をデータベースに保存
            val user = User(
                null,
                uid = response!!.localId
            )
            userRepository.save(user)
        }

        return response ?: throw IllegalStateException("Failed to sign up with Firebase")
    }
}
