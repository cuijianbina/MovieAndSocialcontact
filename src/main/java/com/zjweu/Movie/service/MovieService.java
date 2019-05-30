package com.zjweu.Movie.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.constants.MovieConstants;
import com.zjweu.Movie.entity.ChatRoom;
import com.zjweu.Movie.entity.Movie;
import com.zjweu.Movie.util.CMyString;

@Service
public class MovieService {

	@Autowired
	private BaseService baseService;
	
	@Autowired
	private ChatRoomService chatRoomService;

	/**
	 * Description:添加电影或更新
	 *
	 * @param movie
	 * @author cui.jianbin
	 * @date 2019年5月3日 下午4:08:08
	 * @version 1.0
	 */
	public void saveOrUpdate(Movie movie) {
		if (movie.getId() == null) {
			baseService.save(movie);
		} else {
			baseService.update(movie);
		}
	}

	/**
	 * Description:批量删除,同时删除该电影的资源（封面图和视频）
	 *
	 * @param movieIds
	 * @author cui.jianbin
	 * @date 2019年5月9日 上午10:33:31
	 * @version 1.0
	 * @throws Exception 
	 */
	public boolean delete(String movieIds) throws Exception {
		// String sql ="delete from movie where id in(?)";
		String[] idss = movieIds.split(",");
		boolean flag = true;
		for (String id : idss) {
			Movie movie = findById(Integer.valueOf(id));
			String coverImg = movie.getCoverImg();
			String path = movie.getPath();
			File file = new File(coverImg);
			File file1 = new File(path);
			if(file.exists()&&file1.exists()){
				file.delete();
				file1.delete();
			}else{
				return false;
			}
			ChatRoom chatRoom  = chatRoomService.findByMovieId(movie.getId());
			if(chatRoom==null){
				return flag;
			}
			chatRoomService.delete(chatRoom);//删除电影前先删除该聊天室
			baseService.delete(movie);
		}
		return flag;
	}

	/**
	 * Description:批量操作电影（0是上架，1是下架）
	 *
	 * @param movieIds
	 * @param operation
	 * @author cui.jianbin
	 * @date 2019年5月3日 下午4:08:25
	 * @version 1.0
	 */
	public void updateByIds(String movieIds, Integer operation) {
		String[] idss = movieIds.split(",");
		Movie movie = null;
		for (String id : idss) {
			movie = findById(Integer.valueOf(id));
			if (operation == 0) {
				movie.setStatus(MovieConstants.MOVIE_ON);
				saveOrUpdate(movie);
			} else if (operation == 1) {
				movie.setStatus(MovieConstants.MOVIE_CLOSE);
				saveOrUpdate(movie);
			}
		}
	}

	/**
	 * Description:只显示movie
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin
	 * @date 2019年5月4日 下午4:14:11
	 * @version 1.0
	 */
	public Movie findById(int id) {
		return baseService.findById(Movie.class, id);
	}

	/**
	 * Description:统计总电影数
	 *
	 * @return
	 * @author cui.jianbin
	 * @date 2019年5月7日 下午4:50:26
	 * @version 1.0
	 */
	public int countMovie() {
		return baseService.executeCountSql("select count(*) from movie", null);
	}

	/**
	 * Description:根据Id查找(用作关联查询)
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午3:44:12
	 * @version 1.0
	 */
	public List<Map<String, Object>> findByIdDetail(int id) {
		String sql = "select m.*,mt.type as movie_type,c.id as room_id,c.chat_room_name as cname,c.user_ids"
				+ " from movie m,movie_type mt,chatroom c where m.id=c.movie_id and m.type=mt.id and m.id=?";
		List<Object> params = new ArrayList<>();
		params.add(id);
		return baseService.executeQuerySql(sql, params, 0, 1);
	}

	/**
	 * Description:多条件可选查询(当status=0时用于用户电影列表展示)
	 *
	 * @param movieName
	 * @param playTime
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @param country
	 * @param sOrder
	 * @param nStartPage
	 * @param nPageSize
	 * @return
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月10日 下午5:46:58
	 * @version 1.0
	 */
	public List<Map<String, Object>> findByConditions(Integer status, String movieName, Integer searchTime,
			Integer type, String country, String sOrder) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select m.id,m.cover_img,m.search_time,m.movie_name,m.director,m.create_time,m.`status`,m.hot,mt.type from movie m,movie_type mt where  m.type=mt.id ");
		List<Object> params = new ArrayList<>();
		if (!CMyString.isEmpty(movieName)) {
			sql.append(" and m.movie_name like ? ");
			params.add("%" + movieName + "%");
		}
		if (searchTime != null && searchTime == 1) {
			sql.append(" and m.search_time>=90 ");
		} else if (searchTime != null && searchTime == 2) {
			sql.append(" and m.search_time<=90 ");
		}
		if (type != null) {
			sql.append(" and m.type = ? ");
			params.add(type);
		}
		if (!CMyString.isEmpty(country)) {
			sql.append(" and m.country = ? ");
			params.add(country);
		}
		if (status != null) {
			sql.append(" and m.status = ? ");
			params.add(status);
		}
		if (CMyString.isEmpty(sOrder)) {
			sql.append(" order by m.hot desc");
		} else {
			sql.append(" order by m.create_time desc");
		}
		int count = baseService.executeCountSql("select count(*) from (" + sql.toString() + ") t", params);
		return baseService.executeQuerySql(sql.toString(), params, 0, count);
	}
}
