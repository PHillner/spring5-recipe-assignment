package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.services.ImageService;
import guru.springframework.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testShowUploadForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyString());
    }

    @Test
    void testHandleImagePost() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "test_image.png", "text/plain",
                        "Some bytes from a file".getBytes());

        when(imageService.saveImageFile(anyString(), any())).thenReturn(Mono.empty());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1"));

        verify(imageService).saveImageFile(anyString(), any());
    }

    @Test
    void testRenderImageFromDB() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        String s = "test image";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;

        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        command.setImage(bytesBoxed);

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

//        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipe-image"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//
//        byte[] responseBytes = response.getContentAsByteArray();
//
//        assertEquals(s.getBytes().length, responseBytes.length);
    }

    @Test
    void testRenderOnionsImage() throws Exception {
        byte[] bytes = "test image".getBytes();

        when(imageService.getOnionsImage()).thenReturn(Mono.just(bytes));

//        MockHttpServletResponse response = mockMvc.perform(get("/error/onions"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//        byte[] responseBytes = response.getContentAsByteArray();
//
//        assertTrue(responseBytes.length > 0, "Image byte count should be > 0");
    }

    @Test
    void testRecipeNotFoundException() throws Exception {
        when(recipeService.findCommandById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/hello/recipe-image"))
                .andExpect(status().isNotFound());
    }
}
