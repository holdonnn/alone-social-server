package social.alone.server.image

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream


@RunWith(SpringRunner::class)
@WebMvcTest(ImageController::class, secure = false)
class ImageControllerTest(

) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var imageUploader: ImageUploader

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)


    @Test
    fun imageCreate() {
        val multipartFile = MockMultipartFile("test.jpg", FileInputStream(createTempFile()))
        val image = Image("imageUrl")
        image.id = "1234"
        given(
                imageUploader.upload(any(MultipartFile::class.java))
        ).willReturn(image)

        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/images")
                        .file("file", multipartFile.bytes)
        )

        perform.andExpect(status().isOk)
        perform.andDo(print())
    }
}