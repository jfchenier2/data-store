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
import ca.gc.triagency.datastore.model.Dataset.DatasetType;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.DatasetProgram;
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

	@GetMapping(value = "/createAwardDataset")
	public String createAwardDatasetSelectFile(@RequestParam long id, Model model) {
		model.addAttribute("parentDataset", datasetService.getDataset(id));
		model.addAttribute("datasetFiles", datasetService.getDatasetFiles());
		return "datasets/createAwardDataset";
	}

	@GetMapping(value = "/createAwardDataset", params = { "filename" })
	public String createAwardDataset(@RequestParam long id, @RequestParam String filename, Model model) {
		Dataset datasetForm = datasetService.configureNewDatasetFromFilename(filename);
		datasetForm.setParentDataset(datasetService.getDataset(id));
		model.addAttribute("form", datasetForm);
		return "datasets/createAwardDatasetValidate";
	}

	@Async
	@PostMapping(value = "/createAwardDataset")
	public String createAwardDatasetPost(@RequestParam long id, @RequestParam String filename) {
		Dataset form = datasetService.configureNewDatasetFromFilename(filename);
		form.setDatasetStatus(Dataset.DatasetStatus.CREATED);
		form.setParentDataset(datasetService.getDataset(id));
		form.setDatasetType(DatasetType.AWARDS);
		Dataset dataset = datasetService.saveDataset(form);
		datasetService.uploadAwardData(dataset);
		return "redirect:home";
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
		form.setDatasetType(DatasetType.APPLICATIONS);
		Dataset dataset = datasetService.saveDataset(form);
		datasetService.uploadData(dataset);
		return "redirect:home";
	}

	@GetMapping(value = "/viewDataset")
	public String viewDataset(@RequestParam("id") long id, Model model) {

		Dataset ds = datasetService.getDataset(id);
		ds = datasetService.markAssessIfFirstTimeView(ds);
		model.addAttribute("dataset", ds);
		/*if(ds.getDatasetType() == DatasetType.AWARDS) {
			return "datasets/viewAwardDataset";
			
		}*/
		model.addAttribute("warningOrgs", datasetService.getUnlinkedDatasetOrgs(id));
		model.addAttribute("warningProgs", datasetService.getUnlinkedDatasetPrograms(id));

		return "datasets/viewDataset";
	}

	@GetMapping(value = "/viewDatasetConfig")
	public String viewDataestConfig(@RequestParam("id") long id, Model model) {
		model.addAttribute("config", datasetService.getDatasetConfiguration(id));
		model.addAttribute("programLinks", datasetService.getDatasetConfigProgramLinks(id));
		model.addAttribute("orgLinks", datasetService.getDatasetConfigOrgLinks(id));
		return "datasets/viewDatasetConfiguration";
	}

	@GetMapping(value = "/viewDatasetOrg")
	public String viewDatasetOrg(@RequestParam("id") long id, @RequestParam("datasetId") long datasetId, Model model) {
		DatasetOrganization org = datasetService.getDatasetOrganization(id);
		model.addAttribute("datasetId", datasetId);
		model.addAttribute("org", org);
		if (org.getEntityLink() == null) {
			model.addAttribute("orgsForLink", dataService.getAllOrganizations());
		}

		return "datasets/viewDatasetOrganization";
	}

	@GetMapping(value = "/viewDatasetProgram")
	public String viewDatasetProgram(@RequestParam("id") long id, Model model) {
		DatasetProgram prog = datasetService.getDatasetProgram(id);
		model.addAttribute("datasetId", prog.getDataset().getId());
		model.addAttribute("prog", prog);
		if (prog.getEntityLink() == null) {
			model.addAttribute("progsForLink", dataService.getAllPrograms());
		}

		return "datasets/viewDatasetProgram";
	}

	@PostMapping(value = "/registerProgLink")
	public String registerProgramLinkPost(@ModelAttribute("datasetProgramId") Long id,
			@ModelAttribute("progId") Long progId) {
		DatasetProgram dsProg = datasetService.getDatasetProgram(id);
		Program prog = dataService.getProgram(progId);
		datasetService.linkDatasetProgram(dsProg, prog);
		return "redirect:viewDataset?id=" + dsProg.getDataset().getId();
	}

	@PostMapping(value = "/createProgFromDataset")
	public String createProgramFromDatasetPost(@ModelAttribute("id") Long id) {
		DatasetProgram prog = datasetService.getDatasetProgram(id);
		Program newProg = datasetService.createProgramFromDatasetProg(prog);
		dataService.saveProgram(newProg);
		datasetService.linkDatasetProgram(prog, newProg);
		return "redirect:viewDatasetProgram?id=" + id;
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
	public String registerOrgLinkPost(@ModelAttribute("datasetOrgId") Long id, @ModelAttribute("orgId") Long orgId,
			@ModelAttribute("datasetId") Long datasetId) {
		DatasetOrganization dsOrg = datasetService.getDatasetOrganization(id);
		Organization org = dataService.getOrganization(orgId);
		datasetService.linkDatasetOrg(dsOrg, org);
		return "redirect:viewDataset?id=" + datasetId;
	}

	@PostMapping(value = "/linkToProgramEntityLinks")
	public String linkToProgramEntityLinksPost(@ModelAttribute("id") Long datasetId) {
		datasetService.linkToProgramEntityLinks(datasetId);
		return "redirect:viewDataset?id=" + datasetId;
	}

	@PostMapping(value = "/linkToOrgEntityLinks")
	public String linkToOrgEntityLinksPost(@ModelAttribute("id") Long datasetId) {
		datasetService.linkToOrgEntityLinks(datasetId);
		return "redirect:viewDataset?id=" + datasetId;
	}

	@PostMapping(value = "/linkMatchingOrgEntities")
	public String linkMatchingOrgEntitiesPost(@ModelAttribute("id") Long id) {
		datasetService.linkMatchingOrgEntities(id);
		return "redirect:viewDataset?id=" + id;
	}

	@PostMapping(value = "/linkMatchingProgEntities")
	public String linkMatchingProgramEntitiesPost(@ModelAttribute("id") Long id) {
		datasetService.linkMatchingProgramEntities(id);
		return "redirect:viewDataset?id=" + id;
	}

	@PostMapping(value = "/expressCreateAndSetLinkPrograms")
	public String expressCreateAndSetLinkPrograms(@ModelAttribute("id") Long id) {
		for (DatasetProgram prog : datasetService.getUnlinkedDatasetPrograms(id)) {
			Program newProg = datasetService.createProgramFromDatasetProg(prog);
			dataService.saveProgram(newProg);
			datasetService.linkDatasetProgram(prog, newProg);

		}
		return "redirect:viewDataset?id=" + id;
	}

	@PostMapping(value = "/expressCreateAndSetLink")
	public String expressCreateAndSetLinkOrgs(@ModelAttribute("id") Long id) {
		for (DatasetOrganization org : datasetService.getUnlinkedDatasetOrgs(id)) {
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
