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
    private val INSERT_RESPONSE = Fixtures.readJson("/insertResponse.json")
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
        `when`(insertCustomerUseCase.insert(customer)).thenReturn(uuid.toId().right())

        mvc.perform(
            MockMvcRequestBuilders.post("/insert")
                .content(INSERT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful).andExpect(content().json(INSERT_RESPONSE))

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
        `when`(insertCustomerUseCase.insert(customer)).thenReturn(GenericDbError.left())

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
        val id = "123"

        `when`(updateCustomerUseCase.addDestination(id.toId(), Destination("London"))).thenReturn(Unit.right())

        mvc.perform(
            MockMvcRequestBuilders.put("/add-destination/123")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        verify(updateCustomerUseCase).addDestination(id.toId(), Destination("London"))
    }

    @Test
    fun `update customer destination error`() {
        val id = "123"

        `when`(
            updateCustomerUseCase.addDestination(
                id.toId(),
                Destination("London")
            )
        ).thenReturn(CustomerNotFoundError.left())

        mvc.perform(
            MockMvcRequestBuilders.put("/add-destination/123")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

        verify(updateCustomerUseCase).addDestination(id.toId(), Destination("London"))
    }

    @Test
    fun `remove customer destination successful`() {
        val id = "123"

        `when`(updateCustomerUseCase.removeDestination(id.toId(), Destination("London"))).thenReturn(Unit.right())

        mvc.perform(
            MockMvcRequestBuilders.put("/remove-destination/123")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        verify(updateCustomerUseCase).removeDestination(id.toId(), Destination("London"))
    }

    @Test
    fun `remove customer destination error`() {
        val id = "123"

        `when`(
            updateCustomerUseCase.removeDestination(
                id.toId(),
                Destination("London")
            )
        ).thenReturn(CustomerNotFoundError.left())

        mvc.perform(
            MockMvcRequestBuilders.put("/remove-destination/123")
                .content(ADD_DESTINATION_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

        verify(updateCustomerUseCase).removeDestination(id.toId(), Destination("London"))
    }
}