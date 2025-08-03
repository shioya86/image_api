package jp.harashio.image_api.controller.auth

import jp.harashio.image_api.dto.response.IdentityTookitSigninEmailResponse
import jp.harashio.image_api.dto.request.FirebaseAuthRequest
import jp.harashio.image_api.service.FirebaseAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @Autowired
    private lateinit var authService: FirebaseAuthService

    @PostMapping("/firebase/login")
    fun authenticate(@RequestBody request: FirebaseAuthRequest): IdentityTookitSigninEmailResponse {
        return authService.authenticate(request)
    }

    @PostMapping("/firebase/signup")
    fun signUp(@RequestBody request: FirebaseAuthRequest): Map<String, String> {
        authService.signUp(request.email, request.password)

        return mapOf("status" to "ok")
    }
}