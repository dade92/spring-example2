package webapp

import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName
import domain.usecases.FindCustomerUseCase
import domain.usecases.InsertCustomerUseCase
import domain.usecases.UpdateCustomerUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
        insertCustomerUseCase.insert(userRequestAdapter.adapt(insertCustomerRequest)).fold(
            {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Unit>()
            },
            {
                ResponseEntity.ok(InsertCustomerResponse(it.value))
            }
        )

    @PostMapping("/remove/{id}")
    fun remove(
        @PathVariable id: String
    ): ResponseEntity<*> =
        insertCustomerUseCase.remove(id.toId()).fold(
            {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Unit>()
            },
            {
                ResponseEntity.noContent().build()
            }
        )

    @GetMapping("/find")
    fun find(
        @RequestParam name: String
    ): ResponseEntity<Customer> =
        findCustomerUseCase.findBy(name.toName()).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.ok(it)
            }
        )

    @RequestMapping(method = [RequestMethod.PUT], path = ["/add-destination/{id}"])
    fun addDestination(
        @PathVariable id: String,
        @RequestBody addDestinationRequest: UpdateRequest
    ): ResponseEntity<Customer> =
        updateCustomerUseCase.addDestination(id.toId(), Destination(addDestinationRequest.destination)).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.noContent().build()
            }
        )

    @RequestMapping(method = [RequestMethod.PUT], path = ["/remove-destination/{id}"])
    fun removeDestination(
        @PathVariable id: String,
        @RequestBody updateRequest: UpdateRequest,
    ): ResponseEntity<Customer> =
        updateCustomerUseCase.removeDestination(id.toId(), Destination(updateRequest.destination)).fold(
            {
                ResponseEntity.notFound().build()
            },
            {
                ResponseEntity.noContent().build()
            }
        )

    @RequestMapping(method = [RequestMethod.PUT], path = ["/update-destination/{id}"])
    fun updateDestination(
        @PathVariable id: String,
        @RequestBody updateDestinationRequest: UpdateDestinationRequest,
    ): ResponseEntity<Customer> =
        updateCustomerUseCase.updateDestination(
            id.toId(),
            Destination(updateDestinationRequest.old),
            Destination(updateDestinationRequest.new)
        ).fold(
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
}

data class InsertCustomerRequest(
    val name: String,
    val age: Int,
    val favouriteDestinations: FavouriteDestinations
)

data class InsertCustomerResponse(
    val id: String
)

data class UpdateDestinationRequest(
    val old: String,
    val new: String
)

data class UpdateRequest(
    val destination: String
)