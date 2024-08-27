package adapters.customers

import adapters.dynamo.DynamoCustomer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Test

class DynamoDbCustomersRepositoryTest {

    @Test
    fun deserialize() {
        val objectMapper = ObjectMapper().registerModule(KotlinModule())
        objectMapper.readValue("""{"id":"77527c47-a3a9-4c43-8d33-28e6134a84f5","name":"Karissa","age":31}""", DynamoCustomer::class.java)
    }
}