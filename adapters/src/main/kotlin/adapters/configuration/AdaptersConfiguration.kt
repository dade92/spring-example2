package adapters.configuration

import adapters.customers.DynamoCustomerAdapter
import adapters.customers.DynamoDbCustomersRepository
import adapters.dynamo.DynamoCustomer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import domain.repository.CustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


@Configuration
class AdaptersConfiguration {

    @Bean
    fun customerRepository(
        mongoTemplate: MongoTemplate,
        dynamoDbClient: DynamoDbClient,
    ): CustomerRepository {
        val dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()

        val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

        return DynamoDbCustomersRepository(
            dynamoDbEnhancedClient.table("Customer", TableSchema.fromBean(DynamoCustomer::class.java)),
            DynamoCustomerAdapter(objectMapper),
            objectMapper
        )
    }

}