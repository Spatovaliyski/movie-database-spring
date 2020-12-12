package uni.fmi.masters.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import uni.fmi.masters.WebSecurityConfig;
import uni.fmi.masters.bean.UserBean;
import uni.fmi.masters.repo.UserRepo;

@RestController
public class LoginController {

	private UserRepo userRepo;
	private WebSecurityConfig webSecurityConfig;
	
	public LoginController(UserRepo userRepo, WebSecurityConfig webSecurityConfig) {
		this.userRepo = userRepo;
		this.webSecurityConfig = webSecurityConfig;
	}
	
	
	@PostMapping(path = "/register")
	public UserBean register(@RequestParam(name = "email")String email, 
			@RequestParam(name = "username")String username, @RequestParam(name = "password")String password,
			@RequestParam(name = "repeatPassword")String repeatPassword) {
		
		if(password.equals(repeatPassword)) {
			
			UserBean user = new UserBean(username, hashPassword(password), email);
			
			return userRepo.saveAndFlush(user);
			
		} else {
			return null;
		}
	}
	
	@PostMapping(path = "/updateUser")
	public String updateUser(
		@RequestParam(name = "email")String email,
		@RequestParam(name = "username")String username,
		@RequestParam(name = "avatar")String avatar,
		@RequestParam(name = "oldPassword")String oldPassword,
		@RequestParam(name = "newPassword")String newPassword,
		@RequestParam(name = "repeatPassword")String repeatPassword,
		HttpSession session) {
		
		UserBean user = (UserBean) session.getAttribute("user");
	
		if(user != null) {
			if(user.getPassword().equals(hashPassword(oldPassword))) {
				user.setPassword(hashPassword(newPassword));
				user.setEmail(email);
				user.setUsername(username);
				user.setAvatarPath(avatar);
				userRepo.save(user);
			} else if(newPassword.isEmpty() && oldPassword.isEmpty() && repeatPassword.isEmpty()) {
				user.setEmail(email);
				user.setUsername(username);
				user.setAvatarPath(avatar);
				userRepo.save(user);
			} else {
				return "home.html";
			}
		}
		return "profile.html";
	}
	
	@PostMapping(path = "/login")
	public String login(
			@RequestParam(name = "username")
			String username, 
			@RequestParam(name = "password")
			String password, HttpSession session) {
		
		UserBean user = userRepo.findUserByUsernameAndPassword(username, hashPassword(password));
		
		if(user != null) {
			session.setAttribute("user", user);
			
			try {
				UserDetails userDetails = 
						webSecurityConfig.userDetailsServiceBean().
						loadUserByUsername(user.getUsername());
				
				if(userDetails != null) {
					Authentication auth = new UsernamePasswordAuthenticationToken(
							userDetails.getUsername(),
							userDetails.getPassword(),
							userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(auth);
					
					ServletRequestAttributes attr = (ServletRequestAttributes)
							RequestContextHolder.currentRequestAttributes();
					
					HttpSession http = attr.getRequest().getSession(true);
					http.setAttribute("SPRING_SECURITY_CONTEXT", 
							SecurityContextHolder.getContext());
				}
				
				return "home.html";
				
			} catch (UsernameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "error.html";
	}
	
	@RequestMapping(path = "/whoAmI", method = RequestMethod.GET)
	public ResponseEntity<Integer> whoAmI(HttpSession session){

		UserBean user = (UserBean)session.getAttribute("user");
		
		if(user != null) {
			return new ResponseEntity<>(user.getId(), HttpStatus.OK);

		}else {
			return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(path = "/logout")
	public ResponseEntity<Boolean> logout(HttpSession session){

		UserBean user = (UserBean) session.getAttribute("user");

		if(user != null ) {
			session.invalidate();
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
	}

	private String hashPassword(String password) {
		
		StringBuilder result = new StringBuilder();
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(password.getBytes());
			
			byte[] bytes = md.digest();
			
			for(int i = 0; i < bytes.length; i++) {
				result.append((char)bytes[i]);
			}			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
	
		return result.toString();
	}
}
