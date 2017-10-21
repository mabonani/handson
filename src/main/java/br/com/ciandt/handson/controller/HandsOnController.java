package br.com.ciandt.handson.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/handson")
public class HandsOnController {
	
	@GetMapping
	private String handsOnIndex(){
		return "hands On";
	}

}
