package guru.springframework.recipeapp.services;

import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeService) {
        this.recipeRepository = recipeService;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Saving image");
        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            log.error("Failed to save image", e);
        }
        log.debug("Image saved");
    }
}
