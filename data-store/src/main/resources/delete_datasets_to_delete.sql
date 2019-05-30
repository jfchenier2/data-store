CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_datasets_to_delete`()
BEGIN

DECLARE v_id BIGINT DEFAULT 0;
DECLARE v_finished INTEGER DEFAULT 0;
DECLARE v_ds_type VARCHAR(100) DEFAULT "";

DECLARE id_cursor CURSOR FOR
	SELECT id FROM dataset
	WHERE dataset_status = "TO_DELETE";

DECLARE CONTINUE HANDLER FOR
	NOT FOUND SET v_finished = 1;

OPEN id_cursor;

delete_to_delete: LOOP

	FETCH id_cursor INTO v_id;

	IF v_finished = 1 THEN
		LEAVE delete_to_delete;
	END IF;

	SELECT dataset_type INTO v_ds_type
		FROM dataset
		WHERE id = v_id;

	DELETE FROM dataset
		WHERE id = v_id
		;

	IF v_ds_type = "APPLICATIONS" THEN
		DELETE FROM master_dataset 
			WHERE dataset_id = v_id
			;
		
		DELETE FROM dataset_app_registration_role
			WHERE id IN( SELECT registration_role_id
							FROM dataset_application_registration
							WHERE dataset_application_id IN( SELECT id
																FROM dataset_application
																WHERE dataset_id = v_id
																)
							)
			;
		
		DELETE FROM dataset_application_registration 
			WHERE dataset_application_id IN( SELECT id
												FROM dataset_application
												WHERE dataset_id = v_id
												)
			;
		
		DELETE FROM dataset_application 
			WHERE dataset_id = v_id
			;
		
		DELETE FROM dataset_program
			WHERE dataset_id = v_id
			;
		
		DELETE FROM dataset_organization
			WHERE dataset_id = v_id
			;
		
	--  	DELETE FROM dataset_award
	--  		WHERE dataset_application_id IN( SELECT id
	-- 											 	FROM dataset_application
	--      	                             	 	WHERE dataset_id = v_id
	-- 	                                     	 	)
	--  	;									 
	ELSEIF v_ds_type = "AWARDS" THEN
		DELETE FROM award_dataset 
			WHERE dataset_id = v_id
			;
		
	END IF;

END LOOP;

CLOSE id_cursor;

END