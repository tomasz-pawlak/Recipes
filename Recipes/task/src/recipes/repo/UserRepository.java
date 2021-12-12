package recipes.repo;


import org.springframework.data.repository.CrudRepository;
import recipes.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

     User findUserByEmail(String string);
     boolean existsByEmail(String string);

}
