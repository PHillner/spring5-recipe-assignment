package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.services.ImageService;
import guru.springframework.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));

        log.debug("Returning image upload form, id: " + id);
        return "recipe/image_upload_form";
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){
        imageService.saveImageFile(id, file);

        log.debug("Uploaded image, id: " + id);
        return String.format("redirect:/recipe/%s", id);
    }

    @GetMapping("recipe/{id}/recipe-image")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(id).block();

        if (recipeCommand.getImage() != null && recipeCommand.getImage().length > 0) {
            log.debug("Fetching image, id: " + recipeCommand.getId());
            byte[] byteArray = new byte[recipeCommand.getImage().length];
            int i = 0;

            for (Byte wrappedByte : recipeCommand.getImage()){
                byteArray[i++] = wrappedByte;
            }

            returnImage(response, byteArray);
        }
    }

    /**
     * Used in error pages to simulate crying
     * @param response
     * @throws IOException
     */
    @GetMapping("error/onions")
    public void renderOnionsImage(HttpServletResponse response) throws IOException {
        log.debug("Fetching error image - onions");
        returnImage(response, imageService.getOnionsImage());
    }

    private void returnImage(HttpServletResponse response, byte[] image) throws IOException {
        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(image);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }
}
