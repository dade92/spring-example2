package adapters.configuration

import adapters.MongoCustomerRepository
import domain.CustomerRepository
import domain.DefaultTimeProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class AdaptersConfiguration {

    @Bean
    fun customerRepository(
        mongoTemplate: MongoTemplate
    ): CustomerRepository = MongoCustomerRepository(mongoTemplate, DefaultTimeProvider())

}