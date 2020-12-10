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
import uni.fmi.masters.bean.UserBean;
import uni.fmi.masters.repo.CommentRepo;

@RestController
public class CommentController {

	
	CommentRepo commentRepo;
	
	public CommentController(CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
	}
	
	
	@PostMapping(path = "/comment/post")
	public String addComent(
			@RequestParam(name = "comment-area")String comment,
			@RequestParam(name = "rating")int rating,
			@RequestParam(value = "image")String image,
			HttpSession session
			) {
		
		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			
			CommentBean commentBean = new CommentBean();
			commentBean.setComment(comment);
			commentBean.setIcon(image);
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
	@Secured("ROLE_ADMIN")
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
			
			if(comment.getUser().getId() == user.getId())	{
				commentRepo.delete(comment);				
				return new ResponseEntity<>(true, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
			}
			
			
		}else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
	}
}
