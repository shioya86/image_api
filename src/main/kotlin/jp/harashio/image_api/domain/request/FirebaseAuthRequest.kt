package jp.harashio.image_api.domain.request

data class FirebaseAuthRequest(
    val email: String,
    val password: String
)