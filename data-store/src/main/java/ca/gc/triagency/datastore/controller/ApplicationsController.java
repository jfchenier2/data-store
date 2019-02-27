package ca.gc.triagency.datastore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.gc.triagency.datastore.model.ApprovedApplication;
import ca.gc.triagency.datastore.service.DataAccessService;

@Controller
@RequestMapping("/entities/applications")
public class ApplicationsController {
	@Autowired
	DataAccessService dataService;

	@ResponseBody
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public List<ApprovedApplication> getAllDatasetApplications() {
		return dataService.getApprovedApplications();
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("apps", dataService.getApprovedApplications());
		return "entities/applications/home";
	}

	@GetMapping(value = "/viewApplication")
	public String viewApplication(@RequestParam("id") long id, Model model) {
		model.addAttribute("app", dataService.getDatasetApplication(id));
		model.addAttribute("participations", dataService.getAppDatasetParticipations(id));
		return "entities/applications/viewApplication";
	}

}
