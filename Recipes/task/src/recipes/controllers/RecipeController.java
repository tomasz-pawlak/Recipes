package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.entities.Recipe;
import recipes.services.RecipeService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id, HttpServletResponse response) {
        if (!recipeService.isRecipeExistsById(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return recipeService.findRecipeById(id);
    }

    @GetMapping(value = "/api/recipe/search", params = "category")
    public List<Recipe> getByCategory(@RequestParam(name = "category") String category) {
        return recipeService.findRecipeByCategory(category);
    }

    @GetMapping(value = "/api/recipe/search", params = "name")
    public List<Recipe> getByName(@RequestParam(name = "name") String name) {
        return recipeService.findRecipeByName(name);
    }

    @PostMapping("/api/recipe/new")
    public String postRecipe(@AuthenticationPrincipal UserDetails details, @Valid @RequestBody Recipe recipe) {

        Recipe newRecipe = new Recipe(
                recipe.getId(), recipe.getName(), recipe.getCategory(), LocalDateTime.now(), recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections(),
                recipe.getEmail()
        );

        newRecipe.setEmail(details.getUsername());
        recipeService.save(newRecipe);

        return String.format("{ \"id\":%s }", newRecipe.getId());
    }

    @PutMapping("/api/recipe/{id}")
    public void updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, HttpServletResponse response, @Valid @RequestBody Recipe recipe) {
        Recipe dbRecipe = recipeService.findRecipeById(id);

        if (recipeService.isRecipeExistsById(id) && dbRecipe.getEmail().equals(userDetails.getUsername())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            recipeService.updateRecipe(id, recipe);
        } else if (recipeService.isRecipeExistsById(id) && !dbRecipe.getEmail().equals(userDetails.getUsername())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if (!recipeService.isRecipeExistsById(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Transactional
    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, HttpServletResponse response) {
        Recipe recipe = recipeService.findRecipeById(id);

        if (recipeService.isRecipeExistsById(id) && recipe.getEmail().equals(userDetails.getUsername())) {
            recipeService.delete(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (recipeService.isRecipeExistsById(id) && !recipe.getEmail().equals(userDetails.getUsername())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
