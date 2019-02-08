package ca.gc.triagency.datastore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.gc.triagency.datastore.model.ApprovedApplicationParticipation;
import ca.gc.triagency.datastore.model.ApprovedAward;
import ca.gc.triagency.datastore.service.ReportService;

@Controller
@RequestMapping("/api")
public class ApiController {

	@Autowired
	ReportService reportService;
	// @Autowired
	// ReportService reportService;

	@ResponseBody
	@RequestMapping(path = "/approvedAwards", method = RequestMethod.GET)
	public List<ApprovedAward> getApprovedAwards() {
		return reportService.getApprovedAwards();
	}

	@ResponseBody
	@RequestMapping(path = "/approvedAppParticipations", method = RequestMethod.GET)
	public List<ApprovedApplicationParticipation> getApprovedAppParticipations() {
		return reportService.getApprovedAppParticipations();
	}

}
