package com.cn.leedane.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cn.leedane.utils.SpringUtil;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.model.FriendBean;
import com.cn.leedane.model.FriendModel;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.observer.template.NotificationTemplate;
import com.cn.leedane.service.FriendService;

/**
 * 被观察者实现类
 * @author LeeDane
 * 2016年7月12日 下午1:53:05
 * Version 1.0
 */
@Component
public class ConcreteWatched implements Watched{

	private FriendService<FriendBean> friendService;
	
	@Resource
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}
	
	/**
	 * 多个观察者
	 */
	private List<Watcher> mWatchers = new ArrayList<Watcher>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void notifyWatchers(UserBean watchedBean, NotificationTemplate template) {	
		/**
		 * 通知的多个用户
		 */
		List<FriendModel> friends = new ArrayList<FriendModel>();
		friendService = (FriendService<FriendBean>) SpringUtil.getBean("friendService");
		
		List<Map<String, Object>> friendObject = friendService.getToFromFriends(watchedBean.getId());
		
		if(friends != null && friendObject.size() > 0){
			for(Map<String, Object> friend: friendObject){
				friends.add(new FriendModel(StringUtil.changeObjectToInt(friend.get("id")), StringUtil.changeNotNull(friend.get("remark"))));		
			}
			
			for(Watcher watcher: mWatchers){
				watcher.updateMood(friends, watchedBean, template);
			}
		}else{
			System.out.println(watchedBean.getAccount() +"还没有好友");
		}
	}
	@Override
	public void addWatcher(Watcher watcher) {
		mWatchers.add(watcher);
	}

	@Override
	public void removeWatcher(Watcher watcher) {
		mWatchers.remove(watcher);
	}
	
	

}
