/**
 * 
 */
package com.meidusa.amoeba.data;

import java.util.Map;

/**
 * @author CZX
 * @date 2015-7-17
 */
public class UserDbserver {
	private Long userId;
	private String dbserver;
	
	public UserDbserver(){
		
	}
	
	public UserDbserver(Map<String, Object> userDbserverMap){
		userId = (Long) userDbserverMap.get("userid");
		dbserver = (String) userDbserverMap.get("dbserver");
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDbserver() {
		return dbserver;
	}
	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}
}
