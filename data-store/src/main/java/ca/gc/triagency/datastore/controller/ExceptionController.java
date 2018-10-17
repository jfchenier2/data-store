package ca.gc.triagency.datastore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.gc.triagency.datastore.service.impl.DataAccessServiceImpl;

@Controller
@RequestMapping(value = "/exception")
public class ExceptionController {

	@Autowired
	DataAccessServiceImpl dataService;

	@GetMapping("/forbiden-by-role")
	public String listPrograms(Model model) {
		return "/exception/forbiden-by-role";
	}

}
