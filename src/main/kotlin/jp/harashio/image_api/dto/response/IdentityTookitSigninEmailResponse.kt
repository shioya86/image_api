package jp.harashio.image_api.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class IdentityTookitSigninEmailResponse (
    @JsonProperty("kind")
    val kind: String,

    @JsonProperty("localId")
    val localId: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("displayName")
    val displayName: String? = null,

    @JsonProperty("idToken")
    val idToken: String,

    @JsonProperty("registered")
    val registered: Boolean,

    @JsonProperty("refreshToken")
    val refreshToken: String,

    @JsonProperty("expiresIn")
    val expiresIn: Integer
)