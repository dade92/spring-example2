package adapters.configuration

import adapters.customers.DynamoDbCustomersRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import domain.repository.CustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Configuration
class AdaptersConfiguration {

    @Bean
    fun customerRepository(
        mongoTemplate: MongoTemplate,
        dynamoDbClient: DynamoDbClient,
    ): CustomerRepository = DynamoDbCustomersRepository(dynamoDbClient, "Customer", ObjectMapper().registerModule(KotlinModule.Builder().build()))

}