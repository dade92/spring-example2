package webapp

import domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import webapp.adapters.UserRequestAdapter

@RestController
class UserResource(
    private val insertCustomerUseCase: InsertCustomerUseCase,
    private val findCustomerUseCase: FindCustomerUseCase,
    private val userRequestAdapter: UserRequestAdapter,
    private val updateCustomerUseCase: UpdateCustomerUseCase
) {
    @PostMapping("/insert")
    fun insert(
        @RequestBody insertCustomerRequest: InsertCustomerRequest
    ): ResponseEntity<*> =
        try {
            insertCustomerUseCase.insert(userRequestAdapter.adapt(insertCustomerRequest))
            ResponseEntity.noContent().build<Unit>()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Unit>()
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

    @RequestMapping(method = [RequestMethod.PUT], path = ["/update/{name}"])
    fun updateCustomer(
        @PathVariable name: String
    ): ResponseEntity<Customer> =
        updateCustomerUseCase.update(name).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.noContent().build()
            }
        )

    @GetMapping("/findById")
    fun findById(
        @RequestParam id: String
    ): ResponseEntity<Customer> =
        findCustomerUseCase.findById(id.toId()).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.ok(it)
            }
        )

    //TODO test or delete?
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
)