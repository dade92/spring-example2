package webapp

import domain.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import webapp.adapters.UserRequestAdapter

@Configuration
class AppConfiguration {

    @Bean
    fun insertCustomerUseCase(customerRepository: CustomerRepository): InsertCustomerUseCase =
        DefaultInsertCustomerUseCase(customerRepository)

    @Bean
    fun findCustomerUseCase(customerRepository: CustomerRepository): FindCustomerUseCase =
        DefaultFindCustomerUseCase(customerRepository)

    @Bean
    fun updateCustomerUseCase(customerRepository: CustomerRepository): UpdateCustomerUseCase =
        DefaultUpdateCustomerUseCase(customerRepository)

    @Bean
    fun translationsProvider(): TranslationsProvider =
        StaticTranslationsProvider()

    @Bean
    fun uuidGenerator(): UUIDGenerator = RandomUUIDGenerator()

    @Bean
    fun userRequestAdapter(uuidGenerator: UUIDGenerator): UserRequestAdapter =
        UserRequestAdapter(uuidGenerator)

}