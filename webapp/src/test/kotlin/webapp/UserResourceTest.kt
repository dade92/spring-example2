package webapp

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(UserResource::class)
class UserResourceTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var insertCustomerUseCase: InsertCustomerUseCase

    @MockBean
    private lateinit var findCustomerUseCase: FindCustomerUseCase

    @MockBean
    private lateinit var uuidGenerator: UUIDGenerator

    private val INSERT_REQUEST = Fixtures.readJson("/insertRequest.json")
    private val FIND_RESPONSE = Fixtures.readJson("/findResponse.json")

    @Test
    fun `insert successful`() {
        val uuid = "uuid"
        `when`(uuidGenerator.get()).thenReturn(uuid)

        mvc.perform(
            MockMvcRequestBuilders.post("/insert")
                .content(INSERT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(insertCustomerUseCase).insert(
            Customer(
                uuid.toId(),
                "Davide",
                30,
                FavouriteDestinations(listOf(Destination("Milan")))
            )
        )
    }


    @Test
    fun `insert fails`() {
        `when`(
            insertCustomerUseCase.insert(
                Customer("uuid".toId(), "Davide", 30, FavouriteDestinations(listOf(Destination("Milan"))))
            )
        ).thenThrow(RuntimeException())

        mvc.perform(
            MockMvcRequestBuilders.post("/insert")
                .content(INSERT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError)
    }

    @Test
    fun `find successful`() {
        `when`(findCustomerUseCase.findBy("Davide")).thenReturn(
            Customer(
                "uuid".toId(),
                "Davide",
                30,
                FavouriteDestinations(listOf(Destination("Milan")))
            ).right()
        )

        mvc.perform(
            MockMvcRequestBuilders.get("/find?name=Davide")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(FIND_RESPONSE))

        verify(findCustomerUseCase).findBy("Davide")
    }


}