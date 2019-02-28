package ca.gc.triagency.datastore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.gc.triagency.datastore.service.ReportService;

@Controller
@RequestMapping("/reports")
public class ReportsController {

	@Autowired
	ReportService reportService;

	// @ResponseBody
	// @RequestMapping(path = "/orgs", method = RequestMethod.GET)
	// public List<Organization> getAllOrganizations() {
	// return dataService.getAllOrganizations();
	// }

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		// model.addAttribute("orgs",
		// dataService.getAllOrganizationsWithLinkNum());
		return "reports/home";
	}

	@GetMapping(value = "/applicationsPerOrganization")
	public String viewProgram(Model model) {
		model.addAttribute("rows", reportService.getApplicationsPerOrganization());
		model.addAttribute("approvedDatasets", reportService.getApprovedDatasets());
		return "reports/applicationsPerOrg";
	}
	
	@GetMapping(value = "/dataVizAwardsByOrganization")
	public String dataVizAwardsByOrganization() {
		return "reports/dataVizAwardsByOrganization";
	}
	
	@GetMapping(value = "/dataVizAwardsByOrganizationPBI")
	public String dataVizAwardsByOrganizationPBI() {
		return "reports/dataVizAwardsByOrganizationPBI";
	}
	
	@GetMapping(value = "/dataVizProgramsByAgency")
	public String dataVizProgramsByAgency() {
		return "reports/dataVizProgramsByAgency";
	}
	
	@GetMapping(value = "/dataVizApplicationsByOrg")
	public String dataVizApplicationsByOrg() {
		return "reports/dataVizApplicationsByOrg";
	}
	
	@GetMapping(value = "/dataVizApplicationsByLocation")
	public String dataVizApplicationsByLocation() {
		return "reports/dataVizApplicationsByLocation";
	}
	
	@GetMapping(value = "/dataVizApplicationsByProgram")
	public String dataVizApplicationsByProgram() {
		return "reports/dataVizApplicationsByProgram";
	}
	
	@GetMapping(value = "/dataVizAwardsByLocation")
	public String dataVizAwardsByLocation() {
		return "reports/dataVizAwardsByLocation";
	}
	
	@GetMapping(value = "/dataVizAwardsByProgram")
	public String dataVizAwardsByProgram() {
		return "reports/dataVizAwardsByProgram";
	}
	
	@GetMapping(value = "/dataVizParticipantsByProgram")
	public String dataVizParticipantsByProgram() {
		return "reports/dataVizParticipantsByProgram";
	}
	
	@GetMapping(value = "/dataVizApplicationsByLocationEDI")
	public String dataVizApplicationsByLocationEDI() {
		return "reports/dataVizApplicationsByLocationEDI";
	}
	
	@GetMapping(value = "/dataVizApplicationsByProgramEDI")
	public String dataVizApplicationsByProgramEDI() {
		return "reports/dataVizApplicationsByProgramEDI";
	}
}
