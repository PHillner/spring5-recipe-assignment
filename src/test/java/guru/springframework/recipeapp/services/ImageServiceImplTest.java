package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        imageService = new ImageServiceImpl(recipeReactiveRepository);
    }

    @Test
    void saveImageFile() throws IOException {
        String id = "1";
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "test_image.png", "text/plain",
                "Some bytes from a file".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);

        Recipe recipeWithImage = new Recipe();
        recipeWithImage.setId(id);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImageFile(id, multipartFile).block();

        verify(recipeReactiveRepository).save(argumentCaptor.capture());
        Recipe recipeToBeSaved = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, recipeToBeSaved.getImage().length);
    }

    @Test
    void get404ImageFile() {
        assertDoesNotThrow(
                () -> imageService.getOnionsImage().block(),
                "Expected to get byte[] of an image"
        );
    }
}
