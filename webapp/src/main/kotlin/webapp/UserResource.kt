package webapp

import domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserResource(
    private val insertCustomerUseCase: InsertCustomerUseCase,
    private val findCustomerUseCase: FindCustomerUseCase
) {
    @PostMapping("/insert")
    fun insert(
        @RequestBody insertCustomerRequest: InsertCustomerRequest
    ): ResponseEntity<*> {
        return try {
            insertCustomerUseCase.insert(insertCustomerRequest.toDomain())
            ResponseEntity.noContent().build<Unit>()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Unit>()
        }
    }

    @GetMapping("/find")
    fun find(
        @RequestParam name: String
    ): ResponseEntity<Customer> {
        return ResponseEntity.ok(
            findCustomerUseCase.findBy(name)
        )
    }

    //TODO test
    @GetMapping("/retrieveUsers")
    fun retrieveAll(): ResponseEntity<CustomersResponse> {
        return ResponseEntity.ok(
            CustomersResponse(
                findCustomerUseCase.getAll()
            )
        )
    }

}

data class CustomersResponse(
    val users: List<Customer>
)

data class InsertCustomerRequest(
    val name: String,
    val age: Int,
    val favouriteDestinations: FavouriteDestinations
) {

    fun toDomain() = Customer(this.name, age, favouriteDestinations)

}