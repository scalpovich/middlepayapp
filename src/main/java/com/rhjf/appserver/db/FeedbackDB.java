package com.rhjf.appserver.db;

public class FeedbackDB extends DBBase{

	
	public static Integer saveFeedback(Object[] obj){
		String sql = "insert into tab_feedback (ID,UserID , CreateTime ,Phone  , Content) values (?,?,now(),?,?)";
		return executeSql(sql, obj);
	}
}
