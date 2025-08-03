package jp.harashio.image_api.filter

import com.google.api.client.util.Base64
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SigningKeyResolverAdapter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jp.harashio.image_api.domain.db.User
import jp.harashio.image_api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.util.AntPathMatcher
import java.io.ByteArrayInputStream
import java.security.Key
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


@Component
class LoginFilter : OncePerRequestFilter() {

    private val excludePatterns = listOf(
        "/api/v1/auth/firebase/login",
        "/api/v1/auth/firebase/signup",
        "/api/v1/public/images/**"
    )
    private val pathMatcher = AntPathMatcher()

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // コンテキストにログインユーザ情報をセット
        SecurityContextHolder.getContext().authentication = PreAuthenticatedAuthenticationToken(auth(request), null)

        filterChain.doFilter(request, response)
    }

    private fun auth(request: HttpServletRequest): User {
        val token = getToken(request)

        try {
            // JWTのクレームを取得
            val claim = Jwts.parser().setSigningKeyResolver(GoogleSigningKeyResolver())
                .parseClaimsJws(token)

            // uidを取得
            val uid = claim.body.get("user_id").toString()

            // ユーザ情報がDBに存在しない場合エラーを吐く
            val user = userRepository.findByUid(uid)
            if (user.isEmpty) {
                throw RuntimeException("User not found. (id=$uid)")
            }

            return user.get()

        } catch (e: Exception) {
            throw RuntimeException("Invalid token", e)
        }
    }

    // ログインユーザ情報の取得
    private fun getToken(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        // Implement logic to extract token from request headers or parameters
        if (token == null || !token.startsWith("Bearer ")) {
            return null
        }
        return token.substring("Bearer ".length)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return excludePatterns.any { pattern -> pathMatcher.match(pattern, path) }
    }

    class GoogleSigningKeyResolver : SigningKeyResolverAdapter() {
        val webClient = WebClient.create()

        override fun resolveSigningKey(jwsHeader: JwsHeader<*>, claims: Claims): Key? {
            try {
                val map = getJwks()

                if (map == null || map.isEmpty()) {
                    return null
                }

                val keyValue: String? = map[jwsHeader.keyId].toString()
                    .replace("-----BEGIN CERTIFICATE-----\n", "")
                    .replace("-----END CERTIFICATE-----\n", "")
                    .replace("\\s".toRegex(), "")

                val inputStream = ByteArrayInputStream(Base64.decodeBase64(keyValue))
                val certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream) as X509Certificate


                return  certificate.publicKey
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        // GoogleからJWKSを取得
        private fun getJwks(): Map<String, String>? {
            return webClient.get()
                .uri("https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {})
                .block()
        }
    }


}

// --- コントローラ等でのLoginUser取得例 ---
//
// 1. SecurityContextHolderから直接取得
// import org.springframework.security.core.context.SecurityContextHolder
// val loginUser = SecurityContextHolder.getContext().authentication.principal as? LoginUser
//
// 2. コントローラの引数で取得
// import org.springframework.security.core.annotation.AuthenticationPrincipal
// @GetMapping("/me")
// fun me(@AuthenticationPrincipal loginUser: LoginUser): String {
//     return loginUser.id
// }
// ------------------------------------------
