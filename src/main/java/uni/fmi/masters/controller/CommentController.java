package uni.fmi.masters.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.masters.bean.CommentBean;
import uni.fmi.masters.bean.UserBean;
import uni.fmi.masters.repo.CommentRepo;

@RestController
public class CommentController {

	
	CommentRepo commentRepo;
	
	public CommentController(CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
	}
	
	
	@PostMapping(path = "/comment/add")
	public String addComent(
			@RequestParam(value = "comment")String comment,
			@RequestParam(value = "temp")double temp,
			@RequestParam(value = "city")String city,
			@RequestParam(value = "image")String image,
			HttpSession session
			) {
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			
			CommentBean commentBean = new CommentBean();
			commentBean.setCity(city);
			commentBean.setComment(comment);
			commentBean.setIcon(image);
			commentBean.setTemp(temp);
			commentBean.setUser(user);
			
			commentBean = commentRepo.saveAndFlush(commentBean);
			
			if(commentBean != null) {
				return String.valueOf(commentBean.getId());
			}
			
			return "Error: insert unsuccessfull";
			
		}else {
			return "Error: not logged in";
		}		
		
	}	
	
	@GetMapping(path = "/comment/all")
	public List<CommentBean> getAllComents(){
		return commentRepo.findAll();
	}
	
	@DeleteMapping(path = "/comment/delete")
	public ResponseEntity<Boolean> deleteComment(
			@RequestParam(value = "id")int id,
			HttpSession session
			){
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user == null) {
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
		
		Optional<CommentBean> optionalComment = commentRepo.findById(id);
		
		if(optionalComment.isPresent()) {
			CommentBean comment = optionalComment.get();
			
			commentRepo.delete(comment);
			
			return new ResponseEntity<>(true, HttpStatus.OK);
			
			
		}else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
	}
}
