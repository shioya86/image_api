package jp.harashio.image_api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

enum class EnvironmentType {
    DEVELOP,
    PRODUCTION
}

@Configuration
@Profile(value = ["local", "develop"])
class DevelopProfileConfig {

    @Bean("environmentType")
    fun environmentType(): EnvironmentType {
        return EnvironmentType.DEVELOP
    }
}

@Configuration
@Profile("production")
class ProductionProfileConfig {

    @Bean("environmentType")
    fun environmentType(): EnvironmentType {
        return EnvironmentType.PRODUCTION
    }
}
