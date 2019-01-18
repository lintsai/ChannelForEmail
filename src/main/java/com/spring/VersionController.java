package com.spring;

import com.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping
public class VersionController {
	
	public VersionController(){
		Util.getConsoleLogger().info(
				"VersionController() called");
		Util.getFileLogger().info(
				"VersionController() called");
	}

	@Value("${version}")
	private String version;
	
	@RequestMapping("/version")
	public String version() {
		return version;
	}

}