package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class controller {
	@RequestMapping(value = "/index")
	public ModelAndView home(Model model) {
		System.out.println("INICIA");
//		model.addAttribute("Hello", "Hola Mundo Velocity");
		return new ModelAndView("index");
	}
}
