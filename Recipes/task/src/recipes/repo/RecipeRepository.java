package recipes.repo;

import org.springframework.data.repository.CrudRepository;
import recipes.entities.Recipe;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Recipe findRecipeById(Long id);

    void deleteRecipeById(Long id);

    boolean existsById(Long id);

    List<Recipe> findRecipeByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findRecipeByNameContainingIgnoreCaseOrderByDateDesc(String name);

    boolean findRecipeByEmail(String email);

}
