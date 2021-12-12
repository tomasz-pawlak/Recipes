package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.entities.Recipe;
import recipes.repo.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;


    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe save(Recipe toSave) {
        return recipeRepository.save(toSave);
    }


    public Recipe findRecipeById(Long id) {
        return recipeRepository.findRecipeById(id);
    }

    public List<Recipe> findRecipeByCategory(String category) {
        return recipeRepository.findRecipeByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findRecipeByName(String name) {
        return recipeRepository.findRecipeByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public boolean findByEmail(String email){
        return recipeRepository.findRecipeByEmail(email);
    }


    public void delete(Long id) {
        recipeRepository.deleteRecipeById(id);
    }

    public boolean isRecipeExistsById(Long id) {
        return recipeRepository.existsById(id);
    }

    public void updateRecipe(Long id, Recipe toUpdate) {

        Optional<Recipe> recipe = Optional.ofNullable(findRecipeById(id));

        recipe.get().setName(toUpdate.getName());
        recipe.get().setCategory(toUpdate.getCategory());
        recipe.get().setDate(LocalDateTime.now());
        recipe.get().setDescription(toUpdate.getDescription());
        recipe.get().setIngredients(toUpdate.getIngredients());
        recipe.get().setDirections(toUpdate.getDirections());

        save(recipe.get());
    }


}
