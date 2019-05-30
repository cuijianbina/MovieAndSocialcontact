package com.zjweu.Movie.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.entity.Comment;
import com.zjweu.Movie.entity.Movie;
import com.zjweu.Movie.entity.User;
import com.zjweu.Movie.service.CommentService;
import com.zjweu.Movie.service.MovieService;
import com.zjweu.Movie.service.UserService;
import com.zjweu.Movie.util.CMyString;
import com.zjweu.Movie.util.SessionUtil;

@CrossOrigin
@RequestMapping("/comment")
@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MovieService movieService;

	/**
	 * Description:显示某电影下的所有评论
	 *
	 * @param movieId
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年5月3日 下午4:19:02
	 * @version 1.0
	 */
	@RequestMapping("/findByMovieId")
	public Object findByMovieId(@RequestParam(value = "movieId", required = true) Integer movieId) throws Exception {
		List<Map<String, Object>> result = commentService.getCommentByMovieId(movieId);
		return CtlStatus.success(result);
	}

	/**
	 * Description:新增评论
	 *
	 * @param request
	 * @return
	 * @author cui.jianbin
	 * @date 2019年5月3日 下午4:31:04
	 * @version 1.0
	 * @throws Exception
	 */
	@RequestMapping("/addComment")
	public Object addComment(HttpServletRequest request,
			@RequestParam(value = "content", required = true) String content,
			@RequestParam(value = "movieId", required = true) Integer movieId) throws Exception {
		String token  = request.getHeader("token");
		if(CMyString.isEmpty(token)||token.equals("undefined")){
			return CtlStatus.failed("请先登录");
		}
		User user = userService.findByToken(token);
		if(user==null){
			return CtlStatus.failed("该用户不存在，请联系管理员");
		}
		int userId = user.getId();
		boolean flag = userService.getWordAllow(userId);
		if (!flag) {
			return CtlStatus.failed("你已被禁言");
		}
		Comment comment = new Comment();
		comment.setUserId(userId);
		comment.setComments(content);
		comment.setMovieId(movieId);
		Date createTime = DateCommon.getTime();
		comment.setCTime(createTime);
		commentService.save(comment);
		Movie movie = movieService.findById(movieId);
		movie.setHot(movie.getHot()+100);
		movieService.saveOrUpdate(movie);
		Map<String,Object> result = commentService.findByIdDetail(comment.getId());
		return CtlStatus.success(result);
	}

}
