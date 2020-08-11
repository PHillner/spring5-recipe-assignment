package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.config.WebConfig;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(WebConfig.class)
public class RouterFunctionTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testGetRecipes() {
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }
}
