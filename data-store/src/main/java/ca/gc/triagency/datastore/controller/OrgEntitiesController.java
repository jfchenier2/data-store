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

import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.service.DataAccessService;

@Controller
@RequestMapping("/entities/orgs")
public class OrgEntitiesController {
	@Autowired
	DataAccessService dataService;

	@ResponseBody
	@RequestMapping(path = "/orgs", method = RequestMethod.GET)
	public List<Organization> getAllOrganizations() {
		return dataService.getAllOrganizations();
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("orgs", dataService.getAllOrganizationsWithLinkNum());
		return "entities/orgs/home";
	}

	@GetMapping(value = "/viewOrg")
	public String viewProgram(@RequestParam("id") long id, Model model) {
		model.addAttribute("org", dataService.getOrganization(id));
		return "entities/orgs/viewOrg";
	}
}
