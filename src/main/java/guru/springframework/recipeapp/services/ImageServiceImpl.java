package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
        log.debug("Saving image");
        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    try {
                        Byte[] byteObjects = new Byte[file.getBytes().length];

                        int i = 0;
                        for (byte b : file.getBytes()){
                            byteObjects[i++] = b;
                        }

                        recipe.setImage(byteObjects);
                        return recipe;
                    } catch (IOException e) {
                        log.error("Failed to save image", e);
                        throw new RuntimeException("Failed to save image");
                    }
                });

        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }

    public Mono<byte[]> getOnionsImage() {
        URL url = this.getClass().getClassLoader()
                .getResource("static/images/sliced-onion-420x420.jpg");
        try (Reader reader = new FileReader(url.getFile(), UTF_8)) {
            return Mono.just( FileCopyUtils.copyToByteArray(new FileInputStream(url.getFile())) );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
