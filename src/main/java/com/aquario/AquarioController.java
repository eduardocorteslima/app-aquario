package com.aquario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aquario")
public class AquarioController {

	@Autowired
	private AquarioComponent component;
	


	@RequestMapping("/ligar")
	public String ligar() {
		component.ligar();
		return "ok";
	}

	@RequestMapping("/desligar")
	public String desligar() {

		component.desligar();
		return "desligado";
	}
	
	@RequestMapping("/")
	public String status(){
		component.status();
		return "ok";
	}
}
