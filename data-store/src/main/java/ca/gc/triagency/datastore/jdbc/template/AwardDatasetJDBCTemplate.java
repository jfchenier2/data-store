package ca.gc.triagency.datastore.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import ca.gc.triagency.datastore.jdbc.dao.DatasetDAO;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;
import ca.gc.triagency.datastore.model.file.AwardDatasetRow;

public class AwardDatasetJDBCTemplate implements DatasetDAO<AwardDatasetRow>{
	
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
	public void insertMasterBatch(List<AwardDatasetRow> awards){
		String sql = "INSERT INTO award_dataset " +
				"(Id, Source, Application_Identifier, Application_ID, Competition_Year, Program_ID, Program_En, Program_Fr, Create_Date, Role_Code, Role_En, Role_Fr, Family_Name, Given_Name, Person_Identifier, Awarded_Amount, Funding_Year) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
			}
			
			@Override
			public int getBatchSize() {
				return awards.size();
			}
		});
	}
}
