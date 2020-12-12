package uni.fmi.masters.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.masters.bean.CommentBean;
import uni.fmi.masters.bean.FavoriteMoviesBean;
import uni.fmi.masters.bean.UserBean;
import uni.fmi.masters.repo.FavoritesRepo;

@RestController
public class FavoritesController {
	
	FavoritesRepo favoritesRepo;
	
	public FavoritesController(FavoritesRepo favoritesRepo) {
		this.favoritesRepo = favoritesRepo;
	}
	
	@GetMapping(path = "/favorites/all")
	public List<FavoriteMoviesBean> getAllFavorites(){
		return favoritesRepo.findAll();
	}
	
	@PostMapping(path = "/favorites/add")
	public String addToFavorite(
			@RequestParam(name = "movieId")String movieId,
			HttpSession session
			) {
		
		String moviePage = "index.html?id=";
		UserBean user = (UserBean)session.getAttribute("user");
		
		try {
			if(user != null) {
				
				FavoriteMoviesBean favoritesBean = new FavoriteMoviesBean();
				favoritesBean.setUser(user);
				favoritesBean.setMovieId(movieId);
				
				favoritesBean = favoritesRepo.saveAndFlush(favoritesBean);
				
				if(favoritesBean != null) {
					return String.valueOf(favoritesBean.getId());
				}
				
				return moviePage + movieId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return moviePage + movieId;
		}
		
		return moviePage + movieId;
	}
	

	@DeleteMapping(path = "/favorites/remove")
	//@Secured("ROLE_ADMIN")
	public ResponseEntity<Boolean> deleteFavorite(
			@RequestParam(value = "id")int id,
			@RequestParam(value = "movieId")String movieId,
			HttpSession session
			){
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user == null) {
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
		
		Optional<FavoriteMoviesBean> optionalFavoriteMovie= favoritesRepo.findById(id);
		
		if(optionalFavoriteMovie.isPresent()) {
			FavoriteMoviesBean favorite = optionalFavoriteMovie.get();
			
			if(favorite.getUser().getId() == user.getId() && favorite.getMovieId() == movieId)	{
				favoritesRepo.delete(favorite);				
				return new ResponseEntity<>(true, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
			}
			
			
		}else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
	}
}
