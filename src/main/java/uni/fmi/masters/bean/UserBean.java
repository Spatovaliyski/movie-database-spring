package uni.fmi.masters.bean;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"comments"})
public class UserBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="username", nullable = false, unique = true, length = 40)
	private String username;
	
	@Column(name="password", nullable = false, length = 32)
	private String password;
	
	@Column(name="email", nullable = false, unique = true, length = 256)
	private String email;
	
	@Column(name="avatar")
	private String avatar;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<CommentBean> comments;
	
//	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//	private List<FavoriteMoviesBean> favorites;
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

//	public List<FavoriteMoviesBean> getFavorites() {
//		return favorites;
//	}
//
//	public void setFavorites(List<FavoriteMoviesBean> favorites) {
//		this.favorites = favorites;
//	}

	@ManyToMany
	@JoinTable(name = "account_role",
	joinColumns=@JoinColumn(name="account_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<RoleBean> roles;
	
	public Set<RoleBean> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleBean> roles) {
		this.roles = roles;
	}

	public UserBean() {}
	
	public UserBean(String username, String email) {
		this.username = username;
		this.email = email;
	}
	
	public UserBean(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;	
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarPath() {
		return avatar;
	}

	public void setAvatarPath(String avatar) {
		this.avatar = avatar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}	
}
