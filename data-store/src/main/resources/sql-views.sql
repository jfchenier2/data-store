create or replace view data_store.report_approved_applications as 
	select app.id as id, app.ext_id as appl_id, app.program_year as program_year, p.id as program_id,  p.name_en as program_name_en, p.name_fr as program_name_fr, a.id as agency_id, a.acronym_en agency_name_en, a.acronym_fr as agency_name_fr
		from dataset_application app
		join dataset ds on app.dataset_id=ds.id
		join dataset_program ds_program on app.dataset_program_id=ds_program.id
		join entity_link_program elp on ds_program.entity_link_id=elp.id
		join program p on elp.program_id=p.id
		join agency a on p.lead_agency_id=a.id
		where ds.dataset_status like 'APPROVED';

create or replace view data_store.report_approved_application_participations_edi as 
	select pea.*, app.ext_id as appl_id, p.id as program_id,  p.name_en as program_name_en, p.name_fr as program_name_fr, a.id as agency_id, a.acronym_en agency_name_en, a.acronym_fr as agency_name_fr, o.id as org_id, o.name_en as org_name_en, o.name_fr as org_name_fr, ds_org.city as org_city, ds_org.postal_zip_code as postal_zip_code
		from dataset_application app
		join dataset ds on app.dataset_id=ds.id
		join dataset_program ds_program on app.dataset_program_id=ds_program.id
		join entity_link_program elp on ds_program.entity_link_id=elp.id
		join program p on elp.program_id=p.id
		left join agency a on p.lead_agency_id=a.id
		join dataset_application_registration ds_ar on ds_ar.dataset_application_id=app.id
		join dataset_organization ds_org on ds_ar.dataset_organization_id=ds_org.id
		join entity_link_organization elo on ds_org.entity_link_id=elo.id
		join organization o on elo.org_id=o.id
		join participation_edi_data pea on ds_ar.participation_edi_data_id=pea.id
		where ds.dataset_status like 'APPROVED';

create or replace view data_store.report_approved_application_participations as 
	select ds_ar.id as id, app.ext_id as appl_id, p.id as program_id,  p.name_en as program_name_en, p.name_fr as program_name_fr, a.id as agency_id, a.acronym_en agency_name_en, a.acronym_fr as agency_name_fr, o.id as org_id, o.name_en as org_name_en, o.name_fr as org_name_fr, ds_org.city as org_city, ds_org.postal_zip_code as postal_zip_code
		from dataset_application app
		join dataset ds on app.dataset_id=ds.id
		join dataset_program ds_program on app.dataset_program_id=ds_program.id
		join entity_link_program elp on ds_program.entity_link_id=elp.id
		join program p on elp.program_id=p.id
		left join agency a on p.lead_agency_id=a.id
		join dataset_application_registration ds_ar on ds_ar.dataset_application_id=app.id
		join dataset_organization ds_org on ds_ar.dataset_organization_id=ds_org.id
		join entity_link_organization elo on ds_org.entity_link_id=elo.id
		join organization o on elo.org_id=o.id
		where ds.dataset_status like 'APPROVED';

create or replace view data_store.report_approved_awards as 
	select award.id as id, award.program_year as date_year, app.ext_id as appl_id, p.id as program_id,  p.name_en as program_name_en, p.name_fr as program_name_fr, a.id as agency_id, a.acronym_en agency_name_en, a.acronym_fr as agency_name_fr, o.id as org_id, o.name_en as org_name_en, o.name_fr as org_name_fr, ds_org.city as org_city, ds_org.postal_zip_code as postal_zip_code, award.amount
		from dataset_application app
		join dataset_award award on app.id=award.dataset_application_id
		join dataset ds on app.dataset_id=ds.id
		join dataset_program ds_program on app.dataset_program_id=ds_program.id
		join entity_link_program elp on ds_program.entity_link_id=elp.id
		join program p on elp.program_id=p.id
		join agency a on p.lead_agency_id=a.id
		join dataset_application_registration ds_ar on ds_ar.dataset_application_id=app.id
		join dataset_organization ds_org on ds_ar.dataset_organization_id=ds_org.id
		join entity_link_organization elo on ds_org.entity_link_id=elo.id
		join organization o on elo.org_id=o.id
		where ds.dataset_status like 'APPROVED';
--		join dataset_application_registration ds_ar on ds_ar.dataset_application_id=app.id and ds_ar.dataset_person_id=award.dataset_person_id
		
		
create or replace view data_store.report_application_registrations_per_organization as 
select org.id as id, org.name_en, org.name_fr, count(*) as app_reg_num, count(Distinct app.id) as app_num, count(Distinct app.dataset_program_id) as program_num
from data_store.dataset_application_registration as reg 
			join data_store.dataset_organization ds_org 
				on reg.dataset_organization_id=ds_org.id            
			join data_store.entity_link_organization as link_org
				on ds_org.entity_link_id = link_org.id
			join data_store.organization as org                  
				on link_org.org_id = org.id
    		join data_store.dataset_application as app
				on app.id = reg.dataset_application_id           
			join data_store.dataset as ds
				on app.dataset_id = ds.id and ds.dataset_status like 'APPROVED'
			group by id;
			

create or replace view data_cabin.orgs_with_external_link_num as 
	select org.*, count(org.id) as link_num 
    from data_cabin.organization as org
		join data_cabin.entity_link_organization as link
			on org.id=link.org_id
		group by org.id;
        
