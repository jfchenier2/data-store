CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_dataset_by_id`(
IN in_id BIGINT)
BEGIN
	DECLARE ds_type VARCHAR(255);
    
    SELECT dataset_type INTO ds_type
    FROM dataset
    WHERE id = in_id
    ;
    
    DELETE FROM dataset
    WHERE id = in_id
    ;
    
    IF ds_type = "APPLICATIONS" THEN
		DELETE FROM master_dataset 
		WHERE dataset_id = in_id
		;
		
		DELETE FROM dataset_app_registration_role
		WHERE id IN( SELECT registration_role_id
					 FROM dataset_application_registration
					 WHERE dataset_application_id IN( SELECT id
													  FROM dataset_application
													  WHERE dataset_id = in_id
													  )
					 )
		;
		
		DELETE FROM dataset_application_registration 
		WHERE dataset_application_id IN( SELECT id
										 FROM dataset_application
										 WHERE dataset_id = in_id
										 )
		;
		
		DELETE FROM dataset_application 
		WHERE dataset_id = in_id
		;
		
		DELETE FROM dataset_program
		WHERE dataset_id = in_id
		;
		
		DELETE FROM dataset_organization
		WHERE dataset_id = in_id
		;
		
--  	DELETE FROM dataset_award
--  	WHERE dataset_application_id IN( SELECT id
-- 										 FROM dataset_application
--      	                             WHERE dataset_id = in_id
-- 	                                     )
--  	;									 
    ELSEIF ds_type = "AWARDS" THEN
		DELETE FROM award_dataset 
		WHERE dataset_id = in_id
		;
        
	END IF;
END