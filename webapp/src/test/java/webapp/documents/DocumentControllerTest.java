package webapp.documents;

import documents.DocumentService;
import documents.ImageLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    private static final String UPLOAD_RESPONSE = "{\"imageLocation\": \"http://example.com\"}";

    @Test
    void upload() throws Exception {
        String url = "http://example.com";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename", "Text/plain", "some xml".getBytes());

        when(documentService.upload(any())).thenReturn(new ImageLocation(url));

        mockMvc.perform(
            multipart("/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json(UPLOAD_RESPONSE));
    }

}