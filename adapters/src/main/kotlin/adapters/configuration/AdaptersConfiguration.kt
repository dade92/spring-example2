package adapters.configuration

import adapters.MongoDBCustomerRepository
import domain.repository.CustomerRepository
import domain.utils.DefaultTimeProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class AdaptersConfiguration {

    @Bean
    fun customerRepository(
        mongoTemplate: MongoTemplate
    ): CustomerRepository = MongoDBCustomerRepository(mongoTemplate, DefaultTimeProvider())

}