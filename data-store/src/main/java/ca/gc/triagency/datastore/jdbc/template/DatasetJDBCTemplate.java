package ca.gc.triagency.datastore.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import ca.gc.triagency.datastore.jdbc.dao.DatasetDAO;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;

public class DatasetJDBCTemplate implements DatasetDAO<ApplyDatasetRow> {
	
	
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
//	@Override
//	public List<Dataset> listDatasets(){
//		String sql = "SELECT * FROM data_store.dataset";
//		List<Dataset> datasets = jdbcTemplateObject.query(sql, new DatasetMapper());
//		return datasets;
//	}
//	
//	@Override
//	public Dataset findDatasetById(long id){
//		String sql = "SELECT * FROM data_store.dataset WHERE id = ?";
//		Dataset result = jdbcTemplateObject.queryForObject(sql, new Object[] {id}, new DatasetMapper());
//		return result;
//	}
	
	@Override
	public void insertMasterBatch(List<ApplyDatasetRow> applications){
		String sql = "INSERT INTO master_dataset " +
				"(Id, Source, Application_Identifier, Appl_ID, Competition_Year, Program_ID, Program_En, Program_Fr, Create_Date, Role_Code, Role_En, Role_Fr, Family_Name, Given_Name, Person_Identifier, Org_ID, Org_Name_En, Org_Name_Fr, Addr_One, Addr_Two, Addr_Three, Addr_Four, City, Postal_Zip_Code, State_Prov_Code, Country) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
			jdbcTemplateObject.batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ApplyDatasetRow application = applications.get(i);
					
					ps.setLong(1, application.getId());
					ps.setString(2, application.getSource());
					ps.setString(3, application.getApplicationIdentifier());
					ps.setLong(4, application.getApplId());
					ps.setInt(5, application.getCompetitionYear());
					ps.setString(6, application.getProgramId());
					ps.setString(7, application.getProgramEn());
					ps.setString(8, application.getProgramFr());
					ps.setString(9, application.getCreateDate());
					ps.setInt(10, application.getRoleCode());
					ps.setString(11, application.getRoleEn());
					ps.setString(12, application.getRoleFr());
					ps.setString(13, application.getFamilyName());
					ps.setString(14, application.getGivenName());
					ps.setLong(15, application.getPersonIdentifier());
					ps.setLong(16, application.getOrgId());
					ps.setString(17, application.getOrgNameEn());
					ps.setString(18, application.getOrgNameFr());
					ps.setString(19, application.getAddrOne());
					ps.setString(20, application.getAddrTwo());
					ps.setString(21, application.getAddrThree());
					ps.setString(22, application.getAddrFour());
					ps.setString(23, application.getCity());
					ps.setString(24, application.getPostalZipCode());
					ps.setString(25, application.getStateProvCode());
					ps.setString(26, application.getCountry());
				}
				
				@Override
				public int getBatchSize() {
					return applications.size();
				}
			});
	}
	
}
