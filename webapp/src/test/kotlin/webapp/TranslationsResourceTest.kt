package webapp

import com.springexample.utils.Fixtures
import domain.translations.Translations
import domain.translations.TranslationsProvider
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TranslationsResource::class)
class TranslationsResourceTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var translationsProvider: TranslationsProvider

    private val TRANSLATIONS_RESPONSE = Fixtures.readJson("/translationsResponse.json")

    @Test
    fun `retrieve translations`() {
        `when`(translationsProvider.retrieve("es")).thenReturn(
            Translations(
                mapOf(
                    "key1" to "value1",
                    "key2" to "value2"
                )
            )
        )

        mvc.perform(
            get("/translations/es")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().json(TRANSLATIONS_RESPONSE))
    }
}