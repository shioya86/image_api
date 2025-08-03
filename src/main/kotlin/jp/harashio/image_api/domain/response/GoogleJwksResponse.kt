package jp.harashio.image_api.domain.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleJwksResponse (
    @JsonProperty("iss")
    val iss: String,

    @JsonProperty("aud")
    val aud: String,

    @JsonProperty("auth_time")
    val authTime: Long,

    @JsonProperty("user_id")
    val userId: String,

    @JsonProperty("sub")
    val sub: String,

    @JsonProperty("iat")
    val iat: Long,

    @JsonProperty("exp")
    val exp: Long,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("email_verified")
    val emailVerified: Boolean,

    @JsonProperty("firebase")
    val firebase: GoogleJwksFirebaseInnnerResponse,

    @JsonProperty("sign_in_provider")
    val signInProvider: String
) {
    data class GoogleJwksFirebaseInnnerResponse(
        @JsonProperty("identities")
        val identities: Map<String, List<String>>
    )
}