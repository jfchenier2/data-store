package ca.gc.triagency.datastore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.gc.triagency.datastore.service.ImportService;

@Controller
@RequestMapping(value = "/import")
public class ImportController {

	@Autowired
	ImportService importService;

	@GetMapping("/importPrograms")
	public String importPrograms() {
		return "import/importPrograms";
	}

	@PostMapping("/importPrograms")
	public String importPrograms_post(Model model) {
		importService.importProgramsFromFile();
		return "redirect:/home";
	}

}
