package jp.harashio.image_api.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import jp.harashio.image_api.domain.response.IdentityTookitSigninEmailResponse
import jp.harashio.image_api.domain.request.ImageBase64UploadRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import jp.harashio.image_api.domain.request.FirebaseAuthRequest
import org.springframework.beans.factory.annotation.Value

@Service
class FirebaseAuthService {

    @Value("\${services.auth.firebase.apiKey}")
    lateinit var apiKey: String

    @Value("\${services.auth.firebase.baseUri}")
    lateinit var baseUri: String

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

        return response ?: throw IllegalStateException("Failed to sign up user with Firebase")
    }
}
