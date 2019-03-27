package ca.gc.triagency.datastore.jdbc;

import java.util.List;
import javax.sql.DataSource;

import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;
import ca.gc.triagency.datastore.model.file.AwardDatasetRow;

public interface DatasetDAO{
	
	public void setDataSource(DataSource dataSource);
	
//	public List<Dataset> listDatasets();
//	
//	public Dataset findDatasetById(long id);
	
	public void insertMasterBatch(List<ApplyDatasetRow> applications);
	
	public void insertAwardBatch(List<AwardDatasetRow> awards);
	
	public void deleteToDelete();
	
	public void deleteDatasetById(Long id);
	
//	public List<Long> getDatasetsToDelete();
}
