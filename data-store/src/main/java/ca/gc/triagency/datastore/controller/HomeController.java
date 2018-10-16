package ca.gc.triagency.datastore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.triagency.datastore.service.impl.DataAccessServiceImpl;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	DataAccessServiceImpl dataService;

	@GetMapping("/listPrograms")
	public String listPrograms(Model model) {
		model.addAttribute("programs", dataService.getAllPrograms());
		return "listPrograms";
	}

	@GetMapping("/viewProgram")
	public String viewPrograms(@RequestParam Long id, Model model) {
		model.addAttribute("program", dataService.getProgram(id));
		return "viewProgram";

	}
}
