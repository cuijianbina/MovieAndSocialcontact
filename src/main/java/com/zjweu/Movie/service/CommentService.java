package com.zjweu.Movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.entity.Comment;

@Service
public class CommentService {

	@Autowired
	private BaseService baseService;

	/**
	 * Description:新增评论
	 *
	 * @param comment
	 * @author cui.jianbin
	 * @date 2019年4月30日 下午4:40:28
	 * @version 1.0
	 */
	public void save(Comment comment) {
		if(comment.getId()==null){
			baseService.save(comment);
		}else{
			baseService.update(comment);
		}
	}
	
	/**
	 * Description:根据id查询
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月8日  上午11:39:44
	 * @version 1.0
	 */
	public  Map<String,Object> findByIdDetail(Integer id){
		List<Object> params = new ArrayList<>();
		params.add(id);
		String sql = "select c.comments,c.c_time,u.logo,u.username from comment c,user u where c.user_id=u.id and c.id=?";
		return baseService.executeQuerySql(sql, params, 0, 1).get(0);
	}

	/**
	 * Description:某电影下的所有评论
	 *
	 * @param movieId
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月30日 下午4:38:42
	 * @version 1.0
	 */
	public List<Map<String, Object>> getCommentByMovieId(int movieId) throws Exception {
		List<Object> params = new ArrayList<>();
		params.add(movieId);
		String sql = "select c.comments,c.c_time,u.logo,u.username from comment c,user u where c.user_id=u.id and c.movie_id=? order by c.c_time desc";
		int count = baseService.executeCountSql("select count(*) from ("+sql+") t", params);
		return baseService.executeQuerySql(sql, params, 0, count);
	}
}
