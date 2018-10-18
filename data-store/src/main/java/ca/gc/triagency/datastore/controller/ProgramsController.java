package ca.gc.triagency.datastore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.gc.triagency.datastore.form.ProgramForm;
import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Program;
import ca.gc.triagency.datastore.service.DataAccessService;

@Controller
@RequestMapping("/programs")
public class ProgramsController {
	@Autowired
	DataAccessService dataService;

	@ResponseBody
	@RequestMapping(path = "/programs", method = RequestMethod.GET)
	public List<Program> getAllPrograms() {
		return dataService.getAllPrograms();
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() {
		return "programs/home";
	}

	@GetMapping(value = "/createProgram")
	public String createProgram(Model model) {
		model.addAttribute("agencies", dataService.getAllAgencies());
		return "programs/createProgram";
	}

	@GetMapping(value = "/viewProgram")
	public String viewProgram(@RequestParam("id") long id, Model model) {
		model.addAttribute("programForm", new ProgramForm(dataService.getProgram(id)));
		return "programs/viewProgram";
	}

	@GetMapping(value = "/viewAgency")
	public String viewAgency(@RequestParam("id") long id, Model model) {
		model.addAttribute("agency", dataService.getAgency(id));
		model.addAttribute("agencyPrograms", dataService.getAgencyPrograms(id));
		return "programs/viewAgency";
	}

	@GetMapping(value = "/editProgram")
	public String editProgram(@RequestParam("id") long id, Model model) {
		Program program = dataService.getProgram(id);
		model.addAttribute("programForm", new ProgramForm(program));
		List<Agency> allAgencies = dataService.getAllAgencies();
		List<Agency> otherAgencies = new ArrayList<Agency>();
		for (Agency a : allAgencies) {
			if (program.getAgencies().contains(a) == false) {
				otherAgencies.add(a);
			}
		}
		model.addAttribute("otherAgencies", otherAgencies);
		model.addAttribute("allAgencies", allAgencies);
		return "programs/editProgram";
	}

	@PostMapping(value = "/editProgram")
	public String editProgramPost(@Valid @ModelAttribute("programForm") ProgramForm command, // Model
																								// model,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// model.addAttribute("allAgencies", dataService.getAllAgencies());
			return "programs/editProgram";
		}
		Program targetUpdate = dataService.getProgram(command.getId());
		targetUpdate.loadFromForm(command);
		dataService.saveProgram(targetUpdate);
		return "redirect:viewProgram?id=" + targetUpdate.getId();
	}
}
