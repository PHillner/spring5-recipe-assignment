package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebFluxTest(IndexController.class)
@Import(ThymeleafAutoConfiguration.class)
public class IndexControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testWebClient() {
        String description = "Some goodies";
        Recipe recipe = new Recipe();
        recipe.setDescription(description);

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = Objects.requireNonNull(response.getResponseBody());
                    assertTrue(responseBody.contains(description));
                });
    }

}
