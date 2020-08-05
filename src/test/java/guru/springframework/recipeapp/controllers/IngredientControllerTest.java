package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.IngredientCommand;
import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.services.IngredientService;
import guru.springframework.recipeapp.services.RecipeService;
import guru.springframework.recipeapp.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    RecipeService recipeService;

    IngredientController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new IngredientController(ingredientService, recipeService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testGetIngredientList() throws Exception {
        RecipeCommand command = new RecipeCommand();

        when(recipeService.findCommandById(anyString())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyString());
    }

    @Test
    void testShowIngredient() throws Exception {
        IngredientCommand command = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/ingredient/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService).findByRecipeIdAndIngredientId(anyString(), anyString());
    }

    @Test
    void testNewIngredient() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(recipeService.findCommandById(anyString())).thenReturn(command);
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.empty());

        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredient_form"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService).findCommandById(anyString());
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    void testEditIngredient() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId("4");
        command.setRecipeId("2");

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).thenReturn(command);
        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.empty());

        mockMvc.perform(get("/recipe/2/ingredient/4/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredient_form"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(ingredientService).findByRecipeIdAndIngredientId(anyString(), anyString());
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    void testSaveOrUpdateIngredient() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId("4");
        command.setDescription("great description");

        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "great description")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:ingredient/" + command.getId()));

        verify(ingredientService).saveIngredientCommand(any());
    }

    @Test
    void testDeleteIngredient() throws Exception {
        mockMvc.perform(get("/recipe/4/ingredient/23/remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/4/ingredients"));

        verify(ingredientService).deleteById(anyString(), anyString());
    }

    @Test
    void testRecipeNotFoundException() throws Exception {
        when(recipeService.findCommandById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/hello/ingredients"))
                .andExpect(status().isNotFound());
    }
}
