package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.services.IngredientService;
import guru.springframework.recipeapp.services.RecipeService;
import guru.springframework.recipeapp.services.UnitOfMeasureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebFluxTest(IngredientController.class)
@Import(ThymeleafAutoConfiguration.class)
class IngredientControllerTest {

    @MockBean
    IngredientService ingredientService;

    @MockBean
    UnitOfMeasureService unitOfMeasureService;

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testGetIngredientList() {
        RecipeCommand command = new RecipeCommand();

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

        webTestClient.get()
                .uri("/recipe//1/ingredients")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(recipeService).findCommandById(anyString());
    }

    @Test
    void testShowIngredient() {
        IngredientCommand command = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(command));

        webTestClient.get()
                .uri("/recipe/1/ingredient/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(ingredientService).findByRecipeIdAndIngredientId(anyString(), anyString());
    }

    @Test
    void testNewIngredient() {
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/recipe/1/ingredient/new")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    void testEditIngredient() {
        IngredientCommand command = new IngredientCommand();
        command.setId("4");
        command.setRecipeId("2");

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).thenReturn(Mono.just(command));
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/recipe/1/ingredient/4/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(ingredientService).findByRecipeIdAndIngredientId(anyString(), anyString());
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    void testSaveOrUpdateIngredient() {
        IngredientCommand command = new IngredientCommand();
        command.setId("4");
        command.setDescription("great description");
        command.setAmount(BigDecimal.valueOf(3L));
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId("5");
        command.setUom(uomCommand);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(command));

        webTestClient.post()
                .uri(String.format("/recipe/2/ingredient?%s&%s&%s&%s",
                        "id=",
                        "description=great+description",
                        "amount=3",
                        "uom.id=5"))
                .exchange()
                .expectStatus().is3xxRedirection();

        verify(ingredientService).saveIngredientCommand(any());
    }

    @Test
    public void testSaveRecipeHasError() {
        when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(String.format("/recipe/2/ingredient?%s&%s&%s&%s",
                        "id=4",
                        "description=",
                        "amount=3",
                        "uom.id=5"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));

        verify(ingredientService, times(0)).saveIngredientCommand(any());
    }

    @Test
    void testDeleteIngredient() {
        when(ingredientService.deleteById(anyString(), anyString())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/recipe/4/ingredient/23/remove")
                .exchange()
                .expectStatus().is3xxRedirection();

        verify(ingredientService).deleteById(anyString(), anyString());
    }

    @Test
    void testRecipeNotFoundException() {
        when(recipeService.findCommandById(anyString())).thenThrow(NotFoundException.class);

        webTestClient.get()
                .uri("/recipe/hello/ingredients")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(response -> assertNotNull(response.getResponseBody()));
    }
}
