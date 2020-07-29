package guru.springframework.recipeapp.bootstrap;

import guru.springframework.recipeapp.model.*;
import guru.springframework.recipeapp.repositories.CategoryRepository;
import guru.springframework.recipeapp.repositories.RecipeRepository;
import guru.springframework.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@Profile("default")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading data");
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>();
        log.debug("Fetching units and categories to be used in recipes...");

        // Get UOMs
        Optional<UnitOfMeasure> teaspoonUnitOptional = unitOfMeasureRepository.findByDescription("tsp");
        if (teaspoonUnitOptional.isEmpty()) throw new RuntimeException("Teaspoon unit not in db");
        UnitOfMeasure teaspoonUnit = teaspoonUnitOptional.get();
        Optional<UnitOfMeasure> tablespoonUnitOptional = unitOfMeasureRepository.findByDescription("tblsp");
        if (tablespoonUnitOptional.isEmpty()) throw new RuntimeException("Tablespoon unit not in db");
        UnitOfMeasure tablespoonUnit = tablespoonUnitOptional.get();
        Optional<UnitOfMeasure> cupUnitOptional = unitOfMeasureRepository.findByDescription("cup");
        if (cupUnitOptional.isEmpty()) throw new RuntimeException("Cup unit not in db");
        UnitOfMeasure cupUnit = cupUnitOptional.get();
        Optional<UnitOfMeasure> pintUnitOptional = unitOfMeasureRepository.findByDescription("pint");
        if (pintUnitOptional.isEmpty()) throw new RuntimeException("Pint unit not in db");
        UnitOfMeasure pintUnit = pintUnitOptional.get();
        Optional<UnitOfMeasure> dashUnitOptional = unitOfMeasureRepository.findByDescription("dash");
        if (dashUnitOptional.isEmpty()) throw new RuntimeException("Dash unit not in db");
        UnitOfMeasure dashUnit = dashUnitOptional.get();
        Optional<UnitOfMeasure> piecesUnitOptional = unitOfMeasureRepository.findByDescription("pcs");
        if (piecesUnitOptional.isEmpty()) throw new RuntimeException("Pieces unit not in db");
        UnitOfMeasure piecesUnit = piecesUnitOptional.get();
        log.debug("Units of measure... Done");

        // Get category
        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        if (mexicanCategoryOptional.isEmpty()) throw new RuntimeException("Mexican category not in db");
        Category mexCategory = mexicanCategoryOptional.get();
        log.debug("Categories... Done");

        log.debug("Initializing recipes...");
        //// ---------------------------
        //// Add guacamole recipe
        Recipe guacamole = new Recipe();
        guacamole.setDescription("How to Make Perfect Guacamole Recipe");
        guacamole.setPrepTime(10);
        guacamole.setServings(2);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setNotes(new Notes("Be careful handling chiles if using." +
                " Wash your hands thoroughly after handling and do not touch your eyes" +
                " or the area near your eyes with your hands for several hours."));

        guacamole.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), piecesUnit));
        guacamole.addIngredient(new Ingredient("of salt, more to taste", new BigDecimal("0.25"), teaspoonUnit));
        guacamole.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("of minced red onion or thinly sliced green onion", new BigDecimal(2), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(1), piecesUnit));
        guacamole.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tablespoonUnit));
        guacamole.addIngredient(new Ingredient("of freshly grated black pepper", new BigDecimal(1), dashUnit));
        guacamole.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal("0.5"), piecesUnit));
        guacamole.addIngredient(new Ingredient("red radishes or jicama, to garnish", new BigDecimal(1), piecesUnit));
        guacamole.addIngredient(new Ingredient("tortilla chips, to serve", new BigDecimal(1), piecesUnit));

        guacamole.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit." +
                " Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice." +
                " The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness." +
                " So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole" +
                " and press down to cover it and to prevent air reaching it." +
                " (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");

        guacamole.setSource("Simply Recipes");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.getCategories().add(mexCategory);

        recipes.add(guacamole);
        log.debug("Guacamole... Done");

        //// ---------------------------
        //// Add tacos recipe
        Recipe tacos = new Recipe();
        tacos.setDescription("Spicy Grilled Chicken Tacos Recipe");
        tacos.setPrepTime(20);
        tacos.setCookTime(15);
        tacos.setServings(4);
        tacos.setDifficulty(Difficulty.MODERATE);
        tacos.setNotes(new Notes("Look for ancho chile powder with the Mexican ingredients at your grocery store," +
                " on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano," +
                " and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)"));

        tacos.addIngredient(new Ingredient("ancho chili powder", new BigDecimal(2), tablespoonUnit));
        tacos.addIngredient(new Ingredient("dried oregano", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("dried cumin", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("sugar", new BigDecimal(1), teaspoonUnit));
        tacos.addIngredient(new Ingredient("salt", new BigDecimal("0.5"), teaspoonUnit));
        tacos.addIngredient(new Ingredient("clove garlic, finely chopped", new BigDecimal(1), piecesUnit));
        tacos.addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1), tablespoonUnit));
        tacos.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tablespoonUnit));
        tacos.addIngredient(new Ingredient("olive oil", new BigDecimal(2), tablespoonUnit));
        tacos.addIngredient(new Ingredient("skinless, boneless chicken thighs (1 1/4 pounds)", new BigDecimal(4), piecesUnit));

        tacos.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), piecesUnit));
        tacos.addIngredient(new Ingredient("packed baby arugula (3 ounces)", new BigDecimal(3), cupUnit));
        tacos.addIngredient(new Ingredient("medium ripe avocados, sliced", new BigDecimal(2), piecesUnit));
        tacos.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), piecesUnit));
        tacos.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), pintUnit));
        tacos.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), piecesUnit));
        tacos.addIngredient(new Ingredient("roughly chopped cilantro", new BigDecimal(1), piecesUnit));
        tacos.addIngredient(new Ingredient("sour cream thinned with 1/4 cup milk", new BigDecimal(".5"), cupUnit));
        tacos.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(1), piecesUnit));

        tacos.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder," +
                " oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste." +
                " Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into" +
                " the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat." +
                " As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula." +
                " Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");

        tacos.setSource("Simply Recipes");
        tacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacos.getCategories().add(mexCategory);

        recipes.add(tacos);
        log.debug("Tacos... Done");
        log.debug("Adding recipes... Done");

        return recipes;
    }
}
