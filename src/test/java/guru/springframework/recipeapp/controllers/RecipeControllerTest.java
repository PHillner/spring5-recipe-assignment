package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Notes;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebFluxTest(RecipeController.class)
@Import(ThymeleafAutoConfiguration.class)
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testGetRecipePage() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Notes notes = new Notes();
        notes.setId("3");
        notes.setRecipeNotes("Might be cool recipe");
        recipe.setNotes(notes);

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        webTestClient.get()
                .uri("/recipe/" + recipe.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));
    }

    @Test
    public void testGetRecipePageNotFound() {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        webTestClient.get()
                .uri("/recipe/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));
    }

    @Test
    public void testNewRecipePage() {
        Recipe recipe = new Recipe();

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        webTestClient.get()
                .uri("/recipe/new")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));
    }

    @Test
    public void testSaveRecipe() {
        RecipeCommand command = new RecipeCommand();

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

        webTestClient.post()
                .uri(String.format("/recipe?%s&%s&%s&%s&%s&%s&%s",
                        "id=",
                        "description=great+description",
                        "prepTime=1",
                        "cookTime=8",
                        "servings=1",
                        "url=https://google.com",
                        "directions=Cook+it+rightly!"))
                .exchange()
                .expectStatus().is3xxRedirection();

        verify(recipeService).saveRecipeCommand(any());
    }

    @Test
    public void testSaveRecipeHasError() {
        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

        webTestClient.post()
                .uri(String.format("/recipe?%s&%s",
                        "id=",
                        "description=great+description"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(recipeService, times(0)).saveRecipeCommand(any());
    }

    @Test
    public void testEditRecipePage() {
        RecipeCommand command = new RecipeCommand();
        command.setId("3");

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

        webTestClient.get()
                .uri("/recipe/" + command.getId() + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));
    }

    @Test
    public void testRemoveRecipe() {
        when(recipeService.deleteById(anyString())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/recipe/4/remove")
                .exchange()
                .expectStatus().is3xxRedirection();

        verify(recipeService).deleteById(anyString());
    }

}
