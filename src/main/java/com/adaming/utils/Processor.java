package com.adaming.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.adaming.models.User;

@Component
public class Processor implements ItemProcessor<User, User>{
	private static final Map<String, String> DEPT_NAMES = 
							new HashMap<String, String>();
	
	public Processor() {
		DEPT_NAMES.put("0101", "Marketing");
		DEPT_NAMES.put("0102", "Technology");
		DEPT_NAMES.put("0103", "Sales");
	}

	@Override
	public User process(User user) throws Exception {
		String deptId = user.getDept();
		String deptName = DEPT_NAMES.get(deptId);
		user.setDept(deptName);
		
		System.out.println(
				String.format("Converted from {%s} to {%s}", deptId, deptName));
		
		return user;
	}

}
