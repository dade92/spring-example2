package adapters.configuration

import adapters.customers.dynamo.DynamoCustomerAdapter
import adapters.customers.dynamo.DynamoDbCustomersRepository
import adapters.customers.dynamo.DynamoCustomer
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

        return DynamoDbCustomersRepository(
            dynamoDbEnhancedClient.table(CUSTOMER_TABLE, TableSchema.fromBean(DynamoCustomer::class.java)),
            DynamoCustomerAdapter()
        )
    }

    companion object {
        private val CUSTOMER_TABLE = "Customer"
    }
}