package uni.fmi.masters.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.masters.bean.CommentBean;

@Repository
public interface CommentRepo extends JpaRepository<CommentBean, Integer>{

}
