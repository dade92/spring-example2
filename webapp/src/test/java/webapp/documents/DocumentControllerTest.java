package webapp.documents;

import data.Post;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    private static final String UPLOAD_RESPONSE = "{\"imageLocation\": \"http://example.com\"}";
    private static final String READ_RESPONSE = "{\"posts\": [{\"name\": \"one\", \"imageLocation\": \"me.com\"}, {\"name\": \"two\", \"imageLocation\": \"you.com\"}]}";

    @Test
    void upload() throws Exception {
        String url = "http://example.com";

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "file",
            "filename",
            "Text/plain",
            "some xml".getBytes()
        );

        when(documentService.upload(any())).thenReturn(new ImageLocation(url));

        mockMvc.perform(
            multipart("/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json(UPLOAD_RESPONSE));
    }

    @Test
    void uploadFailsWithExceptionThrown() throws Exception {
        String url = "http://example.com";

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "file",
            "filename",
            "Text/plain",
            "some xml".getBytes()
        );

        when(documentService.upload(any())).thenThrow(new RuntimeException());

        mockMvc.perform(
            multipart("/upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().is5xxServerError());
    }

    @Test
    void retrievePosts() throws Exception {
        when(documentService.readPosts()).thenReturn(
            Arrays.asList(
                new Post("one", new ImageLocation("me.com")),
                new Post("two", new ImageLocation("you.com"))
            )
        );

        mockMvc.perform(
            get("/posts")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json(READ_RESPONSE));
    }

    @Test
    void retrievePostsFails() throws Exception {
        when(documentService.readPosts()).thenThrow(new RuntimeException());

        mockMvc.perform(
            get("/posts")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is5xxServerError());
    }
}