package uni.fmi.masters.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.masters.bean.FavoriteMoviesBean;

@Repository
public interface FavoritesRepo extends JpaRepository<FavoriteMoviesBean, Integer>{

}
