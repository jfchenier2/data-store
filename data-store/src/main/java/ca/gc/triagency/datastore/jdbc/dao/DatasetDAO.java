package ca.gc.triagency.datastore.jdbc.dao;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;


public interface DatasetDAO<T>{
	
	public void setDataSource(DataSource dataSource);
	
//	public List<Dataset> listDatasets();
//	
//	public Dataset findDatasetById(long id);
	
	public void insertMasterBatch(List<T> applications);
}
