package webapp

import arrow.core.left
import arrow.core.right
import com.springexample.utils.Fixtures
import domain.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import webapp.adapters.UserRequestAdapter

@WebMvcTest(UserResource::class)
class UserResourceTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var insertCustomerUseCase: InsertCustomerUseCase

    @MockBean
    private lateinit var findCustomerUseCase: FindCustomerUseCase

    @MockBean
    private lateinit var updateCustomerUseCase: UpdateCustomerUseCase

    @MockBean
    private lateinit var userRequestAdapter: UserRequestAdapter

    private val INSERT_REQUEST = Fixtures.readJson("/insertRequest.json")
    private val ADD_DESTINATION_REQUEST = Fixtures.readJson("/updateRequest.json")
    private val FIND_RESPONSE = Fixtures.readJson("/findResponse.json")

    companion object {
        val request = InsertCustomerRequest(
            "Davide",
            30,
            FavouriteDestinations(
                listOf(Destination("Milan"))
            )
        )
    }

    @Test
    fun `insert successful`() {
        val uuid = "uuid"
        val customer = Customer(
            uuid.toId(),
            "Davide".toName(),
            30,
            FavouriteDestinations(listOf(Destination("Milan")))
        )

        `when`(userRequestAdapter.adapt(request)).thenReturn(customer)

        mvc.perform(
            MockMvcRequestBuilders.post("/insert")
                .content(INSERT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        verify(insertCustomerUseCase).insert(customer)
    }


    @Test
    fun `insert fails`() {
        val customer = Customer(
            "uuid".toId(),
            "Davide".toName(),
            30,
            FavouriteDestinations(listOf(Destination("Milan")))
        )

        `when`(userRequestAdapter.adapt(request)).thenReturn(customer)
        `when`(insertCustomerUseCase.insert(customer)).thenThrow(RuntimeException())

        mvc.perform(
            MockMvcRequestBuilders.post("/insert")
                .content(INSERT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is5xxServerError)
    }

    @Test
    fun `find successful`() {
        `when`(findCustomerUseCase.findBy("Davide".toName())).thenReturn(
            Customer(
                "uuid".toId(),
                "Davide".toName(),
                30,
                FavouriteDestinations(listOf(Destination("Milan")))
            ).right()
        )

        mvc.perform(
            MockMvcRequestBuilders.get("/find?name=Davide")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().json(FIND_RESPONSE))

        verify(findCustomerUseCase).findBy("Davide".toName())
    }

    @Test
    fun `find fails`() {
        `when`(findCustomerUseCase.findBy("Davide".toName())).thenReturn(CustomerNotFoundError.left())

        mvc.perform(
            MockMvcRequestBuilders.get("/find?name=Davide")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

        verify(findCustomerUseCase).findBy("Davide".toName())
    }

    @Test
    fun `update customer destination successful`() {
        val name = "ciccio"

        `when`(updateCustomerUseCase.addDestination(name, Destination("London"))).thenReturn(Unit.right())

        mvc.perform(
            MockMvcRequestBuilders.put("/add-destination/ciccio")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        verify(updateCustomerUseCase).addDestination(name, Destination("London"))
    }

    @Test
    fun `update customer destination error`() {
        val name = "ciccio"

        `when`(
            updateCustomerUseCase.addDestination(
                name,
                Destination("London")
            )
        ).thenReturn(CustomerNotFoundError.left())

        mvc.perform(
            MockMvcRequestBuilders.put("/add-destination/ciccio")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

        verify(updateCustomerUseCase).addDestination(name, Destination("London"))
    }

    @Test
    fun `remove customer destination successful`() {
        val name = "ciccio"

        `when`(updateCustomerUseCase.removeDestination(name, Destination("London"))).thenReturn(Unit.right())

        mvc.perform(
            MockMvcRequestBuilders.put("/remove-destination/ciccio")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        verify(updateCustomerUseCase).removeDestination(name, Destination("London"))
    }

    @Test
    fun `remove customer destination error`() {
        val name = "ciccio"

        `when`(
            updateCustomerUseCase.removeDestination(
                name,
                Destination("London")
            )
        ).thenReturn(CustomerNotFoundError.left())

        mvc.perform(
            MockMvcRequestBuilders.put("/remove-destination/ciccio")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

        verify(updateCustomerUseCase).removeDestination(name, Destination("London"))
    }
}