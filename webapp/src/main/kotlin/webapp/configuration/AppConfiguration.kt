package webapp.configuration

import domain.repository.CustomerRepository
import domain.translations.StaticTranslationsProvider
import domain.translations.TranslationsProvider
import domain.usecases.*
import domain.utils.RandomUUIDGenerator
import domain.utils.UUIDGenerator
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