package com.zjweu.Movie.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zjweu.Movie.common.CtlStatus;
import com.zjweu.Movie.constants.MovieConstants;
import com.zjweu.Movie.entity.ChatRoom;
import com.zjweu.Movie.entity.Movie;
import com.zjweu.Movie.service.ChatRoomService;
import com.zjweu.Movie.service.MovieService;


@CrossOrigin
@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private ChatRoomService chatRoomService;

	private FileUploadController fileUploadControler = new FileUploadController();

	/**
	 * Description:上架电影
	 *
	 * @param movieName
	 * @param playTime
	 *            播放时间，单位是分钟
	 * @param createTime
	 * @param introduce
	 * @param type
	 * @param actors
	 * @param director
	 * @param country
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月15日 下午4:58:46
	 * @version 1.0
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/pendMovie")
	public Object saveMovie(HttpServletRequest request,
			@RequestParam(value = "movieName", required = true) String movieName,
			@RequestParam(value = "createTime", required = true) String createTime,
			@RequestParam(value = "introduce", required = true) String introduce,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "actor", required = true) String actors,
			@RequestParam(value = "director", required = true) String director,
			@RequestParam(value = "country", required = true) String country,
			@RequestParam(value = "imgfile") MultipartFile imgFile,
			@RequestParam(value = "videofile") MultipartFile videoFile) throws Exception {
		Movie movie = new Movie();
		movie.setMovieName(movieName);
		movie.setCreateTime(createTime);
		movie.setIntroduce(introduce);
		movie.setActors(actors);
		movie.setDirector(director);
		movie.setCountry(country);
		movie.setType(type);
		movie.setHot(MovieConstants.INIT_HOT);
		Object object = fileUploadControler.manyFile(imgFile, videoFile);
		CtlStatus ctlStatus = (CtlStatus) object;
		boolean flag = ctlStatus.isSuccess();
		if (flag == false) {
			return CtlStatus.failed(ctlStatus.get("message").toString());
		} else {
			Object obj = ctlStatus.get("data");
			Map<String, Object> map = (Map<String, Object>) obj;
			String coverImg = map.get("coverImg").toString(); // 封面图路径
			String moviePath = map.get("moviePath").toString(); // 视频路径
			String playTime = map.get("playTime").toString(); // 播放时长，*时*分*秒
			int searchTime = Integer.valueOf(map.get("searchTime").toString());// 播放时长，*分
			movie.setCoverImg(coverImg);
			movie.setPath(moviePath);
			movie.setPlayTime(playTime);
			movie.setSearchTime(searchTime);
		}
		movie.setStatus(MovieConstants.MOVIE_ON);
		movieService.saveOrUpdate(movie);
		//当电影添加成功时，为电影生成聊天室
		ChatRoom chatRoom  = new ChatRoom();
		chatRoom.setMovieId(movie.getId());
		chatRoom.setChatRoomName(movieName+"聊天室");
		chatRoomService.saveOrUpdate(chatRoom);
		return CtlStatus.success("添加成功");
	}

	/**
	 * Description:按条件筛选电影
	 *
	 * @param request
	 * @param type
	 *            电影类型
	 * @param country
	 *            国家
	 * @param movieName
	 *            电影名
	 * @param searchTime
	 *            时长，分钟为单位
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午10:57:17
	 * @version 1.0
	 * @throws Exception
	 */
	@RequestMapping("/listMovie")
	public Object listMovie(HttpServletRequest request,
			@RequestParam(value="status",required=false) Integer status,
			@RequestParam(value = "movieType", required = false) Integer type,
			@RequestParam(value = "counrty", required = false) String country,
			@RequestParam(value = "movieName", required = false) String movieName,
			@RequestParam(value = "searchTime", required = false) Integer searchTime,
			@RequestParam(value = "sOrder", required = false) String sOrder) throws Exception {
		List<Map<String, Object>> mlist = movieService.findByConditions(status,movieName, searchTime, type, country, sOrder);
		return CtlStatus.success(mlist);
	}
	
	@RequestMapping("/countMovie")
	public Object countMovie(){
		int count = movieService.countMovie();
		return CtlStatus.success(count);
	}
	
	/**
	 * Description:根据Id查找电影
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月3日  下午4:04:06
	 * @version 1.0
	 */
	@RequestMapping("/findMovieById")
	public Object findMovieById(@RequestParam(value="id",required=false) Integer id){
		List<Map<String,Object>> result = movieService.findByIdDetail(id);
		if(result==null||result.size()==0){
			return CtlStatus.failed("电影已被删除");
		}
		Movie movie = movieService.findById(id);
		movie.setPageviews(movie.getPageviews()+1);//浏览数+1
		movie.setHot(movie.getHot()+100);
		movieService.saveOrUpdate(movie);
		return CtlStatus.success(result);
	}

	/**
	 * Description:更新电影状态（包括批量上下架）
	 *
	 * @param request
	 * @param movieIds
	 * @param operation 0是上架，1是下架
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月3日  下午4:05:26
	 * @version 1.0
	 */
	@RequestMapping("/updateMovie")
	public Object updateMovie(HttpServletRequest request,
			@RequestParam(value = "movieIds", required = true) String movieIds,
			@RequestParam(value = "operation", required = true) Integer operation) {
		movieService.updateByIds(movieIds, operation);
		return CtlStatus.success("操作成功");
	}
	
	/**
	 * Description:用于批量删除和单独删除
	 *
	 * @param request
	 * @param movieIds
	 * @param operation
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午11:10:49
	 * @version 1.0
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public Object delete(HttpServletRequest request,
			@RequestParam(value = "movieIds", required = true) String movieIds) throws Exception {
		boolean flag = movieService.delete(movieIds);
		if(flag==false){
			return CtlStatus.failed("操作失败");
		}
		return CtlStatus.success("操作成功");
	}
}
