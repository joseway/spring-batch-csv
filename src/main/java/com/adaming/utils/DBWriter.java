package com.adaming.utils;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adaming.models.User;
import com.adaming.repositories.UserRepository;

@Component
public class DBWriter implements ItemWriter<User>{
	@Autowired
	private UserRepository userRepository;

	@Override
	public void write(List<? extends User> users) throws Exception {
		System.out.println("Info saved to DB: " + users);
		userRepository.saveAll(users);
	}

	
}
