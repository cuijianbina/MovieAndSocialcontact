package com.zjweu.Movie.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjweu.Movie.common.DateCommon;
import com.zjweu.Movie.constants.AdminUserConstants;
import com.zjweu.Movie.entity.Message;

@Service
public class MessageService {

	@Autowired
	private BaseService baseService;
	
	/**
	 * Description:总消息数计数
	 *
	 * @param aimId
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午10:41:31
	 * @version 1.0
	 */
	public int countAllMessage(Integer aimId) {
		List<Object> params = new ArrayList<>();
		params.add(aimId);
		String sql = "select m.id,m.status,m.messages,m.send_time,u.username,u.logo from message m,user u where m.send_id=u.id and m.aim_id=?";
		int count = baseService.executeCountSql("select count(*) from ("+sql+")t", params);
		return count;
	}

	/**
	 * Description:发送给管理员的消息
	 *
	 * @param aimId
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月28日 下午2:47:45
	 * @version 1.0
	 */
	public List<Map<String, Object>> findByAimId(Integer aimId,Integer status) {
		List<Object> params = new ArrayList<>();
		params.add(aimId);
		String sql = "select m.id,m.status,m.messages,m.send_time,u.username,u.logo from message m,user u where m.send_id=u.id and m.aim_id=?";
		if(status!=null){
			sql+=" and m.status =?";
			params.add(status);
		}
		int count = baseService.executeCountSql("select count(*) from ("+sql+")t", params);
		return baseService.executeQuerySql(sql, params, 0, count);
		//return baseService.find("from Message where aimId=?1 and status=?2", params);
	}
	
	/**
	 * Description:查找指定用户发送的所有消息
	 *
	 * @param sendId
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月15日  上午9:21:59
	 * @version 1.0
	 */
	public List<Map<String,Object>> findBySendId(Integer sendId){
		List<Object> params = new ArrayList<>();
		params.add(sendId);
		String sql = "select m.id,m.status,m.messages,m.send_time,u.username,u.logo from message m,user u where m.send_id=u.id and m.send_id=?";
		int count = baseService.executeCountSql("select count(*) from ("+sql+")t", params);
		return baseService.executeQuerySql(sql, params, 0, count);
	}

	/**
	 * Description:保存一个消息
	 *
	 * @param message
	 * @throws Exception
	 * @author cui.jianbin
	 * @date 2019年4月29日 下午3:36:49
	 * @version 1.0
	 */
	public void save(Message message) throws Exception {
		if(message.getId()==null){
			baseService.save(message);
		}else{
			baseService.update(message);
		}
	}
	
	/**
	 * Description:根据Id查找
	 *
	 * @param mid
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午9:26:10
	 * @version 1.0
	 */
	public Message findById(int mid){
		return baseService.findById(Message.class, mid);
	}
	
	/**
	 * Description:更新状态，标记为已读
	 *
	 * @param message
	 * @author cui.jianbin  
	 * @date 2019年5月13日  上午9:35:12
	 * @version 1.0
	 */
	public void update(Message message){
		message.setStatus(AdminUserConstants.MESSAGE_READ);
		baseService.update(message);
	}
}
