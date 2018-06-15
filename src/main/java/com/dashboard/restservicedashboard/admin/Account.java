package com.dashboard.restservicedashboard.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;


@RequiredArgsConstructor
public class Account {
	  
	  @Id
	  @Getter
      private String id;
	  @Getter
      private final String username;
	  @Getter
      private final String password;
	  
	

	}
