package ca.gc.triagency.datastore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.triagency.datastore.form.ProgramForm;
import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.model.Program;
import ca.gc.triagency.datastore.service.DataAccessService;
import ca.gc.triagency.datastore.service.DatasetService;

@Controller
@RequestMapping(value = "/datasets")
public class DatasetController {
	@Autowired
	DataAccessService dataService;

	@Autowired
	DatasetService datasetService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("datasets", datasetService.getAllDatasets());
		return "datasets/home";
	}

	@GetMapping(value = "/createDataset")
	public String createDatasetSelectFile(Model model) {
		model.addAttribute("datasetFiles", datasetService.getDatasetFiles());
		return "datasets/createDataset";
	}

	@GetMapping(value = "/createDataset", params = { "filename" })
	public String createDataset(@RequestParam String filename, Model model) {
		Dataset datasetForm = datasetService.configureNewDatasetFromFilename(filename);
		model.addAttribute("form", datasetForm);
		return "datasets/createDatasetValidate";
	}

	@Async
	@PostMapping(value = "/createDataset")
	public String createDatasetPost(@RequestParam String filename) {
		Dataset form = datasetService.configureNewDatasetFromFilename(filename);
		form.setDatasetStatus(Dataset.DatasetStatus.CREATED);
		Dataset dataset = datasetService.saveDataset(form);
		datasetService.uploadData(dataset);
		return "redirect:home";
	}

	@GetMapping(value = "/viewDataset")
	public String viewDataset(@RequestParam("id") long id, Model model) {

		Dataset ds = datasetService.getDataset(id);
		ds = datasetService.markAssessIfFirstTimeView(ds);
		model.addAttribute("dataset", ds);
		model.addAttribute("warningOrgs", datasetService.getDatasetWarningOrgs(id));

		return "datasets/viewDataset";
	}

	@GetMapping(value = "/viewDatasetConfig")
	public String viewDataestConfig(@RequestParam("id") long id, Model model) {
		model.addAttribute("config", datasetService.getDatasetConfiguration(id));
		return "datasets/viewDatasetConfiguration";
	}

	@GetMapping(value = "/viewDatasetOrg")
	public String viewAgency(@RequestParam("id") long id, Model model) {
		DatasetOrganization org = datasetService.getDatasetOrganization(id);
		model.addAttribute("org", org);
		if (org.getLink() == null) {
			model.addAttribute("orgsForLink", dataService.getAllOrganizations());
		}

		return "datasets/viewDatasetOrganization";
	}

	@PostMapping(value = "/createOrgFromDataset")
	public String createOrgFromDatasetPost(@ModelAttribute("id") Long id) {
		DatasetOrganization org = datasetService.getDatasetOrganization(id);
		Organization newOrg = datasetService.createOrgFromDatasetOrg(org);
		dataService.saveOrganization(newOrg);
		datasetService.linkDatasetOrg(org, newOrg);
		return "redirect:viewDatasetOrg?id=" + id;
	}

	@PostMapping(value = "/approveDataset")
	public String approveDatasetPost(@ModelAttribute("id") Long id, BindingResult bindingResult) {
		String retval = "";
		if (datasetService.approveDataset(id)) {
			retval = "redirect:home";
		} else {
			retval = "datasets/viewDataset?id=" + id;
		}
		return retval;
	}

	@PostMapping(value = "/registerOrgLink")
	public String registerOrgLinkPost(@ModelAttribute("datasetOrgId") Long id, @ModelAttribute("orgId") Long orgId) {
		DatasetOrganization dsOrg = datasetService.getDatasetOrganization(id);
		Organization org = dataService.getOrganization(orgId);
		datasetService.linkDatasetOrg(dsOrg, org);
		return "redirect:viewDatasetOrg?id=" + id;
	}

	@PostMapping(value = "/expressCreateAndSetLink")
	public String expressCreateAndSetLink(@ModelAttribute("id") Long id) {
		for (DatasetOrganization org : datasetService.getDatasetWarningOrgs(id)) {
			Organization newOrg = datasetService.createOrgFromDatasetOrg(org);
			dataService.saveOrganization(newOrg);
			datasetService.linkDatasetOrg(org, newOrg);

		}
		return "redirect:viewDataset?id=" + id;
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
