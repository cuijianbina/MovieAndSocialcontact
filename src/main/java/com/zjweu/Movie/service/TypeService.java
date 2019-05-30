package com.zjweu.Movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.entity.MovieType;

@Service
public class TypeService {

	@Autowired
	private BaseService baseService;

	/**
	 * Description:显示所有电影类型
	 *
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月11日 下午3:01:25
	 * @version 1.0
	 */
	public List<MovieType> listAll() {
		String sql = "from MovieType";
		return baseService.find(sql, null);
	}

	public void saveOrUpdate(MovieType movieType) {
		if(movieType.getId()==null){
			baseService.save(movieType);
		}else{
			baseService.update(movieType);
		}
	}

	/**
	 * Description:根据Id查找
	 *
	 * @param id
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月25日 上午11:01:41
	 * @version 1.0
	 */
	public MovieType findById(Integer id) {
		return baseService.findById(MovieType.class, id);
	}
}
