package ca.gc.triagency.datastore.service;

import java.util.Collection;

import ca.gc.triagency.datastore.model.file.ProgramFromFile;

public interface ImportService {

	public void importAgencies();

	public void importProgramsFromFile();

	public Collection<ProgramFromFile> extractProgramsFromFile(String filename);

}
