package webapp

import domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    ): ResponseEntity<Customer> =
        findCustomerUseCase.findBy(name).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.ok(it)
            }
        )

    @GetMapping("/findById")
    fun findById(
        @RequestParam id: String
    ): ResponseEntity<Customer> =
        findCustomerUseCase.findById(Id(id)).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.ok(it)
            }
        )

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