package guru.springframework.recipeapp.controllers;

import guru.springframework.recipeapp.commands.RecipeCommand;
import guru.springframework.recipeapp.exceptions.NotFoundException;
import guru.springframework.recipeapp.model.Recipe;
import guru.springframework.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController controller;
    MockMvc mockMvc;

    @BeforeEach()
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetRecipePage() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId("1");
        when(recipeService.findById(anyString())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + recipe.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testGetRecipePageNotFound() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testNewRecipePage() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe_form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testSaveRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "great description")
                .param("prepTime", "1")
                .param("cookTime", "8")
                .param("servings", "1")
                .param("url", "https://google.com")
                .param("directions", "Cook it rightly!")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:recipe/" + command.getId()));
    }

    @Test
    public void testSaveRecipeHasError() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "great description")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe_form"));
    }

    @Test
    public void testEditRecipePage() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("3");

        when(recipeService.findCommandById(anyString())).thenReturn(command);

        mockMvc.perform(get("/recipe/" + command.getId() + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe_form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testRemoveRecipe() throws Exception {

        mockMvc.perform(get("/recipe/4/remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService).deleteById(anyString());
    }

}
