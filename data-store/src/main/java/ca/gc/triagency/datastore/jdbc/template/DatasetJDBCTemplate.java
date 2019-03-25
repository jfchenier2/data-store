package ca.gc.triagency.datastore.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import ca.gc.triagency.datastore.jdbc.DatasetDAO;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;
import ca.gc.triagency.datastore.model.file.AwardDatasetRow;

public class DatasetJDBCTemplate implements DatasetDAO {
	
	
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
				"(Id, Source, Application_Identifier, Appl_ID, Competition_Year, Program_ID, Program_En, Program_Fr, Create_Date, Role_Code, Role_En, Role_Fr, Family_Name, Given_Name, Person_Identifier, Org_ID, Org_Name_En, Org_Name_Fr, Addr_One, Addr_Two, Addr_Three, Addr_Four, City, Postal_Zip_Code, State_Prov_Code, Country, Dataset_Id) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
				ps.setLong(27, application.getDatasetId());
			}
			
			@Override
			public int getBatchSize() {
				return applications.size();
			}
		});
	}
	
	@Override
	public void insertAwardBatch(List<AwardDatasetRow> awards){
		String sql = "INSERT INTO award_dataset " +
				"(Id, Source, Application_Identifier, Application_ID, Competition_Year, Program_ID, Program_En, Program_Fr, Create_Date, Role_Code, Role_En, Role_Fr, Family_Name, Given_Name, Person_Identifier, Awarded_Amount, Funding_Year, Dataset_Id) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplateObject.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AwardDatasetRow award = awards.get(i);
				
				ps.setLong(1, award.getId());
				ps.setString(2, award.getSource());
				ps.setString(3, award.getApplicationIdentifier());
				ps.setLong(4, award.getApplicationId());
				ps.setInt(5, award.getCompetitionYear());
				ps.setString(6, award.getProgramId());
				ps.setString(7, award.getProgramEn());
				ps.setString(8, award.getProgramFr());
				ps.setString(9, award.getCreateDate());
				ps.setInt(10, award.getRoleCode());
				ps.setString(11, award.getRoleEn());
				ps.setString(12, award.getRoleFr());
				ps.setString(13, award.getFamilyName());
				ps.setString(14, award.getGivenName());
				ps.setLong(15, award.getPersonIdentifier());
//				ps.setLong(15, application.getOrgId());
//				ps.setString(16, application.getOrgNameEn());
//				ps.setString(17, application.getOrgNameFr());
				ps.setString(16, award.getAwardedAmount());
				ps.setInt(17, award.getFundingYear());
				ps.setLong(18, award.getDatasetId());
			}
			
			@Override
			public int getBatchSize() {
				return awards.size();
			}
		});
	}
	
	@Override
	public void deleteDatasetById(Long id) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("delete_dataset_by_id");
		SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
		jdbcCall.execute(in);
	}
	
	@Override
	public List<Dataset> getDatasetsToDelete(){
		String sql = "SELECT * FROM data_store.dataset WHERE dataset_status = \"TO_DELETE\"";
		List<Dataset> toDelete = null;
		try {
			toDelete = jdbcTemplateObject.queryForList(sql, Dataset.class);
		}catch (DataAccessException e) {
			System.out.println("Couldn't delete datasets. Error accessing data.");
		}
		
		return toDelete;
	}
	
}
