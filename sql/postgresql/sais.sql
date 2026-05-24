-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION postgres;

-- DROP SEQUENCE infra_api_access_log_seq;

CREATE SEQUENCE infra_api_access_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_api_error_log_seq;

CREATE SEQUENCE infra_api_error_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_codegen_column_seq;

CREATE SEQUENCE infra_codegen_column_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_codegen_table_seq;

CREATE SEQUENCE infra_codegen_table_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_config_seq;

CREATE SEQUENCE infra_config_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 14
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_data_source_config_seq;

CREATE SEQUENCE infra_data_source_config_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_file_config_seq;

CREATE SEQUENCE infra_file_config_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 36
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_file_content_seq;

CREATE SEQUENCE infra_file_content_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_file_seq;

CREATE SEQUENCE infra_file_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_job_log_seq;

CREATE SEQUENCE infra_job_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE infra_job_seq;

CREATE SEQUENCE infra_job_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 41
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_alert_id_seq;

CREATE SEQUENCE sais_alert_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_crop_id_seq;

CREATE SEQUENCE sais_crop_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_crop_plan_id_seq;

CREATE SEQUENCE sais_crop_plan_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_decision_evaluation_id_seq;

CREATE SEQUENCE sais_decision_evaluation_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_farm_id_seq;

CREATE SEQUENCE sais_farm_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_field_id_seq;

CREATE SEQUENCE sais_field_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_growth_stage_id_seq;

CREATE SEQUENCE sais_growth_stage_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_irrigation_device_code_seq;

CREATE SEQUENCE sais_irrigation_device_code_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_irrigation_device_id_seq;

CREATE SEQUENCE sais_irrigation_device_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_irrigation_plan_id_seq;

CREATE SEQUENCE sais_irrigation_plan_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_sensor_code_seq;

CREATE SEQUENCE sais_sensor_code_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_sensor_data_id_seq;

CREATE SEQUENCE sais_sensor_data_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_sensor_id_seq;

CREATE SEQUENCE sais_sensor_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sais_weather_data_id_seq;

CREATE SEQUENCE sais_weather_data_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_dept_seq;

CREATE SEQUENCE system_dept_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 114
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_dict_data_seq;

CREATE SEQUENCE system_dict_data_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 3036
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_dict_type_seq;

CREATE SEQUENCE system_dict_type_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 2008
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_login_log_seq;

CREATE SEQUENCE system_login_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_mail_account_seq;

CREATE SEQUENCE system_mail_account_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 5
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_mail_log_seq;

CREATE SEQUENCE system_mail_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_mail_template_seq;

CREATE SEQUENCE system_mail_template_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 16
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_menu_seq;

CREATE SEQUENCE system_menu_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 5047
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_notice_seq;

CREATE SEQUENCE system_notice_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 5
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_notify_message_seq;

CREATE SEQUENCE system_notify_message_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 11
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_notify_template_seq;

CREATE SEQUENCE system_notify_template_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_oauth2_access_token_seq;

CREATE SEQUENCE system_oauth2_access_token_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_oauth2_approve_seq;

CREATE SEQUENCE system_oauth2_approve_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_oauth2_client_seq;

CREATE SEQUENCE system_oauth2_client_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 43
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_oauth2_code_seq;

CREATE SEQUENCE system_oauth2_code_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_oauth2_refresh_token_seq;

CREATE SEQUENCE system_oauth2_refresh_token_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_operate_log_seq;

CREATE SEQUENCE system_operate_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_post_seq;

CREATE SEQUENCE system_post_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 8
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_role_menu_seq;

CREATE SEQUENCE system_role_menu_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 6293
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_role_seq;

CREATE SEQUENCE system_role_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 156
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_sms_channel_seq;

CREATE SEQUENCE system_sms_channel_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 8
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_sms_code_seq;

CREATE SEQUENCE system_sms_code_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_sms_log_seq;

CREATE SEQUENCE system_sms_log_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_sms_template_seq;

CREATE SEQUENCE system_sms_template_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 20
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_social_client_seq;

CREATE SEQUENCE system_social_client_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 46
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_social_user_bind_seq;

CREATE SEQUENCE system_social_user_bind_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_social_user_seq;

CREATE SEQUENCE system_social_user_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_tenant_package_seq;

CREATE SEQUENCE system_tenant_package_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 112
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_tenant_seq;

CREATE SEQUENCE system_tenant_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 123
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_user_post_seq;

CREATE SEQUENCE system_user_post_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 126
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_user_role_seq;

CREATE SEQUENCE system_user_role_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 51
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE system_users_seq;

CREATE SEQUENCE system_users_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 143
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE yudao_demo01_contact_seq;

CREATE SEQUENCE yudao_demo01_contact_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 2
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE yudao_demo02_category_seq;

CREATE SEQUENCE yudao_demo02_category_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 8
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE yudao_demo03_course_seq;

CREATE SEQUENCE yudao_demo03_course_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 21
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE yudao_demo03_grade_seq;

CREATE SEQUENCE yudao_demo03_grade_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE yudao_demo03_student_seq;

CREATE SEQUENCE yudao_demo03_student_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
	CACHE 1
	NO CYCLE;-- public.dual definition

-- Drop table

-- DROP TABLE dual;

CREATE TABLE dual (
                      id int2 NULL
);
COMMENT ON TABLE public.dual IS 'Table used for database connectivity checks';


-- public.infra_api_access_log definition

-- Drop table

-- DROP TABLE infra_api_access_log;

CREATE TABLE infra_api_access_log (
                                      id int8 NOT NULL, -- Log primary key
                                      trace_id varchar(64) DEFAULT ''::character varying NOT NULL, -- Trace tracking ID
                                      user_id int8 DEFAULT 0 NOT NULL, -- User ID
                                      user_type int2 DEFAULT 0 NOT NULL, -- User type
                                      application_name varchar(50) NOT NULL, -- Application name
                                      request_method varchar(16) DEFAULT ''::character varying NOT NULL, -- HTTP request method
                                      request_url varchar(255) DEFAULT ''::character varying NOT NULL, -- Request URL
                                      request_params text NULL, -- Request parameters
                                      response_body text NULL, -- Response result
                                      user_ip varchar(50) NOT NULL, -- User IP address
                                      user_agent varchar(512) NOT NULL, -- Browser user agent (UA)
                                      operate_module varchar(50) DEFAULT NULL::character varying NULL, -- Operation module
                                      operate_name varchar(50) DEFAULT NULL::character varying NULL, -- Operation name
                                      operate_type int2 DEFAULT 0 NULL, -- Operation category
                                      begin_time timestamp NOT NULL, -- Request start time
                                      end_time timestamp NOT NULL, -- Request end time
                                      duration int4 NOT NULL, -- Execution duration (ms)
                                      result_code int4 DEFAULT 0 NOT NULL, -- Result code
                                      result_msg varchar(512) DEFAULT ''::character varying NULL, -- Result message
                                      creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                      updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                      deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                      tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                      CONSTRAINT pk_infra_api_access_log PRIMARY KEY (id)
);
CREATE INDEX idx_infra_api_access_log_01 ON public.infra_api_access_log USING btree (create_time);
COMMENT ON TABLE public.infra_api_access_log IS 'API access log table';

-- Column comments

COMMENT ON COLUMN public.infra_api_access_log.id IS 'Log primary key';
COMMENT ON COLUMN public.infra_api_access_log.trace_id IS 'Trace tracking ID';
COMMENT ON COLUMN public.infra_api_access_log.user_id IS 'User ID';
COMMENT ON COLUMN public.infra_api_access_log.user_type IS 'User type';
COMMENT ON COLUMN public.infra_api_access_log.application_name IS 'Application name';
COMMENT ON COLUMN public.infra_api_access_log.request_method IS 'HTTP request method';
COMMENT ON COLUMN public.infra_api_access_log.request_url IS 'Request URL';
COMMENT ON COLUMN public.infra_api_access_log.request_params IS 'Request parameters';
COMMENT ON COLUMN public.infra_api_access_log.response_body IS 'Response result';
COMMENT ON COLUMN public.infra_api_access_log.user_ip IS 'User IP address';
COMMENT ON COLUMN public.infra_api_access_log.user_agent IS 'Browser user agent (UA)';
COMMENT ON COLUMN public.infra_api_access_log.operate_module IS 'Operation module';
COMMENT ON COLUMN public.infra_api_access_log.operate_name IS 'Operation name';
COMMENT ON COLUMN public.infra_api_access_log.operate_type IS 'Operation category';
COMMENT ON COLUMN public.infra_api_access_log.begin_time IS 'Request start time';
COMMENT ON COLUMN public.infra_api_access_log.end_time IS 'Request end time';
COMMENT ON COLUMN public.infra_api_access_log.duration IS 'Execution duration (ms)';
COMMENT ON COLUMN public.infra_api_access_log.result_code IS 'Result code';
COMMENT ON COLUMN public.infra_api_access_log.result_msg IS 'Result message';
COMMENT ON COLUMN public.infra_api_access_log.creator IS 'Creator';
COMMENT ON COLUMN public.infra_api_access_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_api_access_log.updater IS 'Updater';
COMMENT ON COLUMN public.infra_api_access_log.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_api_access_log.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.infra_api_access_log.tenant_id IS 'Tenant ID';


-- public.infra_api_error_log definition

-- Drop table

-- DROP TABLE infra_api_error_log;

CREATE TABLE infra_api_error_log (
                                     id int8 NOT NULL, -- Log ID
                                     trace_id varchar(64) NOT NULL, -- Trace tracking ID
                                     user_id int8 DEFAULT 0 NOT NULL, -- User ID
                                     user_type int2 DEFAULT 0 NOT NULL, -- User type
                                     application_name varchar(50) NOT NULL, -- Application name
                                     request_method varchar(16) NOT NULL, -- HTTP request method
                                     request_url varchar(255) NOT NULL, -- Request URL
                                     request_params varchar(8000) NOT NULL, -- Request parameters
                                     user_ip varchar(50) NOT NULL, -- User IP address
                                     user_agent varchar(512) NOT NULL, -- Browser user agent (UA)
                                     exception_time timestamp NOT NULL, -- Time the exception occurred
                                     exception_name varchar(128) DEFAULT ''::character varying NOT NULL, -- Exception class name (short)
                                     exception_message text NOT NULL, -- Exception message
                                     exception_root_cause_message text NOT NULL, -- Root cause exception message
                                     exception_stack_trace text NOT NULL, -- Exception stack trace
                                     exception_class_name varchar(512) NOT NULL, -- Fully-qualified class where exception occurred
                                     exception_file_name varchar(512) NOT NULL, -- Source file where exception occurred
                                     exception_method_name varchar(512) NOT NULL, -- Method where exception occurred
                                     exception_line_number int4 NOT NULL, -- Line number where exception occurred
                                     process_status int2 NOT NULL, -- Processing status
                                     process_time timestamp NULL, -- Processing time
                                     process_user_id int4 DEFAULT 0 NULL, -- ID of user who processed the exception
                                     creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                     create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                     updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                     update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                     deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                     tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                     CONSTRAINT pk_infra_api_error_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_api_error_log IS 'System exception log table';

-- Column comments

COMMENT ON COLUMN public.infra_api_error_log.id IS 'Log ID';
COMMENT ON COLUMN public.infra_api_error_log.trace_id IS 'Trace tracking ID';
COMMENT ON COLUMN public.infra_api_error_log.user_id IS 'User ID';
COMMENT ON COLUMN public.infra_api_error_log.user_type IS 'User type';
COMMENT ON COLUMN public.infra_api_error_log.application_name IS 'Application name';
COMMENT ON COLUMN public.infra_api_error_log.request_method IS 'HTTP request method';
COMMENT ON COLUMN public.infra_api_error_log.request_url IS 'Request URL';
COMMENT ON COLUMN public.infra_api_error_log.request_params IS 'Request parameters';
COMMENT ON COLUMN public.infra_api_error_log.user_ip IS 'User IP address';
COMMENT ON COLUMN public.infra_api_error_log.user_agent IS 'Browser user agent (UA)';
COMMENT ON COLUMN public.infra_api_error_log.exception_time IS 'Time the exception occurred';
COMMENT ON COLUMN public.infra_api_error_log.exception_name IS 'Exception class name (short)';
COMMENT ON COLUMN public.infra_api_error_log.exception_message IS 'Exception message';
COMMENT ON COLUMN public.infra_api_error_log.exception_root_cause_message IS 'Root cause exception message';
COMMENT ON COLUMN public.infra_api_error_log.exception_stack_trace IS 'Exception stack trace';
COMMENT ON COLUMN public.infra_api_error_log.exception_class_name IS 'Fully-qualified class where exception occurred';
COMMENT ON COLUMN public.infra_api_error_log.exception_file_name IS 'Source file where exception occurred';
COMMENT ON COLUMN public.infra_api_error_log.exception_method_name IS 'Method where exception occurred';
COMMENT ON COLUMN public.infra_api_error_log.exception_line_number IS 'Line number where exception occurred';
COMMENT ON COLUMN public.infra_api_error_log.process_status IS 'Processing status';
COMMENT ON COLUMN public.infra_api_error_log.process_time IS 'Processing time';
COMMENT ON COLUMN public.infra_api_error_log.process_user_id IS 'ID of user who processed the exception';
COMMENT ON COLUMN public.infra_api_error_log.creator IS 'Creator';
COMMENT ON COLUMN public.infra_api_error_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_api_error_log.updater IS 'Updater';
COMMENT ON COLUMN public.infra_api_error_log.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_api_error_log.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.infra_api_error_log.tenant_id IS 'Tenant ID';


-- public.infra_codegen_column definition

-- Drop table

-- DROP TABLE infra_codegen_column;

CREATE TABLE infra_codegen_column (
                                      id int8 NOT NULL, -- Record ID
                                      table_id int8 NOT NULL, -- Parent table ID
                                      column_name varchar(200) NOT NULL, -- Column name
                                      data_type varchar(100) NOT NULL, -- Column data type
                                      column_comment varchar(500) NOT NULL, -- Column description / comment
                                      "nullable" bool NOT NULL, -- Whether the column is nullable
                                      primary_key bool NOT NULL, -- Whether the column is the primary key
                                      ordinal_position int4 NOT NULL, -- Column ordinal position (sort order)
                                      java_type varchar(32) NOT NULL, -- Java property type
                                      java_field varchar(64) NOT NULL, -- Java property name
                                      dict_type varchar(200) DEFAULT ''::character varying NULL, -- Dictionary type
                                      example varchar(64) DEFAULT NULL::character varying NULL, -- Example value
                                      create_operation bool NOT NULL, -- Whether included in Create operation
                                      update_operation bool NOT NULL, -- Whether included in Update operation
                                      list_operation bool NOT NULL, -- Whether included in List query operation
                                      list_operation_condition varchar(32) DEFAULT '='::character varying NOT NULL, -- Condition type for List query operation
                                      list_operation_result bool NOT NULL, -- Whether returned in List query results
                                      html_type varchar(32) NOT NULL, -- Front-end display type
                                      creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                      updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                      deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                      CONSTRAINT pk_infra_codegen_column PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_codegen_column IS 'Code-generation table: column definitions';

-- Column comments

COMMENT ON COLUMN public.infra_codegen_column.id IS 'Record ID';
COMMENT ON COLUMN public.infra_codegen_column.table_id IS 'Parent table ID';
COMMENT ON COLUMN public.infra_codegen_column.column_name IS 'Column name';
COMMENT ON COLUMN public.infra_codegen_column.data_type IS 'Column data type';
COMMENT ON COLUMN public.infra_codegen_column.column_comment IS 'Column description / comment';
COMMENT ON COLUMN public.infra_codegen_column."nullable" IS 'Whether the column is nullable';
COMMENT ON COLUMN public.infra_codegen_column.primary_key IS 'Whether the column is the primary key';
COMMENT ON COLUMN public.infra_codegen_column.ordinal_position IS 'Column ordinal position (sort order)';
COMMENT ON COLUMN public.infra_codegen_column.java_type IS 'Java property type';
COMMENT ON COLUMN public.infra_codegen_column.java_field IS 'Java property name';
COMMENT ON COLUMN public.infra_codegen_column.dict_type IS 'Dictionary type';
COMMENT ON COLUMN public.infra_codegen_column.example IS 'Example value';
COMMENT ON COLUMN public.infra_codegen_column.create_operation IS 'Whether included in Create operation';
COMMENT ON COLUMN public.infra_codegen_column.update_operation IS 'Whether included in Update operation';
COMMENT ON COLUMN public.infra_codegen_column.list_operation IS 'Whether included in List query operation';
COMMENT ON COLUMN public.infra_codegen_column.list_operation_condition IS 'Condition type for List query operation';
COMMENT ON COLUMN public.infra_codegen_column.list_operation_result IS 'Whether returned in List query results';
COMMENT ON COLUMN public.infra_codegen_column.html_type IS 'Front-end display type';
COMMENT ON COLUMN public.infra_codegen_column.creator IS 'Creator';
COMMENT ON COLUMN public.infra_codegen_column.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_codegen_column.updater IS 'Updater';
COMMENT ON COLUMN public.infra_codegen_column.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_codegen_column.deleted IS 'Soft-delete flag';


-- public.infra_codegen_table definition

-- Drop table

-- DROP TABLE infra_codegen_table;

CREATE TABLE infra_codegen_table (
                                     id int8 NOT NULL, -- Record ID
                                     data_source_config_id int8 NOT NULL, -- Data source configuration ID
                                     scene int2 DEFAULT 1 NOT NULL, -- Generation scene / context
                                     table_name varchar(200) DEFAULT ''::character varying NOT NULL, -- Table name
                                     table_comment varchar(500) DEFAULT ''::character varying NOT NULL, -- Table description
                                     remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                                     module_name varchar(30) NOT NULL, -- Module name
                                     business_name varchar(30) NOT NULL, -- Business / feature name
                                     class_name varchar(100) DEFAULT ''::character varying NOT NULL, -- Java class name
                                     class_comment varchar(50) NOT NULL, -- Java class description
                                     author varchar(50) NOT NULL, -- Author
                                     template_type int2 DEFAULT 1 NOT NULL, -- Template type
                                     front_type int2 NOT NULL, -- Front-end type
                                     parent_menu_id int8 NULL, -- Parent menu ID
                                     master_table_id int8 NULL, -- Master table ID
                                     sub_join_column_id int8 NULL, -- Sub-table join column ID (links to master)
                                     sub_join_many bool NULL, -- Whether master-to-sub relationship is one-to-many
                                     tree_parent_column_id int8 NULL, -- Parent column ID for tree table
                                     tree_name_column_id int8 NULL, -- Name column ID for tree table
                                     creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                     create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                     updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                     update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                     deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                     CONSTRAINT pk_infra_codegen_table PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_codegen_table IS 'Code-generation table: table definitions';

-- Column comments

COMMENT ON COLUMN public.infra_codegen_table.id IS 'Record ID';
COMMENT ON COLUMN public.infra_codegen_table.data_source_config_id IS 'Data source configuration ID';
COMMENT ON COLUMN public.infra_codegen_table.scene IS 'Generation scene / context';
COMMENT ON COLUMN public.infra_codegen_table.table_name IS 'Table name';
COMMENT ON COLUMN public.infra_codegen_table.table_comment IS 'Table description';
COMMENT ON COLUMN public.infra_codegen_table.remark IS 'Remarks';
COMMENT ON COLUMN public.infra_codegen_table.module_name IS 'Module name';
COMMENT ON COLUMN public.infra_codegen_table.business_name IS 'Business / feature name';
COMMENT ON COLUMN public.infra_codegen_table.class_name IS 'Java class name';
COMMENT ON COLUMN public.infra_codegen_table.class_comment IS 'Java class description';
COMMENT ON COLUMN public.infra_codegen_table.author IS 'Author';
COMMENT ON COLUMN public.infra_codegen_table.template_type IS 'Template type';
COMMENT ON COLUMN public.infra_codegen_table.front_type IS 'Front-end type';
COMMENT ON COLUMN public.infra_codegen_table.parent_menu_id IS 'Parent menu ID';
COMMENT ON COLUMN public.infra_codegen_table.master_table_id IS 'Master table ID';
COMMENT ON COLUMN public.infra_codegen_table.sub_join_column_id IS 'Sub-table join column ID (links to master)';
COMMENT ON COLUMN public.infra_codegen_table.sub_join_many IS 'Whether master-to-sub relationship is one-to-many';
COMMENT ON COLUMN public.infra_codegen_table.tree_parent_column_id IS 'Parent column ID for tree table';
COMMENT ON COLUMN public.infra_codegen_table.tree_name_column_id IS 'Name column ID for tree table';
COMMENT ON COLUMN public.infra_codegen_table.creator IS 'Creator';
COMMENT ON COLUMN public.infra_codegen_table.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_codegen_table.updater IS 'Updater';
COMMENT ON COLUMN public.infra_codegen_table.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_codegen_table.deleted IS 'Soft-delete flag';


-- public.infra_config definition

-- Drop table

-- DROP TABLE infra_config;

CREATE TABLE infra_config (
                              id int8 NOT NULL, -- Parameter primary key
                              category varchar(50) NOT NULL, -- Parameter group / category
                              "type" int2 NOT NULL, -- Parameter type
                              "name" varchar(100) DEFAULT ''::character varying NOT NULL, -- Parameter name
                              config_key varchar(100) DEFAULT ''::character varying NOT NULL, -- Parameter key
                              value varchar(500) DEFAULT ''::character varying NOT NULL, -- Parameter value
                              visible bool NOT NULL, -- Whether visible to end users
                              remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                              creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                              create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                              updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                              update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                              deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                              CONSTRAINT pk_infra_config PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_config IS 'System parameter configuration table';

-- Column comments

COMMENT ON COLUMN public.infra_config.id IS 'Parameter primary key';
COMMENT ON COLUMN public.infra_config.category IS 'Parameter group / category';
COMMENT ON COLUMN public.infra_config."type" IS 'Parameter type';
COMMENT ON COLUMN public.infra_config."name" IS 'Parameter name';
COMMENT ON COLUMN public.infra_config.config_key IS 'Parameter key';
COMMENT ON COLUMN public.infra_config.value IS 'Parameter value';
COMMENT ON COLUMN public.infra_config.visible IS 'Whether visible to end users';
COMMENT ON COLUMN public.infra_config.remark IS 'Remarks';
COMMENT ON COLUMN public.infra_config.creator IS 'Creator';
COMMENT ON COLUMN public.infra_config.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_config.updater IS 'Updater';
COMMENT ON COLUMN public.infra_config.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_config.deleted IS 'Soft-delete flag';


-- public.infra_data_source_config definition

-- Drop table

-- DROP TABLE infra_data_source_config;

CREATE TABLE infra_data_source_config (
                                          id int8 NOT NULL, -- Primary key ID
                                          "name" varchar(100) DEFAULT ''::character varying NOT NULL, -- Data source name
                                          url varchar(1024) NOT NULL, -- Data source connection URL
                                          username varchar(255) NOT NULL, -- Database username
                                          "password" varchar(255) DEFAULT ''::character varying NOT NULL, -- Database password
                                          creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                          create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                          updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                          update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                          deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                          CONSTRAINT pk_infra_data_source_config PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_data_source_config IS 'Data source configuration table';

-- Column comments

COMMENT ON COLUMN public.infra_data_source_config.id IS 'Primary key ID';
COMMENT ON COLUMN public.infra_data_source_config."name" IS 'Data source name';
COMMENT ON COLUMN public.infra_data_source_config.url IS 'Data source connection URL';
COMMENT ON COLUMN public.infra_data_source_config.username IS 'Database username';
COMMENT ON COLUMN public.infra_data_source_config."password" IS 'Database password';
COMMENT ON COLUMN public.infra_data_source_config.creator IS 'Creator';
COMMENT ON COLUMN public.infra_data_source_config.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_data_source_config.updater IS 'Updater';
COMMENT ON COLUMN public.infra_data_source_config.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_data_source_config.deleted IS 'Soft-delete flag';


-- public.infra_file definition

-- Drop table

-- DROP TABLE infra_file;

CREATE TABLE infra_file (
                            id int8 NOT NULL, -- File ID
                            config_id int8 NULL, -- File config ID
                            "name" varchar(256) DEFAULT NULL::character varying NULL, -- File name
                            "path" varchar(512) NOT NULL, -- File path
                            url varchar(1024) NOT NULL, -- File URL
                            "type" varchar(128) DEFAULT NULL::character varying NULL, -- File type (MIME)
                            "size" int4 NOT NULL, -- File size (bytes)
                            creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                            create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                            updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                            update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                            deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                            CONSTRAINT pk_infra_file PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_file IS 'File table';

-- Column comments

COMMENT ON COLUMN public.infra_file.id IS 'File ID';
COMMENT ON COLUMN public.infra_file.config_id IS 'File config ID';
COMMENT ON COLUMN public.infra_file."name" IS 'File name';
COMMENT ON COLUMN public.infra_file."path" IS 'File path';
COMMENT ON COLUMN public.infra_file.url IS 'File URL';
COMMENT ON COLUMN public.infra_file."type" IS 'File type (MIME)';
COMMENT ON COLUMN public.infra_file."size" IS 'File size (bytes)';
COMMENT ON COLUMN public.infra_file.creator IS 'Creator';
COMMENT ON COLUMN public.infra_file.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_file.updater IS 'Updater';
COMMENT ON COLUMN public.infra_file.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_file.deleted IS 'Soft-delete flag';


-- public.infra_file_config definition

-- Drop table

-- DROP TABLE infra_file_config;

CREATE TABLE infra_file_config (
                                   id int8 NOT NULL, -- Config ID
                                   "name" varchar(63) NOT NULL, -- Config name
                                   "storage" int2 NOT NULL, -- Storage type / engine
                                   remark varchar(255) DEFAULT NULL::character varying NULL, -- Remarks
                                   master bool NOT NULL, -- Whether this is the primary/master config
                                   config varchar(4096) NOT NULL, -- Storage config JSON
                                   creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                   create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                   updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                   update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                   deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                   CONSTRAINT pk_infra_file_config PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_file_config IS 'File storage configuration table';

-- Column comments

COMMENT ON COLUMN public.infra_file_config.id IS 'Config ID';
COMMENT ON COLUMN public.infra_file_config."name" IS 'Config name';
COMMENT ON COLUMN public.infra_file_config."storage" IS 'Storage type / engine';
COMMENT ON COLUMN public.infra_file_config.remark IS 'Remarks';
COMMENT ON COLUMN public.infra_file_config.master IS 'Whether this is the primary/master config';
COMMENT ON COLUMN public.infra_file_config.config IS 'Storage config JSON';
COMMENT ON COLUMN public.infra_file_config.creator IS 'Creator';
COMMENT ON COLUMN public.infra_file_config.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_file_config.updater IS 'Updater';
COMMENT ON COLUMN public.infra_file_config.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_file_config.deleted IS 'Soft-delete flag';


-- public.infra_file_content definition

-- Drop table

-- DROP TABLE infra_file_content;

CREATE TABLE infra_file_content (
                                    id int8 NOT NULL, -- Record ID
                                    config_id int8 NOT NULL, -- File config ID
                                    "path" varchar(512) NOT NULL, -- File path
                                    "content" bytea NOT NULL, -- File binary content
                                    creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                    create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                    updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                    update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                    deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                    CONSTRAINT pk_infra_file_content PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_file_content IS 'File content storage table';

-- Column comments

COMMENT ON COLUMN public.infra_file_content.id IS 'Record ID';
COMMENT ON COLUMN public.infra_file_content.config_id IS 'File config ID';
COMMENT ON COLUMN public.infra_file_content."path" IS 'File path';
COMMENT ON COLUMN public.infra_file_content."content" IS 'File binary content';
COMMENT ON COLUMN public.infra_file_content.creator IS 'Creator';
COMMENT ON COLUMN public.infra_file_content.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_file_content.updater IS 'Updater';
COMMENT ON COLUMN public.infra_file_content.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_file_content.deleted IS 'Soft-delete flag';


-- public.infra_job definition

-- Drop table

-- DROP TABLE infra_job;

CREATE TABLE infra_job (
                           id int8 NOT NULL, -- Job ID
                           "name" varchar(32) NOT NULL, -- Job name
                           status int2 NOT NULL, -- Job status
                           handler_name varchar(64) NOT NULL, -- Handler bean name
                           handler_param varchar(255) DEFAULT NULL::character varying NULL, -- Handler parameter
                           cron_expression varchar(32) NOT NULL, -- CRON expression
                           retry_count int4 DEFAULT 0 NOT NULL, -- Retry count
                           retry_interval int4 DEFAULT 0 NOT NULL, -- Retry interval (ms)
                           monitor_timeout int4 DEFAULT 0 NOT NULL, -- Monitor timeout (ms)
                           creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                           create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                           updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                           update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                           deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                           CONSTRAINT pk_infra_job PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_job IS 'Scheduled job table';

-- Column comments

COMMENT ON COLUMN public.infra_job.id IS 'Job ID';
COMMENT ON COLUMN public.infra_job."name" IS 'Job name';
COMMENT ON COLUMN public.infra_job.status IS 'Job status';
COMMENT ON COLUMN public.infra_job.handler_name IS 'Handler bean name';
COMMENT ON COLUMN public.infra_job.handler_param IS 'Handler parameter';
COMMENT ON COLUMN public.infra_job.cron_expression IS 'CRON expression';
COMMENT ON COLUMN public.infra_job.retry_count IS 'Retry count';
COMMENT ON COLUMN public.infra_job.retry_interval IS 'Retry interval (ms)';
COMMENT ON COLUMN public.infra_job.monitor_timeout IS 'Monitor timeout (ms)';
COMMENT ON COLUMN public.infra_job.creator IS 'Creator';
COMMENT ON COLUMN public.infra_job.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_job.updater IS 'Updater';
COMMENT ON COLUMN public.infra_job.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_job.deleted IS 'Soft-delete flag';


-- public.infra_job_log definition

-- Drop table

-- DROP TABLE infra_job_log;

CREATE TABLE infra_job_log (
                               id int8 NOT NULL, -- Log ID
                               job_id int8 NOT NULL, -- Job ID
                               handler_name varchar(64) NOT NULL, -- Handler bean name
                               handler_param varchar(255) DEFAULT NULL::character varying NULL, -- Handler parameter
                               execute_index int2 DEFAULT 1 NOT NULL, -- Execution attempt number
                               begin_time timestamp NOT NULL, -- Execution start time
                               end_time timestamp NULL, -- Execution end time
                               duration int4 NULL, -- Execution duration (ms)
                               status int2 NOT NULL, -- Execution status
                               "result" varchar(4000) DEFAULT ''::character varying NULL, -- Result data
                               creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                               create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                               updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                               update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                               deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                               CONSTRAINT pk_infra_job_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.infra_job_log IS 'Scheduled job execution log table';

-- Column comments

COMMENT ON COLUMN public.infra_job_log.id IS 'Log ID';
COMMENT ON COLUMN public.infra_job_log.job_id IS 'Job ID';
COMMENT ON COLUMN public.infra_job_log.handler_name IS 'Handler bean name';
COMMENT ON COLUMN public.infra_job_log.handler_param IS 'Handler parameter';
COMMENT ON COLUMN public.infra_job_log.execute_index IS 'Execution attempt number';
COMMENT ON COLUMN public.infra_job_log.begin_time IS 'Execution start time';
COMMENT ON COLUMN public.infra_job_log.end_time IS 'Execution end time';
COMMENT ON COLUMN public.infra_job_log.duration IS 'Execution duration (ms)';
COMMENT ON COLUMN public.infra_job_log.status IS 'Execution status';
COMMENT ON COLUMN public.infra_job_log."result" IS 'Result data';
COMMENT ON COLUMN public.infra_job_log.creator IS 'Creator';
COMMENT ON COLUMN public.infra_job_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.infra_job_log.updater IS 'Updater';
COMMENT ON COLUMN public.infra_job_log.update_time IS 'Update time';
COMMENT ON COLUMN public.infra_job_log.deleted IS 'Soft-delete flag';


-- public.qrtz_calendars definition

-- Drop table

-- DROP TABLE qrtz_calendars;

CREATE TABLE qrtz_calendars (
                                sched_name varchar(120) NOT NULL,
                                calendar_name varchar(200) NOT NULL,
                                calendar bytea NOT NULL,
                                CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name)
);


-- public.qrtz_fired_triggers definition

-- Drop table

-- DROP TABLE qrtz_fired_triggers;

CREATE TABLE qrtz_fired_triggers (
                                     sched_name varchar(120) NOT NULL,
                                     entry_id varchar(95) NOT NULL,
                                     trigger_name varchar(200) NOT NULL,
                                     trigger_group varchar(200) NOT NULL,
                                     instance_name varchar(200) NOT NULL,
                                     fired_time int8 NOT NULL,
                                     sched_time int8 NOT NULL,
                                     priority int4 NOT NULL,
                                     state varchar(16) NOT NULL,
                                     job_name varchar(200) NULL,
                                     job_group varchar(200) NULL,
                                     is_nonconcurrent bool NULL,
                                     requests_recovery bool NULL,
                                     CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id)
);
CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON public.qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);
CREATE INDEX idx_qrtz_ft_j_g ON public.qrtz_fired_triggers USING btree (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_ft_jg ON public.qrtz_fired_triggers USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_ft_t_g ON public.qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);
CREATE INDEX idx_qrtz_ft_tg ON public.qrtz_fired_triggers USING btree (sched_name, trigger_group);
CREATE INDEX idx_qrtz_ft_trig_inst_name ON public.qrtz_fired_triggers USING btree (sched_name, instance_name);


-- public.qrtz_job_details definition

-- Drop table

-- DROP TABLE qrtz_job_details;

CREATE TABLE qrtz_job_details (
                                  sched_name varchar(120) NOT NULL,
                                  job_name varchar(200) NOT NULL,
                                  job_group varchar(200) NOT NULL,
                                  description varchar(250) NULL,
                                  job_class_name varchar(250) NOT NULL,
                                  is_durable bool NOT NULL,
                                  is_nonconcurrent bool NOT NULL,
                                  is_update_data bool NOT NULL,
                                  requests_recovery bool NOT NULL,
                                  job_data bytea NULL,
                                  CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group)
);
CREATE INDEX idx_qrtz_j_grp ON public.qrtz_job_details USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_j_req_recovery ON public.qrtz_job_details USING btree (sched_name, requests_recovery);


-- public.qrtz_locks definition

-- Drop table

-- DROP TABLE qrtz_locks;

CREATE TABLE qrtz_locks (
                            sched_name varchar(120) NOT NULL,
                            lock_name varchar(40) NOT NULL,
                            CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name)
);


-- public.qrtz_paused_trigger_grps definition

-- Drop table

-- DROP TABLE qrtz_paused_trigger_grps;

CREATE TABLE qrtz_paused_trigger_grps (
                                          sched_name varchar(120) NOT NULL,
                                          trigger_group varchar(200) NOT NULL,
                                          CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group)
);


-- public.qrtz_scheduler_state definition

-- Drop table

-- DROP TABLE qrtz_scheduler_state;

CREATE TABLE qrtz_scheduler_state (
                                      sched_name varchar(120) NOT NULL,
                                      instance_name varchar(200) NOT NULL,
                                      last_checkin_time int8 NOT NULL,
                                      checkin_interval int8 NOT NULL,
                                      CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name)
);


-- public.sais_crop definition

-- Drop table

-- DROP TABLE sais_crop;

CREATE TABLE sais_crop (
                           id bigserial NOT NULL,
                           crop_name varchar(100) NOT NULL,
                           crop_type int4 NOT NULL,
                           variety varchar(100) NULL,
                           growth_period int4 NULL,
                           soil_ph_min numeric(4, 2) NULL,
                           soil_ph_max numeric(4, 2) NULL,
                           irrigation_method int4 NULL,
                           drought_resistance int2 NULL,
                           waterlogging_tolerance int2 NULL,
                           image_url varchar(500) NULL,
                           creator varchar(64) DEFAULT ''::character varying NULL,
                           create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           updater varchar(64) DEFAULT ''::character varying NULL,
                           update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           deleted int2 DEFAULT 0 NOT NULL,
                           CONSTRAINT sar_crop_check CHECK (((soil_ph_min IS NULL) OR (soil_ph_max IS NULL) OR (soil_ph_min <= soil_ph_max))),
                           CONSTRAINT sar_crop_crop_name_variety_key UNIQUE (crop_name, variety),
                           CONSTRAINT sar_crop_drought_resistance_check CHECK (((drought_resistance >= 1) AND (drought_resistance <= 5))),
                           CONSTRAINT sar_crop_pkey PRIMARY KEY (id),
                           CONSTRAINT sar_crop_waterlogging_tolerance_check CHECK (((waterlogging_tolerance >= 1) AND (waterlogging_tolerance <= 5)))
);


-- public.sais_decision_evaluation definition

-- Drop table

-- DROP TABLE sais_decision_evaluation;

CREATE TABLE sais_decision_evaluation (
                                          id bigserial NOT NULL,
                                          field_id int8 NULL,
                                          field_name varchar(100) NULL,
                                          crop_name varchar(100) NULL,
                                          stage_name varchar(100) NULL,
                                          current_moisture numeric(10, 2) NULL,
                                          moisture_min numeric(10, 2) NULL,
                                          moisture_optimal numeric(10, 2) NULL,
                                          tomorrow_rainfall numeric(10, 2) NULL,
                                          rule_decision varchar(32) NULL,
                                          rule_reason text NULL,
                                          ai_decision varchar(32) NULL,
                                          ai_reason text NULL,
                                          ai_duration_minutes int4 NULL,
                                          aligned bool NULL,
                                          ai_available bool NULL,
                                          evaluated_at timestamp NOT NULL,
                                          tenant_id int8 DEFAULT 0 NOT NULL,
                                          creator varchar(64) NULL,
                                          create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          updater varchar(64) NULL,
                                          update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          deleted int2 DEFAULT 0 NOT NULL,
                                          rule_duration_minutes int4 NULL,
                                          CONSTRAINT sais_decision_evaluation_pkey PRIMARY KEY (id)
);
CREATE INDEX idx_sais_decision_evaluation_evaluated_at ON public.sais_decision_evaluation USING btree (evaluated_at DESC);
CREATE INDEX idx_sais_decision_evaluation_field_id ON public.sais_decision_evaluation USING btree (field_id);


-- public.sais_farm definition

-- Drop table

-- DROP TABLE sais_farm;

CREATE TABLE sais_farm (
                           id bigserial NOT NULL,
                           farm_name varchar(100) NULL,
                           longitude numeric(11, 7) NOT NULL,
                           latitude numeric(10, 7) NOT NULL,
                           address varchar(255) NULL,
                           creator varchar(64) DEFAULT ''::character varying NULL,
                           create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           updater varchar(64) DEFAULT ''::character varying NULL,
                           update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           deleted int2 DEFAULT 0 NOT NULL,
                           tenant_id int8 DEFAULT 0 NOT NULL,
                           CONSTRAINT sar_farm_pkey PRIMARY KEY (id)
);


-- public.system_dept definition

-- Drop table

-- DROP TABLE system_dept;

CREATE TABLE system_dept (
                             id int8 NOT NULL, -- Department ID
                             "name" varchar(30) DEFAULT ''::character varying NOT NULL, -- Department name
                             parent_id int8 DEFAULT 0 NOT NULL, -- Parent department ID
                             sort int4 DEFAULT 0 NOT NULL, -- Display order
                             leader_user_id int8 NULL, -- Department leader user ID
                             phone varchar(11) DEFAULT NULL::character varying NULL, -- Contact phone number
                             email varchar(50) DEFAULT NULL::character varying NULL, -- Email address
                             status int2 NOT NULL, -- Department status (0=Normal, 1=Disabled)
                             creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                             updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                             deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                             tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                             CONSTRAINT pk_system_dept PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_dept IS 'Department table';

-- Column comments

COMMENT ON COLUMN public.system_dept.id IS 'Department ID';
COMMENT ON COLUMN public.system_dept."name" IS 'Department name';
COMMENT ON COLUMN public.system_dept.parent_id IS 'Parent department ID';
COMMENT ON COLUMN public.system_dept.sort IS 'Display order';
COMMENT ON COLUMN public.system_dept.leader_user_id IS 'Department leader user ID';
COMMENT ON COLUMN public.system_dept.phone IS 'Contact phone number';
COMMENT ON COLUMN public.system_dept.email IS 'Email address';
COMMENT ON COLUMN public.system_dept.status IS 'Department status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_dept.creator IS 'Creator';
COMMENT ON COLUMN public.system_dept.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_dept.updater IS 'Updater';
COMMENT ON COLUMN public.system_dept.update_time IS 'Update time';
COMMENT ON COLUMN public.system_dept.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_dept.tenant_id IS 'Tenant ID';


-- public.system_dict_data definition

-- Drop table

-- DROP TABLE system_dict_data;

CREATE TABLE system_dict_data (
                                  id int8 NOT NULL, -- Dictionary entry ID
                                  sort int4 DEFAULT 0 NOT NULL, -- Sort order
                                  "label" varchar(100) DEFAULT ''::character varying NOT NULL, -- Display label
                                  value varchar(100) DEFAULT ''::character varying NOT NULL, -- Entry value
                                  dict_type varchar(100) DEFAULT ''::character varying NOT NULL, -- Dictionary type
                                  status int2 DEFAULT 0 NOT NULL, -- Status (0=Normal, 1=Disabled)
                                  color_type varchar(100) DEFAULT ''::character varying NULL, -- Color type / tag style
                                  css_class varchar(100) DEFAULT ''::character varying NULL, -- CSS class name
                                  remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  CONSTRAINT pk_system_dict_data PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_dict_data IS 'Dictionary data table';

-- Column comments

COMMENT ON COLUMN public.system_dict_data.id IS 'Dictionary entry ID';
COMMENT ON COLUMN public.system_dict_data.sort IS 'Sort order';
COMMENT ON COLUMN public.system_dict_data."label" IS 'Display label';
COMMENT ON COLUMN public.system_dict_data.value IS 'Entry value';
COMMENT ON COLUMN public.system_dict_data.dict_type IS 'Dictionary type';
COMMENT ON COLUMN public.system_dict_data.status IS 'Status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_dict_data.color_type IS 'Color type / tag style';
COMMENT ON COLUMN public.system_dict_data.css_class IS 'CSS class name';
COMMENT ON COLUMN public.system_dict_data.remark IS 'Remarks';
COMMENT ON COLUMN public.system_dict_data.creator IS 'Creator';
COMMENT ON COLUMN public.system_dict_data.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_dict_data.updater IS 'Updater';
COMMENT ON COLUMN public.system_dict_data.update_time IS 'Update time';
COMMENT ON COLUMN public.system_dict_data.deleted IS 'Soft-delete flag';


-- public.system_dict_type definition

-- Drop table

-- DROP TABLE system_dict_type;

CREATE TABLE system_dict_type (
                                  id int8 NOT NULL, -- Dictionary primary key
                                  "name" varchar(100) DEFAULT ''::character varying NOT NULL, -- Dictionary name
                                  "type" varchar(100) DEFAULT ''::character varying NOT NULL, -- Dictionary type code
                                  status int2 DEFAULT 0 NOT NULL, -- Status (0=Normal, 1=Disabled)
                                  remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  deleted_time timestamp NULL, -- Deletion timestamp
                                  CONSTRAINT pk_system_dict_type PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_dict_type IS 'Dictionary type table';

-- Column comments

COMMENT ON COLUMN public.system_dict_type.id IS 'Dictionary primary key';
COMMENT ON COLUMN public.system_dict_type."name" IS 'Dictionary name';
COMMENT ON COLUMN public.system_dict_type."type" IS 'Dictionary type code';
COMMENT ON COLUMN public.system_dict_type.status IS 'Status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_dict_type.remark IS 'Remarks';
COMMENT ON COLUMN public.system_dict_type.creator IS 'Creator';
COMMENT ON COLUMN public.system_dict_type.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_dict_type.updater IS 'Updater';
COMMENT ON COLUMN public.system_dict_type.update_time IS 'Update time';
COMMENT ON COLUMN public.system_dict_type.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_dict_type.deleted_time IS 'Deletion timestamp';


-- public.system_login_log definition

-- Drop table

-- DROP TABLE system_login_log;

CREATE TABLE system_login_log (
                                  id int8 NOT NULL, -- Access log ID
                                  log_type int8 NOT NULL, -- Log type
                                  trace_id varchar(64) DEFAULT ''::character varying NOT NULL, -- Trace tracking ID
                                  user_id int8 DEFAULT 0 NOT NULL, -- User ID
                                  user_type int2 DEFAULT 0 NOT NULL, -- User type
                                  username varchar(50) DEFAULT ''::character varying NOT NULL, -- Username / account
                                  "result" int2 NOT NULL, -- Login result
                                  user_ip varchar(50) NOT NULL, -- User IP address
                                  user_agent varchar(512) NOT NULL, -- Browser user agent (UA)
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                  CONSTRAINT pk_system_login_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_login_log IS 'System login / access log table';

-- Column comments

COMMENT ON COLUMN public.system_login_log.id IS 'Access log ID';
COMMENT ON COLUMN public.system_login_log.log_type IS 'Log type';
COMMENT ON COLUMN public.system_login_log.trace_id IS 'Trace tracking ID';
COMMENT ON COLUMN public.system_login_log.user_id IS 'User ID';
COMMENT ON COLUMN public.system_login_log.user_type IS 'User type';
COMMENT ON COLUMN public.system_login_log.username IS 'Username / account';
COMMENT ON COLUMN public.system_login_log."result" IS 'Login result';
COMMENT ON COLUMN public.system_login_log.user_ip IS 'User IP address';
COMMENT ON COLUMN public.system_login_log.user_agent IS 'Browser user agent (UA)';
COMMENT ON COLUMN public.system_login_log.creator IS 'Creator';
COMMENT ON COLUMN public.system_login_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_login_log.updater IS 'Updater';
COMMENT ON COLUMN public.system_login_log.update_time IS 'Update time';
COMMENT ON COLUMN public.system_login_log.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_login_log.tenant_id IS 'Tenant ID';


-- public.system_mail_account definition

-- Drop table

-- DROP TABLE system_mail_account;

CREATE TABLE system_mail_account (
                                     id int8 NOT NULL, -- Primary key
                                     mail varchar(255) NOT NULL, -- Email address
                                     username varchar(255) NOT NULL, -- Username
                                     "password" varchar(255) NOT NULL, -- Password
                                     host varchar(255) NOT NULL, -- SMTP server hostname
                                     port int4 NOT NULL, -- SMTP server port
                                     ssl_enable bool DEFAULT false NOT NULL, -- Whether SSL is enabled
                                     starttls_enable bool DEFAULT false NOT NULL, -- Whether STARTTLS is enabled
                                     creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                     create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                     updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                     update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                     deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                     CONSTRAINT pk_system_mail_account PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_mail_account IS 'Mail account table';

-- Column comments

COMMENT ON COLUMN public.system_mail_account.id IS 'Primary key';
COMMENT ON COLUMN public.system_mail_account.mail IS 'Email address';
COMMENT ON COLUMN public.system_mail_account.username IS 'Username';
COMMENT ON COLUMN public.system_mail_account."password" IS 'Password';
COMMENT ON COLUMN public.system_mail_account.host IS 'SMTP server hostname';
COMMENT ON COLUMN public.system_mail_account.port IS 'SMTP server port';
COMMENT ON COLUMN public.system_mail_account.ssl_enable IS 'Whether SSL is enabled';
COMMENT ON COLUMN public.system_mail_account.starttls_enable IS 'Whether STARTTLS is enabled';
COMMENT ON COLUMN public.system_mail_account.creator IS 'Creator';
COMMENT ON COLUMN public.system_mail_account.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_mail_account.updater IS 'Updater';
COMMENT ON COLUMN public.system_mail_account.update_time IS 'Update time';
COMMENT ON COLUMN public.system_mail_account.deleted IS 'Soft-delete flag';


-- public.system_mail_log definition

-- Drop table

-- DROP TABLE system_mail_log;

CREATE TABLE system_mail_log (
                                 id int8 NOT NULL, -- Record ID
                                 user_id int8 NULL, -- User ID
                                 user_type int2 NULL, -- User type
                                 to_mails varchar(1024) NOT NULL, -- Recipient email address(es)
                                 cc_mails varchar(1024) DEFAULT NULL::character varying NULL, -- CC email address(es)
                                 bcc_mails varchar(1024) DEFAULT NULL::character varying NULL, -- BCC email address(es)
                                 account_id int8 NOT NULL, -- Sending mail account ID
                                 from_mail varchar(255) NOT NULL, -- Sender email address
                                 template_id int8 NOT NULL, -- Mail template ID
                                 template_code varchar(63) NOT NULL, -- Mail template code
                                 template_nickname varchar(255) DEFAULT NULL::character varying NULL, -- Sender display name from template
                                 template_title varchar(255) NOT NULL, -- Mail subject
                                 template_content text NOT NULL, -- Mail body content
                                 template_params varchar(255) NOT NULL, -- Mail template parameters
                                 send_status int2 DEFAULT 0 NOT NULL, -- Send status
                                 send_time timestamp NULL, -- Send time
                                 send_message_id varchar(255) DEFAULT NULL::character varying NULL, -- Message ID returned by mail server
                                 send_exception varchar(4096) DEFAULT NULL::character varying NULL, -- Send exception details
                                 creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                 create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                 updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                 update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                 deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                 CONSTRAINT pk_system_mail_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_mail_log IS 'Mail send log table';

-- Column comments

COMMENT ON COLUMN public.system_mail_log.id IS 'Record ID';
COMMENT ON COLUMN public.system_mail_log.user_id IS 'User ID';
COMMENT ON COLUMN public.system_mail_log.user_type IS 'User type';
COMMENT ON COLUMN public.system_mail_log.to_mails IS 'Recipient email address(es)';
COMMENT ON COLUMN public.system_mail_log.cc_mails IS 'CC email address(es)';
COMMENT ON COLUMN public.system_mail_log.bcc_mails IS 'BCC email address(es)';
COMMENT ON COLUMN public.system_mail_log.account_id IS 'Sending mail account ID';
COMMENT ON COLUMN public.system_mail_log.from_mail IS 'Sender email address';
COMMENT ON COLUMN public.system_mail_log.template_id IS 'Mail template ID';
COMMENT ON COLUMN public.system_mail_log.template_code IS 'Mail template code';
COMMENT ON COLUMN public.system_mail_log.template_nickname IS 'Sender display name from template';
COMMENT ON COLUMN public.system_mail_log.template_title IS 'Mail subject';
COMMENT ON COLUMN public.system_mail_log.template_content IS 'Mail body content';
COMMENT ON COLUMN public.system_mail_log.template_params IS 'Mail template parameters';
COMMENT ON COLUMN public.system_mail_log.send_status IS 'Send status';
COMMENT ON COLUMN public.system_mail_log.send_time IS 'Send time';
COMMENT ON COLUMN public.system_mail_log.send_message_id IS 'Message ID returned by mail server';
COMMENT ON COLUMN public.system_mail_log.send_exception IS 'Send exception details';
COMMENT ON COLUMN public.system_mail_log.creator IS 'Creator';
COMMENT ON COLUMN public.system_mail_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_mail_log.updater IS 'Updater';
COMMENT ON COLUMN public.system_mail_log.update_time IS 'Update time';
COMMENT ON COLUMN public.system_mail_log.deleted IS 'Soft-delete flag';


-- public.system_mail_template definition

-- Drop table

-- DROP TABLE system_mail_template;

CREATE TABLE system_mail_template (
                                      id int8 NOT NULL, -- Record ID
                                      "name" varchar(63) NOT NULL, -- Template name
                                      code varchar(63) NOT NULL, -- Template code
                                      account_id int8 NOT NULL, -- Sending mail account ID
                                      nickname varchar(255) DEFAULT NULL::character varying NULL, -- Sender display name
                                      title varchar(255) NOT NULL, -- Mail subject template
                                      "content" varchar(10240) NOT NULL, -- Mail body template
                                      params varchar(255) NOT NULL, -- Template parameter array
                                      status int2 NOT NULL, -- Enabled status
                                      remark varchar(255) DEFAULT NULL::character varying NULL, -- Remarks
                                      creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                      updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                      deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                      CONSTRAINT pk_system_mail_template PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_mail_template IS 'Mail template table';

-- Column comments

COMMENT ON COLUMN public.system_mail_template.id IS 'Record ID';
COMMENT ON COLUMN public.system_mail_template."name" IS 'Template name';
COMMENT ON COLUMN public.system_mail_template.code IS 'Template code';
COMMENT ON COLUMN public.system_mail_template.account_id IS 'Sending mail account ID';
COMMENT ON COLUMN public.system_mail_template.nickname IS 'Sender display name';
COMMENT ON COLUMN public.system_mail_template.title IS 'Mail subject template';
COMMENT ON COLUMN public.system_mail_template."content" IS 'Mail body template';
COMMENT ON COLUMN public.system_mail_template.params IS 'Template parameter array';
COMMENT ON COLUMN public.system_mail_template.status IS 'Enabled status';
COMMENT ON COLUMN public.system_mail_template.remark IS 'Remarks';
COMMENT ON COLUMN public.system_mail_template.creator IS 'Creator';
COMMENT ON COLUMN public.system_mail_template.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_mail_template.updater IS 'Updater';
COMMENT ON COLUMN public.system_mail_template.update_time IS 'Update time';
COMMENT ON COLUMN public.system_mail_template.deleted IS 'Soft-delete flag';


-- public.system_menu definition

-- Drop table

-- DROP TABLE system_menu;

CREATE TABLE system_menu (
                             id int8 NOT NULL, -- Menu ID
                             "name" varchar(50) NOT NULL, -- Menu name
                             "permission" varchar(100) DEFAULT ''::character varying NOT NULL, -- Permission identifier
                             "type" int2 NOT NULL, -- Menu type
                             sort int4 DEFAULT 0 NOT NULL, -- Display order
                             parent_id int8 DEFAULT 0 NOT NULL, -- Parent menu ID
                             "path" varchar(200) DEFAULT ''::character varying NULL, -- Route path
                             icon varchar(100) DEFAULT '#'::character varying NULL, -- Menu icon
                             component varchar(255) DEFAULT NULL::character varying NULL, -- Front-end component path
                             component_name varchar(255) DEFAULT NULL::character varying NULL, -- Front-end component name
                             status int2 DEFAULT 0 NOT NULL, -- Menu status
                             visible bool DEFAULT true NOT NULL, -- Whether visible in navigation
                             keep_alive bool DEFAULT true NOT NULL, -- Whether to cache the component
                             always_show bool DEFAULT true NOT NULL, -- Whether to always show in menu
                             creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                             updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                             deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                             CONSTRAINT pk_system_menu PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_menu IS 'Menu and permission table';

-- Column comments

COMMENT ON COLUMN public.system_menu.id IS 'Menu ID';
COMMENT ON COLUMN public.system_menu."name" IS 'Menu name';
COMMENT ON COLUMN public.system_menu."permission" IS 'Permission identifier';
COMMENT ON COLUMN public.system_menu."type" IS 'Menu type';
COMMENT ON COLUMN public.system_menu.sort IS 'Display order';
COMMENT ON COLUMN public.system_menu.parent_id IS 'Parent menu ID';
COMMENT ON COLUMN public.system_menu."path" IS 'Route path';
COMMENT ON COLUMN public.system_menu.icon IS 'Menu icon';
COMMENT ON COLUMN public.system_menu.component IS 'Front-end component path';
COMMENT ON COLUMN public.system_menu.component_name IS 'Front-end component name';
COMMENT ON COLUMN public.system_menu.status IS 'Menu status';
COMMENT ON COLUMN public.system_menu.visible IS 'Whether visible in navigation';
COMMENT ON COLUMN public.system_menu.keep_alive IS 'Whether to cache the component';
COMMENT ON COLUMN public.system_menu.always_show IS 'Whether to always show in menu';
COMMENT ON COLUMN public.system_menu.creator IS 'Creator';
COMMENT ON COLUMN public.system_menu.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_menu.updater IS 'Updater';
COMMENT ON COLUMN public.system_menu.update_time IS 'Update time';
COMMENT ON COLUMN public.system_menu.deleted IS 'Soft-delete flag';


-- public.system_notice definition

-- Drop table

-- DROP TABLE system_notice;

CREATE TABLE system_notice (
                               id int8 NOT NULL, -- Notice ID
                               title varchar(50) NOT NULL, -- Notice title
                               "content" text NOT NULL, -- Notice content
                               "type" int2 NOT NULL, -- Notice type (1=Notification, 2=Announcement)
                               status int2 DEFAULT 0 NOT NULL, -- Notice status (0=Normal, 1=Closed)
                               creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                               create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                               updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                               update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                               deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                               tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                               CONSTRAINT pk_system_notice PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_notice IS 'System notice / announcement table';

-- Column comments

COMMENT ON COLUMN public.system_notice.id IS 'Notice ID';
COMMENT ON COLUMN public.system_notice.title IS 'Notice title';
COMMENT ON COLUMN public.system_notice."content" IS 'Notice content';
COMMENT ON COLUMN public.system_notice."type" IS 'Notice type (1=Notification, 2=Announcement)';
COMMENT ON COLUMN public.system_notice.status IS 'Notice status (0=Normal, 1=Closed)';
COMMENT ON COLUMN public.system_notice.creator IS 'Creator';
COMMENT ON COLUMN public.system_notice.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_notice.updater IS 'Updater';
COMMENT ON COLUMN public.system_notice.update_time IS 'Update time';
COMMENT ON COLUMN public.system_notice.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_notice.tenant_id IS 'Tenant ID';


-- public.system_notify_message definition

-- Drop table

-- DROP TABLE system_notify_message;

CREATE TABLE system_notify_message (
                                       id int8 NOT NULL, -- Message record ID
                                       user_id int8 NOT NULL, -- Recipient user ID
                                       user_type int2 NOT NULL, -- User type
                                       template_id int8 NOT NULL, -- Notification template ID
                                       template_code varchar(64) NOT NULL, -- Notification template code
                                       template_nickname varchar(63) NOT NULL, -- Sender display name from template
                                       template_content varchar(1024) NOT NULL, -- Rendered notification content
                                       template_type int4 NOT NULL, -- Template type
                                       template_params varchar(255) NOT NULL, -- Template parameters
                                       read_status bool NOT NULL, -- Whether the message has been read
                                       read_time timestamp NULL, -- Time the message was read
                                       creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                       create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                       updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                       update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                       deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                       tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                       CONSTRAINT pk_system_notify_message PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_notify_message IS 'In-app notification message table';

-- Column comments

COMMENT ON COLUMN public.system_notify_message.id IS 'Message record ID';
COMMENT ON COLUMN public.system_notify_message.user_id IS 'Recipient user ID';
COMMENT ON COLUMN public.system_notify_message.user_type IS 'User type';
COMMENT ON COLUMN public.system_notify_message.template_id IS 'Notification template ID';
COMMENT ON COLUMN public.system_notify_message.template_code IS 'Notification template code';
COMMENT ON COLUMN public.system_notify_message.template_nickname IS 'Sender display name from template';
COMMENT ON COLUMN public.system_notify_message.template_content IS 'Rendered notification content';
COMMENT ON COLUMN public.system_notify_message.template_type IS 'Template type';
COMMENT ON COLUMN public.system_notify_message.template_params IS 'Template parameters';
COMMENT ON COLUMN public.system_notify_message.read_status IS 'Whether the message has been read';
COMMENT ON COLUMN public.system_notify_message.read_time IS 'Time the message was read';
COMMENT ON COLUMN public.system_notify_message.creator IS 'Creator';
COMMENT ON COLUMN public.system_notify_message.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_notify_message.updater IS 'Updater';
COMMENT ON COLUMN public.system_notify_message.update_time IS 'Update time';
COMMENT ON COLUMN public.system_notify_message.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_notify_message.tenant_id IS 'Tenant ID';


-- public.system_notify_template definition

-- Drop table

-- DROP TABLE system_notify_template;

CREATE TABLE system_notify_template (
                                        id int8 NOT NULL, -- Primary key
                                        "name" varchar(63) NOT NULL, -- Template name
                                        code varchar(64) NOT NULL, -- Template code
                                        nickname varchar(255) NOT NULL, -- Sender display name
                                        "content" varchar(1024) NOT NULL, -- Template content
                                        "type" int2 NOT NULL, -- Template type
                                        params varchar(255) DEFAULT NULL::character varying NULL, -- Parameter array
                                        status int2 NOT NULL, -- Status
                                        remark varchar(255) DEFAULT NULL::character varying NULL, -- Remarks
                                        creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                        create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                        updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                        update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                        deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                        CONSTRAINT pk_system_notify_template PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_notify_template IS 'In-app notification template table';

-- Column comments

COMMENT ON COLUMN public.system_notify_template.id IS 'Primary key';
COMMENT ON COLUMN public.system_notify_template."name" IS 'Template name';
COMMENT ON COLUMN public.system_notify_template.code IS 'Template code';
COMMENT ON COLUMN public.system_notify_template.nickname IS 'Sender display name';
COMMENT ON COLUMN public.system_notify_template."content" IS 'Template content';
COMMENT ON COLUMN public.system_notify_template."type" IS 'Template type';
COMMENT ON COLUMN public.system_notify_template.params IS 'Parameter array';
COMMENT ON COLUMN public.system_notify_template.status IS 'Status';
COMMENT ON COLUMN public.system_notify_template.remark IS 'Remarks';
COMMENT ON COLUMN public.system_notify_template.creator IS 'Creator';
COMMENT ON COLUMN public.system_notify_template.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_notify_template.updater IS 'Updater';
COMMENT ON COLUMN public.system_notify_template.update_time IS 'Update time';
COMMENT ON COLUMN public.system_notify_template.deleted IS 'Soft-delete flag';


-- public.system_oauth2_access_token definition

-- Drop table

-- DROP TABLE system_oauth2_access_token;

CREATE TABLE system_oauth2_access_token (
                                            id int8 NOT NULL, -- Record ID
                                            user_id int8 NOT NULL, -- User ID
                                            user_type int2 NOT NULL, -- User type
                                            user_info varchar(512) NOT NULL, -- User info snapshot
                                            access_token varchar(255) NOT NULL, -- Access token value
                                            refresh_token varchar(32) NOT NULL, -- Refresh token value
                                            client_id varchar(255) NOT NULL, -- OAuth2 client ID
                                            scopes varchar(255) DEFAULT NULL::character varying NULL, -- Authorized scopes
                                            expires_time timestamp NOT NULL, -- Expiry time
                                            creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                            create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                            updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                            update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                            deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                            tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                            CONSTRAINT pk_system_oauth2_access_token PRIMARY KEY (id)
);
CREATE INDEX idx_system_oauth2_access_token_01 ON public.system_oauth2_access_token USING btree (access_token);
CREATE INDEX idx_system_oauth2_access_token_02 ON public.system_oauth2_access_token USING btree (refresh_token);
COMMENT ON TABLE public.system_oauth2_access_token IS 'OAuth2 access token table';

-- Column comments

COMMENT ON COLUMN public.system_oauth2_access_token.id IS 'Record ID';
COMMENT ON COLUMN public.system_oauth2_access_token.user_id IS 'User ID';
COMMENT ON COLUMN public.system_oauth2_access_token.user_type IS 'User type';
COMMENT ON COLUMN public.system_oauth2_access_token.user_info IS 'User info snapshot';
COMMENT ON COLUMN public.system_oauth2_access_token.access_token IS 'Access token value';
COMMENT ON COLUMN public.system_oauth2_access_token.refresh_token IS 'Refresh token value';
COMMENT ON COLUMN public.system_oauth2_access_token.client_id IS 'OAuth2 client ID';
COMMENT ON COLUMN public.system_oauth2_access_token.scopes IS 'Authorized scopes';
COMMENT ON COLUMN public.system_oauth2_access_token.expires_time IS 'Expiry time';
COMMENT ON COLUMN public.system_oauth2_access_token.creator IS 'Creator';
COMMENT ON COLUMN public.system_oauth2_access_token.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_oauth2_access_token.updater IS 'Updater';
COMMENT ON COLUMN public.system_oauth2_access_token.update_time IS 'Update time';
COMMENT ON COLUMN public.system_oauth2_access_token.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_oauth2_access_token.tenant_id IS 'Tenant ID';


-- public.system_oauth2_approve definition

-- Drop table

-- DROP TABLE system_oauth2_approve;

CREATE TABLE system_oauth2_approve (
                                       id int8 NOT NULL, -- Record ID
                                       user_id int8 NOT NULL, -- User ID
                                       user_type int2 NOT NULL, -- User type
                                       client_id varchar(255) NOT NULL, -- OAuth2 client ID
                                       "scope" varchar(255) DEFAULT ''::character varying NOT NULL, -- Authorized scope
                                       approved bool DEFAULT false NOT NULL, -- Whether approved
                                       expires_time timestamp NOT NULL, -- Expiry time
                                       creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                       create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                       updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                       update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                       deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                       tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                       CONSTRAINT pk_system_oauth2_approve PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_oauth2_approve IS 'OAuth2 authorization approval table';

-- Column comments

COMMENT ON COLUMN public.system_oauth2_approve.id IS 'Record ID';
COMMENT ON COLUMN public.system_oauth2_approve.user_id IS 'User ID';
COMMENT ON COLUMN public.system_oauth2_approve.user_type IS 'User type';
COMMENT ON COLUMN public.system_oauth2_approve.client_id IS 'OAuth2 client ID';
COMMENT ON COLUMN public.system_oauth2_approve."scope" IS 'Authorized scope';
COMMENT ON COLUMN public.system_oauth2_approve.approved IS 'Whether approved';
COMMENT ON COLUMN public.system_oauth2_approve.expires_time IS 'Expiry time';
COMMENT ON COLUMN public.system_oauth2_approve.creator IS 'Creator';
COMMENT ON COLUMN public.system_oauth2_approve.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_oauth2_approve.updater IS 'Updater';
COMMENT ON COLUMN public.system_oauth2_approve.update_time IS 'Update time';
COMMENT ON COLUMN public.system_oauth2_approve.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_oauth2_approve.tenant_id IS 'Tenant ID';


-- public.system_oauth2_client definition

-- Drop table

-- DROP TABLE system_oauth2_client;

CREATE TABLE system_oauth2_client (
                                      id int8 NOT NULL, -- Record ID
                                      client_id varchar(255) NOT NULL, -- Client ID
                                      secret varchar(255) NOT NULL, -- Client secret
                                      "name" varchar(255) NOT NULL, -- Application name
                                      logo varchar(255) NOT NULL, -- Application logo URL
                                      description varchar(255) DEFAULT NULL::character varying NULL, -- Application description
                                      status int2 NOT NULL, -- Status
                                      access_token_validity_seconds int4 NOT NULL, -- Access token validity period (seconds)
                                      refresh_token_validity_seconds int4 NOT NULL, -- Refresh token validity period (seconds)
                                      redirect_uris varchar(255) NOT NULL, -- Allowed redirect URIs
                                      authorized_grant_types varchar(255) NOT NULL, -- Authorized grant types
                                      scopes varchar(255) DEFAULT NULL::character varying NULL, -- Authorized scopes
                                      auto_approve_scopes varchar(255) DEFAULT NULL::character varying NULL, -- Auto-approved scopes
                                      authorities varchar(255) DEFAULT NULL::character varying NULL, -- Granted authorities / permissions
                                      resource_ids varchar(255) DEFAULT NULL::character varying NULL, -- Accessible resource IDs
                                      additional_information varchar(4096) DEFAULT NULL::character varying NULL, -- Additional information (JSON)
                                      creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                      updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                      deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                      CONSTRAINT pk_system_oauth2_client PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_oauth2_client IS 'OAuth2 client application table';

-- Column comments

COMMENT ON COLUMN public.system_oauth2_client.id IS 'Record ID';
COMMENT ON COLUMN public.system_oauth2_client.client_id IS 'Client ID';
COMMENT ON COLUMN public.system_oauth2_client.secret IS 'Client secret';
COMMENT ON COLUMN public.system_oauth2_client."name" IS 'Application name';
COMMENT ON COLUMN public.system_oauth2_client.logo IS 'Application logo URL';
COMMENT ON COLUMN public.system_oauth2_client.description IS 'Application description';
COMMENT ON COLUMN public.system_oauth2_client.status IS 'Status';
COMMENT ON COLUMN public.system_oauth2_client.access_token_validity_seconds IS 'Access token validity period (seconds)';
COMMENT ON COLUMN public.system_oauth2_client.refresh_token_validity_seconds IS 'Refresh token validity period (seconds)';
COMMENT ON COLUMN public.system_oauth2_client.redirect_uris IS 'Allowed redirect URIs';
COMMENT ON COLUMN public.system_oauth2_client.authorized_grant_types IS 'Authorized grant types';
COMMENT ON COLUMN public.system_oauth2_client.scopes IS 'Authorized scopes';
COMMENT ON COLUMN public.system_oauth2_client.auto_approve_scopes IS 'Auto-approved scopes';
COMMENT ON COLUMN public.system_oauth2_client.authorities IS 'Granted authorities / permissions';
COMMENT ON COLUMN public.system_oauth2_client.resource_ids IS 'Accessible resource IDs';
COMMENT ON COLUMN public.system_oauth2_client.additional_information IS 'Additional information (JSON)';
COMMENT ON COLUMN public.system_oauth2_client.creator IS 'Creator';
COMMENT ON COLUMN public.system_oauth2_client.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_oauth2_client.updater IS 'Updater';
COMMENT ON COLUMN public.system_oauth2_client.update_time IS 'Update time';
COMMENT ON COLUMN public.system_oauth2_client.deleted IS 'Soft-delete flag';


-- public.system_oauth2_code definition

-- Drop table

-- DROP TABLE system_oauth2_code;

CREATE TABLE system_oauth2_code (
                                    id int8 NOT NULL, -- Record ID
                                    user_id int8 NOT NULL, -- User ID
                                    user_type int2 NOT NULL, -- User type
                                    code varchar(32) NOT NULL, -- Authorization code
                                    client_id varchar(255) NOT NULL, -- OAuth2 client ID
                                    scopes varchar(255) DEFAULT ''::character varying NULL, -- Authorized scopes
                                    expires_time timestamp NOT NULL, -- Expiry time
                                    redirect_uri varchar(255) DEFAULT NULL::character varying NULL, -- Redirect URI
                                    state varchar(255) DEFAULT ''::character varying NOT NULL, -- State parameter
                                    creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                    create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                    updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                    update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                    deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                    tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                    CONSTRAINT pk_system_oauth2_code PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_oauth2_code IS 'OAuth2 authorization code table';

-- Column comments

COMMENT ON COLUMN public.system_oauth2_code.id IS 'Record ID';
COMMENT ON COLUMN public.system_oauth2_code.user_id IS 'User ID';
COMMENT ON COLUMN public.system_oauth2_code.user_type IS 'User type';
COMMENT ON COLUMN public.system_oauth2_code.code IS 'Authorization code';
COMMENT ON COLUMN public.system_oauth2_code.client_id IS 'OAuth2 client ID';
COMMENT ON COLUMN public.system_oauth2_code.scopes IS 'Authorized scopes';
COMMENT ON COLUMN public.system_oauth2_code.expires_time IS 'Expiry time';
COMMENT ON COLUMN public.system_oauth2_code.redirect_uri IS 'Redirect URI';
COMMENT ON COLUMN public.system_oauth2_code.state IS 'State parameter';
COMMENT ON COLUMN public.system_oauth2_code.creator IS 'Creator';
COMMENT ON COLUMN public.system_oauth2_code.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_oauth2_code.updater IS 'Updater';
COMMENT ON COLUMN public.system_oauth2_code.update_time IS 'Update time';
COMMENT ON COLUMN public.system_oauth2_code.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_oauth2_code.tenant_id IS 'Tenant ID';


-- public.system_oauth2_refresh_token definition

-- Drop table

-- DROP TABLE system_oauth2_refresh_token;

CREATE TABLE system_oauth2_refresh_token (
                                             id int8 NOT NULL, -- Record ID
                                             user_id int8 NOT NULL, -- User ID
                                             refresh_token varchar(32) NOT NULL, -- Refresh token value
                                             user_type int2 NOT NULL, -- User type
                                             client_id varchar(255) NOT NULL, -- OAuth2 client ID
                                             scopes varchar(255) DEFAULT NULL::character varying NULL, -- Authorized scopes
                                             expires_time timestamp NOT NULL, -- Expiry time
                                             creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                             updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                             deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                             tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                             CONSTRAINT pk_system_oauth2_refresh_token PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_oauth2_refresh_token IS 'OAuth2 refresh token table';

-- Column comments

COMMENT ON COLUMN public.system_oauth2_refresh_token.id IS 'Record ID';
COMMENT ON COLUMN public.system_oauth2_refresh_token.user_id IS 'User ID';
COMMENT ON COLUMN public.system_oauth2_refresh_token.refresh_token IS 'Refresh token value';
COMMENT ON COLUMN public.system_oauth2_refresh_token.user_type IS 'User type';
COMMENT ON COLUMN public.system_oauth2_refresh_token.client_id IS 'OAuth2 client ID';
COMMENT ON COLUMN public.system_oauth2_refresh_token.scopes IS 'Authorized scopes';
COMMENT ON COLUMN public.system_oauth2_refresh_token.expires_time IS 'Expiry time';
COMMENT ON COLUMN public.system_oauth2_refresh_token.creator IS 'Creator';
COMMENT ON COLUMN public.system_oauth2_refresh_token.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_oauth2_refresh_token.updater IS 'Updater';
COMMENT ON COLUMN public.system_oauth2_refresh_token.update_time IS 'Update time';
COMMENT ON COLUMN public.system_oauth2_refresh_token.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_oauth2_refresh_token.tenant_id IS 'Tenant ID';


-- public.system_operate_log definition

-- Drop table

-- DROP TABLE system_operate_log;

CREATE TABLE system_operate_log (
                                    id int8 NOT NULL, -- Log primary key
                                    trace_id varchar(64) DEFAULT ''::character varying NOT NULL, -- Trace tracking ID
                                    user_id int8 NOT NULL, -- User ID
                                    user_type int2 DEFAULT 0 NOT NULL, -- User type
                                    "type" varchar(50) NOT NULL, -- Operation module type
                                    sub_type varchar(50) NOT NULL, -- Operation name / sub-type
                                    biz_id int8 NOT NULL, -- Business record ID that was operated on
                                    "action" varchar(2000) DEFAULT ''::character varying NOT NULL, -- Operation content / description
                                    success bool DEFAULT true NOT NULL, -- Whether the operation succeeded
                                    extra varchar(2000) DEFAULT ''::character varying NOT NULL, -- Extended fields (JSON)
                                    request_method varchar(16) DEFAULT ''::character varying NULL, -- HTTP request method
                                    request_url varchar(255) DEFAULT ''::character varying NULL, -- Request URL
                                    user_ip varchar(50) DEFAULT NULL::character varying NULL, -- User IP address
                                    user_agent varchar(512) DEFAULT NULL::character varying NULL, -- Browser user agent (UA)
                                    creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                    create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                    updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                    update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                    deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                    tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                    CONSTRAINT pk_system_operate_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_operate_log IS 'Operation audit log table (v2)';

-- Column comments

COMMENT ON COLUMN public.system_operate_log.id IS 'Log primary key';
COMMENT ON COLUMN public.system_operate_log.trace_id IS 'Trace tracking ID';
COMMENT ON COLUMN public.system_operate_log.user_id IS 'User ID';
COMMENT ON COLUMN public.system_operate_log.user_type IS 'User type';
COMMENT ON COLUMN public.system_operate_log."type" IS 'Operation module type';
COMMENT ON COLUMN public.system_operate_log.sub_type IS 'Operation name / sub-type';
COMMENT ON COLUMN public.system_operate_log.biz_id IS 'Business record ID that was operated on';
COMMENT ON COLUMN public.system_operate_log."action" IS 'Operation content / description';
COMMENT ON COLUMN public.system_operate_log.success IS 'Whether the operation succeeded';
COMMENT ON COLUMN public.system_operate_log.extra IS 'Extended fields (JSON)';
COMMENT ON COLUMN public.system_operate_log.request_method IS 'HTTP request method';
COMMENT ON COLUMN public.system_operate_log.request_url IS 'Request URL';
COMMENT ON COLUMN public.system_operate_log.user_ip IS 'User IP address';
COMMENT ON COLUMN public.system_operate_log.user_agent IS 'Browser user agent (UA)';
COMMENT ON COLUMN public.system_operate_log.creator IS 'Creator';
COMMENT ON COLUMN public.system_operate_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_operate_log.updater IS 'Updater';
COMMENT ON COLUMN public.system_operate_log.update_time IS 'Update time';
COMMENT ON COLUMN public.system_operate_log.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_operate_log.tenant_id IS 'Tenant ID';


-- public.system_post definition

-- Drop table

-- DROP TABLE system_post;

CREATE TABLE system_post (
                             id int8 NOT NULL, -- Post ID
                             code varchar(64) NOT NULL, -- Post code
                             "name" varchar(50) NOT NULL, -- Post name
                             sort int4 NOT NULL, -- Display order
                             status int2 NOT NULL, -- Status (0=Normal, 1=Disabled)
                             remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                             creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                             updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                             deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                             tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                             CONSTRAINT pk_system_post PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_post IS 'Position / job post table';

-- Column comments

COMMENT ON COLUMN public.system_post.id IS 'Post ID';
COMMENT ON COLUMN public.system_post.code IS 'Post code';
COMMENT ON COLUMN public.system_post."name" IS 'Post name';
COMMENT ON COLUMN public.system_post.sort IS 'Display order';
COMMENT ON COLUMN public.system_post.status IS 'Status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_post.remark IS 'Remarks';
COMMENT ON COLUMN public.system_post.creator IS 'Creator';
COMMENT ON COLUMN public.system_post.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_post.updater IS 'Updater';
COMMENT ON COLUMN public.system_post.update_time IS 'Update time';
COMMENT ON COLUMN public.system_post.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_post.tenant_id IS 'Tenant ID';


-- public.system_role definition

-- Drop table

-- DROP TABLE system_role;

CREATE TABLE system_role (
                             id int8 NOT NULL, -- Role ID
                             "name" varchar(30) NOT NULL, -- Role name
                             code varchar(100) NOT NULL, -- Role permission code string
                             sort int4 NOT NULL, -- Display order
                             data_scope int2 DEFAULT 1 NOT NULL, -- Data scope (1=All, 2=Custom, 3=Own Dept, 4=Own Dept and below)
                             data_scope_dept_ids varchar(500) DEFAULT ''::character varying NOT NULL, -- Custom data scope department IDs array
                             status int2 NOT NULL, -- Role status (0=Normal, 1=Disabled)
                             "type" int2 NOT NULL, -- Role type
                             remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                             creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                             updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                             deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                             tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                             CONSTRAINT pk_system_role PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_role IS 'Role information table';

-- Column comments

COMMENT ON COLUMN public.system_role.id IS 'Role ID';
COMMENT ON COLUMN public.system_role."name" IS 'Role name';
COMMENT ON COLUMN public.system_role.code IS 'Role permission code string';
COMMENT ON COLUMN public.system_role.sort IS 'Display order';
COMMENT ON COLUMN public.system_role.data_scope IS 'Data scope (1=All, 2=Custom, 3=Own Dept, 4=Own Dept and below)';
COMMENT ON COLUMN public.system_role.data_scope_dept_ids IS 'Custom data scope department IDs array';
COMMENT ON COLUMN public.system_role.status IS 'Role status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_role."type" IS 'Role type';
COMMENT ON COLUMN public.system_role.remark IS 'Remarks';
COMMENT ON COLUMN public.system_role.creator IS 'Creator';
COMMENT ON COLUMN public.system_role.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_role.updater IS 'Updater';
COMMENT ON COLUMN public.system_role.update_time IS 'Update time';
COMMENT ON COLUMN public.system_role.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_role.tenant_id IS 'Tenant ID';


-- public.system_role_menu definition

-- Drop table

-- DROP TABLE system_role_menu;

CREATE TABLE system_role_menu (
                                  id int8 NOT NULL, -- Auto-increment record ID
                                  role_id int8 NOT NULL, -- Role ID
                                  menu_id int8 NOT NULL, -- Menu ID
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                  CONSTRAINT pk_system_role_menu PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_role_menu IS 'Role-menu association table';

-- Column comments

COMMENT ON COLUMN public.system_role_menu.id IS 'Auto-increment record ID';
COMMENT ON COLUMN public.system_role_menu.role_id IS 'Role ID';
COMMENT ON COLUMN public.system_role_menu.menu_id IS 'Menu ID';
COMMENT ON COLUMN public.system_role_menu.creator IS 'Creator';
COMMENT ON COLUMN public.system_role_menu.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_role_menu.updater IS 'Updater';
COMMENT ON COLUMN public.system_role_menu.update_time IS 'Update time';
COMMENT ON COLUMN public.system_role_menu.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_role_menu.tenant_id IS 'Tenant ID';


-- public.system_sms_channel definition

-- Drop table

-- DROP TABLE system_sms_channel;

CREATE TABLE system_sms_channel (
                                    id int8 NOT NULL, -- Channel ID
                                    signature varchar(12) NOT NULL, -- SMS signature
                                    code varchar(63) NOT NULL, -- Channel code
                                    status int2 NOT NULL, -- Enabled status
                                    remark varchar(255) DEFAULT NULL::character varying NULL, -- Remarks
                                    api_key varchar(128) NOT NULL, -- SMS API account key
                                    api_secret varchar(128) DEFAULT NULL::character varying NULL, -- SMS API secret key
                                    callback_url varchar(255) DEFAULT NULL::character varying NULL, -- SMS delivery callback URL
                                    creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                    create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                    updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                    update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                    deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                    CONSTRAINT pk_system_sms_channel PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_sms_channel IS 'SMS channel table';

-- Column comments

COMMENT ON COLUMN public.system_sms_channel.id IS 'Channel ID';
COMMENT ON COLUMN public.system_sms_channel.signature IS 'SMS signature';
COMMENT ON COLUMN public.system_sms_channel.code IS 'Channel code';
COMMENT ON COLUMN public.system_sms_channel.status IS 'Enabled status';
COMMENT ON COLUMN public.system_sms_channel.remark IS 'Remarks';
COMMENT ON COLUMN public.system_sms_channel.api_key IS 'SMS API account key';
COMMENT ON COLUMN public.system_sms_channel.api_secret IS 'SMS API secret key';
COMMENT ON COLUMN public.system_sms_channel.callback_url IS 'SMS delivery callback URL';
COMMENT ON COLUMN public.system_sms_channel.creator IS 'Creator';
COMMENT ON COLUMN public.system_sms_channel.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_sms_channel.updater IS 'Updater';
COMMENT ON COLUMN public.system_sms_channel.update_time IS 'Update time';
COMMENT ON COLUMN public.system_sms_channel.deleted IS 'Soft-delete flag';


-- public.system_sms_code definition

-- Drop table

-- DROP TABLE system_sms_code;

CREATE TABLE system_sms_code (
                                 id int8 NOT NULL, -- Record ID
                                 mobile varchar(11) NOT NULL, -- Mobile phone number
                                 code varchar(6) NOT NULL, -- Verification code
                                 create_ip varchar(15) NOT NULL, -- Creation IP address
                                 scene int2 NOT NULL, -- Send scene / use case
                                 today_index int2 NOT NULL, -- Sequence number of send for today
                                 used int2 NOT NULL, -- Whether the code has been used
                                 used_time timestamp NULL, -- Time the code was used
                                 used_ip varchar(255) DEFAULT NULL::character varying NULL, -- IP address where code was used
                                 creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                 create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                 updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                 update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                 deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                 tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                 CONSTRAINT pk_system_sms_code PRIMARY KEY (id)
);
CREATE INDEX idx_system_sms_code_01 ON public.system_sms_code USING btree (mobile);
COMMENT ON TABLE public.system_sms_code IS 'Mobile verification code table';

-- Column comments

COMMENT ON COLUMN public.system_sms_code.id IS 'Record ID';
COMMENT ON COLUMN public.system_sms_code.mobile IS 'Mobile phone number';
COMMENT ON COLUMN public.system_sms_code.code IS 'Verification code';
COMMENT ON COLUMN public.system_sms_code.create_ip IS 'Creation IP address';
COMMENT ON COLUMN public.system_sms_code.scene IS 'Send scene / use case';
COMMENT ON COLUMN public.system_sms_code.today_index IS 'Sequence number of send for today';
COMMENT ON COLUMN public.system_sms_code.used IS 'Whether the code has been used';
COMMENT ON COLUMN public.system_sms_code.used_time IS 'Time the code was used';
COMMENT ON COLUMN public.system_sms_code.used_ip IS 'IP address where code was used';
COMMENT ON COLUMN public.system_sms_code.creator IS 'Creator';
COMMENT ON COLUMN public.system_sms_code.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_sms_code.updater IS 'Updater';
COMMENT ON COLUMN public.system_sms_code.update_time IS 'Update time';
COMMENT ON COLUMN public.system_sms_code.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_sms_code.tenant_id IS 'Tenant ID';


-- public.system_sms_log definition

-- Drop table

-- DROP TABLE system_sms_log;

CREATE TABLE system_sms_log (
                                id int8 NOT NULL, -- Record ID
                                channel_id int8 NOT NULL, -- SMS channel ID
                                channel_code varchar(63) NOT NULL, -- SMS channel code
                                template_id int8 NOT NULL, -- SMS template ID
                                template_code varchar(63) NOT NULL, -- SMS template code
                                template_type int2 NOT NULL, -- SMS message type
                                template_content varchar(255) NOT NULL, -- SMS message content
                                template_params varchar(255) NOT NULL, -- SMS template parameters
                                api_template_id varchar(63) NOT NULL, -- SMS API provider template ID
                                mobile varchar(11) NOT NULL, -- Recipient mobile number
                                user_id int8 NULL, -- User ID
                                user_type int2 NULL, -- User type
                                send_status int2 DEFAULT 0 NOT NULL, -- Send status
                                send_time timestamp NULL, -- Send time
                                api_send_code varchar(63) DEFAULT NULL::character varying NULL, -- API send result code
                                api_send_msg varchar(255) DEFAULT NULL::character varying NULL, -- API send failure message
                                api_request_id varchar(255) DEFAULT NULL::character varying NULL, -- Unique request ID returned by SMS API
                                api_serial_no varchar(255) DEFAULT NULL::character varying NULL, -- Serial number returned by SMS API
                                receive_status int2 DEFAULT 0 NOT NULL, -- Delivery receipt status
                                receive_time timestamp NULL, -- Delivery receipt time
                                api_receive_code varchar(63) DEFAULT NULL::character varying NULL, -- API delivery receipt result code
                                api_receive_msg varchar(255) DEFAULT NULL::character varying NULL, -- API delivery receipt result message
                                creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                CONSTRAINT pk_system_sms_log PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_sms_log IS 'SMS send log table';

-- Column comments

COMMENT ON COLUMN public.system_sms_log.id IS 'Record ID';
COMMENT ON COLUMN public.system_sms_log.channel_id IS 'SMS channel ID';
COMMENT ON COLUMN public.system_sms_log.channel_code IS 'SMS channel code';
COMMENT ON COLUMN public.system_sms_log.template_id IS 'SMS template ID';
COMMENT ON COLUMN public.system_sms_log.template_code IS 'SMS template code';
COMMENT ON COLUMN public.system_sms_log.template_type IS 'SMS message type';
COMMENT ON COLUMN public.system_sms_log.template_content IS 'SMS message content';
COMMENT ON COLUMN public.system_sms_log.template_params IS 'SMS template parameters';
COMMENT ON COLUMN public.system_sms_log.api_template_id IS 'SMS API provider template ID';
COMMENT ON COLUMN public.system_sms_log.mobile IS 'Recipient mobile number';
COMMENT ON COLUMN public.system_sms_log.user_id IS 'User ID';
COMMENT ON COLUMN public.system_sms_log.user_type IS 'User type';
COMMENT ON COLUMN public.system_sms_log.send_status IS 'Send status';
COMMENT ON COLUMN public.system_sms_log.send_time IS 'Send time';
COMMENT ON COLUMN public.system_sms_log.api_send_code IS 'API send result code';
COMMENT ON COLUMN public.system_sms_log.api_send_msg IS 'API send failure message';
COMMENT ON COLUMN public.system_sms_log.api_request_id IS 'Unique request ID returned by SMS API';
COMMENT ON COLUMN public.system_sms_log.api_serial_no IS 'Serial number returned by SMS API';
COMMENT ON COLUMN public.system_sms_log.receive_status IS 'Delivery receipt status';
COMMENT ON COLUMN public.system_sms_log.receive_time IS 'Delivery receipt time';
COMMENT ON COLUMN public.system_sms_log.api_receive_code IS 'API delivery receipt result code';
COMMENT ON COLUMN public.system_sms_log.api_receive_msg IS 'API delivery receipt result message';
COMMENT ON COLUMN public.system_sms_log.creator IS 'Creator';
COMMENT ON COLUMN public.system_sms_log.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_sms_log.updater IS 'Updater';
COMMENT ON COLUMN public.system_sms_log.update_time IS 'Update time';
COMMENT ON COLUMN public.system_sms_log.deleted IS 'Soft-delete flag';


-- public.system_sms_template definition

-- Drop table

-- DROP TABLE system_sms_template;

CREATE TABLE system_sms_template (
                                     id int8 NOT NULL, -- Record ID
                                     "type" int2 NOT NULL, -- Template type
                                     status int2 NOT NULL, -- Enabled status
                                     code varchar(63) NOT NULL, -- Template code
                                     "name" varchar(63) NOT NULL, -- Template name
                                     "content" varchar(255) NOT NULL, -- Template content
                                     params varchar(255) NOT NULL, -- Parameter array
                                     remark varchar(255) DEFAULT NULL::character varying NULL, -- Remarks
                                     api_template_id varchar(63) NOT NULL, -- SMS API provider template ID
                                     channel_id int8 NOT NULL, -- SMS channel ID
                                     channel_code varchar(63) NOT NULL, -- SMS channel code
                                     creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                     create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                     updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                     update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                     deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                     CONSTRAINT pk_system_sms_template PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_sms_template IS 'SMS template table';

-- Column comments

COMMENT ON COLUMN public.system_sms_template.id IS 'Record ID';
COMMENT ON COLUMN public.system_sms_template."type" IS 'Template type';
COMMENT ON COLUMN public.system_sms_template.status IS 'Enabled status';
COMMENT ON COLUMN public.system_sms_template.code IS 'Template code';
COMMENT ON COLUMN public.system_sms_template."name" IS 'Template name';
COMMENT ON COLUMN public.system_sms_template."content" IS 'Template content';
COMMENT ON COLUMN public.system_sms_template.params IS 'Parameter array';
COMMENT ON COLUMN public.system_sms_template.remark IS 'Remarks';
COMMENT ON COLUMN public.system_sms_template.api_template_id IS 'SMS API provider template ID';
COMMENT ON COLUMN public.system_sms_template.channel_id IS 'SMS channel ID';
COMMENT ON COLUMN public.system_sms_template.channel_code IS 'SMS channel code';
COMMENT ON COLUMN public.system_sms_template.creator IS 'Creator';
COMMENT ON COLUMN public.system_sms_template.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_sms_template.updater IS 'Updater';
COMMENT ON COLUMN public.system_sms_template.update_time IS 'Update time';
COMMENT ON COLUMN public.system_sms_template.deleted IS 'Soft-delete flag';


-- public.system_social_client definition

-- Drop table

-- DROP TABLE system_social_client;

CREATE TABLE system_social_client (
                                      id int8 NOT NULL, -- Record ID
                                      "name" varchar(255) NOT NULL, -- Application name
                                      social_type int2 NOT NULL, -- Social platform type
                                      user_type int2 NOT NULL, -- User type
                                      client_id varchar(255) NOT NULL, -- OAuth2 client ID
                                      client_secret varchar(2048) NOT NULL, -- OAuth2 client secret
                                      public_key varchar(2048) NULL, -- Public key
                                      agent_id varchar(255) DEFAULT NULL::character varying NULL, -- Agent / corp ID
                                      status int2 NOT NULL, -- Status
                                      creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                      updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                      deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                      tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                      CONSTRAINT pk_system_social_client PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_social_client IS 'Social login client application table';

-- Column comments

COMMENT ON COLUMN public.system_social_client.id IS 'Record ID';
COMMENT ON COLUMN public.system_social_client."name" IS 'Application name';
COMMENT ON COLUMN public.system_social_client.social_type IS 'Social platform type';
COMMENT ON COLUMN public.system_social_client.user_type IS 'User type';
COMMENT ON COLUMN public.system_social_client.client_id IS 'OAuth2 client ID';
COMMENT ON COLUMN public.system_social_client.client_secret IS 'OAuth2 client secret';
COMMENT ON COLUMN public.system_social_client.public_key IS 'Public key';
COMMENT ON COLUMN public.system_social_client.agent_id IS 'Agent / corp ID';
COMMENT ON COLUMN public.system_social_client.status IS 'Status';
COMMENT ON COLUMN public.system_social_client.creator IS 'Creator';
COMMENT ON COLUMN public.system_social_client.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_social_client.updater IS 'Updater';
COMMENT ON COLUMN public.system_social_client.update_time IS 'Update time';
COMMENT ON COLUMN public.system_social_client.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_social_client.tenant_id IS 'Tenant ID';


-- public.system_social_user definition

-- Drop table

-- DROP TABLE system_social_user;

CREATE TABLE system_social_user (
                                    id int8 NOT NULL, -- Primary key (auto-increment)
                                    "type" int2 NOT NULL, -- Social platform type
                                    openid varchar(32) NOT NULL, -- Social platform openid
                                    "token" varchar(256) DEFAULT NULL::character varying NULL, -- Social platform token
                                    raw_token_info varchar(1024) NOT NULL, -- Raw token data (usually JSON)
                                    nickname varchar(32) NOT NULL, -- User nickname
                                    avatar varchar(255) DEFAULT NULL::character varying NULL, -- User avatar URL
                                    raw_user_info varchar(1024) NOT NULL, -- Raw user data from platform (usually JSON)
                                    code varchar(256) NOT NULL, -- Most recent authorization code
                                    state varchar(256) DEFAULT NULL::character varying NULL, -- Most recent authorization state
                                    creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                    create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                    updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                    update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                    deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                    tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                    CONSTRAINT pk_system_social_user PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_social_user IS 'Social login user table';

-- Column comments

COMMENT ON COLUMN public.system_social_user.id IS 'Primary key (auto-increment)';
COMMENT ON COLUMN public.system_social_user."type" IS 'Social platform type';
COMMENT ON COLUMN public.system_social_user.openid IS 'Social platform openid';
COMMENT ON COLUMN public.system_social_user."token" IS 'Social platform token';
COMMENT ON COLUMN public.system_social_user.raw_token_info IS 'Raw token data (usually JSON)';
COMMENT ON COLUMN public.system_social_user.nickname IS 'User nickname';
COMMENT ON COLUMN public.system_social_user.avatar IS 'User avatar URL';
COMMENT ON COLUMN public.system_social_user.raw_user_info IS 'Raw user data from platform (usually JSON)';
COMMENT ON COLUMN public.system_social_user.code IS 'Most recent authorization code';
COMMENT ON COLUMN public.system_social_user.state IS 'Most recent authorization state';
COMMENT ON COLUMN public.system_social_user.creator IS 'Creator';
COMMENT ON COLUMN public.system_social_user.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_social_user.updater IS 'Updater';
COMMENT ON COLUMN public.system_social_user.update_time IS 'Update time';
COMMENT ON COLUMN public.system_social_user.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_social_user.tenant_id IS 'Tenant ID';


-- public.system_social_user_bind definition

-- Drop table

-- DROP TABLE system_social_user_bind;

CREATE TABLE system_social_user_bind (
                                         id int8 NOT NULL, -- Primary key (auto-increment)
                                         user_id int8 NOT NULL, -- User ID
                                         user_type int2 NOT NULL, -- User type
                                         social_type int2 NOT NULL, -- Social platform type
                                         social_user_id int8 NOT NULL, -- Social user record ID
                                         creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                         create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                         updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                         update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                         deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                         tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                         CONSTRAINT pk_system_social_user_bind PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_social_user_bind IS 'Social login binding table';

-- Column comments

COMMENT ON COLUMN public.system_social_user_bind.id IS 'Primary key (auto-increment)';
COMMENT ON COLUMN public.system_social_user_bind.user_id IS 'User ID';
COMMENT ON COLUMN public.system_social_user_bind.user_type IS 'User type';
COMMENT ON COLUMN public.system_social_user_bind.social_type IS 'Social platform type';
COMMENT ON COLUMN public.system_social_user_bind.social_user_id IS 'Social user record ID';
COMMENT ON COLUMN public.system_social_user_bind.creator IS 'Creator';
COMMENT ON COLUMN public.system_social_user_bind.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_social_user_bind.updater IS 'Updater';
COMMENT ON COLUMN public.system_social_user_bind.update_time IS 'Update time';
COMMENT ON COLUMN public.system_social_user_bind.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_social_user_bind.tenant_id IS 'Tenant ID';


-- public.system_tenant definition

-- Drop table

-- DROP TABLE system_tenant;

CREATE TABLE system_tenant (
                               id int8 NOT NULL, -- Tenant ID
                               "name" varchar(30) NOT NULL, -- Tenant name
                               contact_user_id int8 NULL, -- Contact person user ID
                               contact_name varchar(30) NOT NULL, -- Contact person name
                               contact_mobile varchar(500) DEFAULT NULL::character varying NULL, -- Contact mobile number
                               status int2 DEFAULT 0 NOT NULL, -- Tenant status
                               websites varchar(1024) DEFAULT ''::character varying NULL, -- Bound domain names array
                               package_id int8 NOT NULL, -- Tenant subscription package ID
                               expire_time timestamp NOT NULL, -- Subscription expiry time
                               account_count int4 NOT NULL, -- Maximum number of user accounts
                               creator varchar(64) DEFAULT ''::character varying NOT NULL, -- Creator
                               create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                               updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                               update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                               deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                               CONSTRAINT pk_system_tenant PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_tenant IS 'Tenant table';

-- Column comments

COMMENT ON COLUMN public.system_tenant.id IS 'Tenant ID';
COMMENT ON COLUMN public.system_tenant."name" IS 'Tenant name';
COMMENT ON COLUMN public.system_tenant.contact_user_id IS 'Contact person user ID';
COMMENT ON COLUMN public.system_tenant.contact_name IS 'Contact person name';
COMMENT ON COLUMN public.system_tenant.contact_mobile IS 'Contact mobile number';
COMMENT ON COLUMN public.system_tenant.status IS 'Tenant status';
COMMENT ON COLUMN public.system_tenant.websites IS 'Bound domain names array';
COMMENT ON COLUMN public.system_tenant.package_id IS 'Tenant subscription package ID';
COMMENT ON COLUMN public.system_tenant.expire_time IS 'Subscription expiry time';
COMMENT ON COLUMN public.system_tenant.account_count IS 'Maximum number of user accounts';
COMMENT ON COLUMN public.system_tenant.creator IS 'Creator';
COMMENT ON COLUMN public.system_tenant.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_tenant.updater IS 'Updater';
COMMENT ON COLUMN public.system_tenant.update_time IS 'Update time';
COMMENT ON COLUMN public.system_tenant.deleted IS 'Soft-delete flag';


-- public.system_tenant_package definition

-- Drop table

-- DROP TABLE system_tenant_package;

CREATE TABLE system_tenant_package (
                                       id int8 NOT NULL, -- Package ID
                                       "name" varchar(30) NOT NULL, -- Package name
                                       status int2 DEFAULT 0 NOT NULL, -- Tenant status (0=Normal, 1=Disabled)
                                       remark varchar(256) DEFAULT ''::character varying NULL, -- Remarks
                                       menu_ids varchar(4096) NOT NULL, -- Included menu IDs
                                       creator varchar(64) DEFAULT ''::character varying NOT NULL, -- Creator
                                       create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                       updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                       update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                       deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                       CONSTRAINT pk_system_tenant_package PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_tenant_package IS 'Tenant subscription package table';

-- Column comments

COMMENT ON COLUMN public.system_tenant_package.id IS 'Package ID';
COMMENT ON COLUMN public.system_tenant_package."name" IS 'Package name';
COMMENT ON COLUMN public.system_tenant_package.status IS 'Tenant status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_tenant_package.remark IS 'Remarks';
COMMENT ON COLUMN public.system_tenant_package.menu_ids IS 'Included menu IDs';
COMMENT ON COLUMN public.system_tenant_package.creator IS 'Creator';
COMMENT ON COLUMN public.system_tenant_package.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_tenant_package.updater IS 'Updater';
COMMENT ON COLUMN public.system_tenant_package.update_time IS 'Update time';
COMMENT ON COLUMN public.system_tenant_package.deleted IS 'Soft-delete flag';


-- public.system_user_post definition

-- Drop table

-- DROP TABLE system_user_post;

CREATE TABLE system_user_post (
                                  id int8 NOT NULL, -- Record ID
                                  user_id int8 DEFAULT 0 NOT NULL, -- User ID
                                  post_id int8 DEFAULT 0 NOT NULL, -- Post / position ID
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                  CONSTRAINT pk_system_user_post PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_user_post IS 'User-post assignment table';

-- Column comments

COMMENT ON COLUMN public.system_user_post.id IS 'Record ID';
COMMENT ON COLUMN public.system_user_post.user_id IS 'User ID';
COMMENT ON COLUMN public.system_user_post.post_id IS 'Post / position ID';
COMMENT ON COLUMN public.system_user_post.creator IS 'Creator';
COMMENT ON COLUMN public.system_user_post.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_user_post.updater IS 'Updater';
COMMENT ON COLUMN public.system_user_post.update_time IS 'Update time';
COMMENT ON COLUMN public.system_user_post.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_user_post.tenant_id IS 'Tenant ID';


-- public.system_user_role definition

-- Drop table

-- DROP TABLE system_user_role;

CREATE TABLE system_user_role (
                                  id int8 NOT NULL, -- Auto-increment record ID
                                  user_id int8 NOT NULL, -- User ID
                                  role_id int8 NOT NULL, -- Role ID
                                  creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NULL, -- Creation time
                                  updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NULL, -- Update time
                                  deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                                  tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                                  CONSTRAINT pk_system_user_role PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_user_role IS 'User-role association table';

-- Column comments

COMMENT ON COLUMN public.system_user_role.id IS 'Auto-increment record ID';
COMMENT ON COLUMN public.system_user_role.user_id IS 'User ID';
COMMENT ON COLUMN public.system_user_role.role_id IS 'Role ID';
COMMENT ON COLUMN public.system_user_role.creator IS 'Creator';
COMMENT ON COLUMN public.system_user_role.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_user_role.updater IS 'Updater';
COMMENT ON COLUMN public.system_user_role.update_time IS 'Update time';
COMMENT ON COLUMN public.system_user_role.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_user_role.tenant_id IS 'Tenant ID';


-- public.system_users definition

-- Drop table

-- DROP TABLE system_users;

CREATE TABLE system_users (
                              id int8 NOT NULL, -- User ID
                              username varchar(30) NOT NULL, -- Username / account
                              "password" varchar(100) DEFAULT ''::character varying NOT NULL, -- Password (hashed)
                              nickname varchar(30) NOT NULL, -- Display nickname
                              remark varchar(500) DEFAULT NULL::character varying NULL, -- Remarks
                              dept_id int8 NULL, -- Department ID
                              post_ids varchar(255) DEFAULT NULL::character varying NULL, -- Post / position IDs array
                              email varchar(50) DEFAULT ''::character varying NULL, -- Email address
                              mobile varchar(11) DEFAULT ''::character varying NULL, -- Mobile phone number
                              sex int2 DEFAULT 0 NULL, -- Gender
                              avatar varchar(512) DEFAULT ''::character varying NULL, -- Avatar image URL
                              status int2 DEFAULT 0 NOT NULL, -- Account status (0=Normal, 1=Disabled)
                              login_ip varchar(50) DEFAULT ''::character varying NULL, -- Last login IP address
                              login_date timestamp NULL, -- Last login time
                              creator varchar(64) DEFAULT ''::character varying NULL, -- Creator
                              create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation time
                              updater varchar(64) DEFAULT ''::character varying NULL, -- Updater
                              update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Update time
                              deleted int2 DEFAULT 0 NOT NULL, -- Soft-delete flag
                              tenant_id int8 DEFAULT 0 NOT NULL, -- Tenant ID
                              CONSTRAINT pk_system_users PRIMARY KEY (id)
);
COMMENT ON TABLE public.system_users IS 'User information table';

-- Column comments

COMMENT ON COLUMN public.system_users.id IS 'User ID';
COMMENT ON COLUMN public.system_users.username IS 'Username / account';
COMMENT ON COLUMN public.system_users."password" IS 'Password (hashed)';
COMMENT ON COLUMN public.system_users.nickname IS 'Display nickname';
COMMENT ON COLUMN public.system_users.remark IS 'Remarks';
COMMENT ON COLUMN public.system_users.dept_id IS 'Department ID';
COMMENT ON COLUMN public.system_users.post_ids IS 'Post / position IDs array';
COMMENT ON COLUMN public.system_users.email IS 'Email address';
COMMENT ON COLUMN public.system_users.mobile IS 'Mobile phone number';
COMMENT ON COLUMN public.system_users.sex IS 'Gender';
COMMENT ON COLUMN public.system_users.avatar IS 'Avatar image URL';
COMMENT ON COLUMN public.system_users.status IS 'Account status (0=Normal, 1=Disabled)';
COMMENT ON COLUMN public.system_users.login_ip IS 'Last login IP address';
COMMENT ON COLUMN public.system_users.login_date IS 'Last login time';
COMMENT ON COLUMN public.system_users.creator IS 'Creator';
COMMENT ON COLUMN public.system_users.create_time IS 'Creation time';
COMMENT ON COLUMN public.system_users.updater IS 'Updater';
COMMENT ON COLUMN public.system_users.update_time IS 'Update time';
COMMENT ON COLUMN public.system_users.deleted IS 'Soft-delete flag';
COMMENT ON COLUMN public.system_users.tenant_id IS 'Tenant ID';


-- public.qrtz_triggers definition

-- Drop table

-- DROP TABLE qrtz_triggers;

CREATE TABLE qrtz_triggers (
                               sched_name varchar(120) NOT NULL,
                               trigger_name varchar(200) NOT NULL,
                               trigger_group varchar(200) NOT NULL,
                               job_name varchar(200) NOT NULL,
                               job_group varchar(200) NOT NULL,
                               description varchar(250) NULL,
                               next_fire_time int8 NULL,
                               prev_fire_time int8 NULL,
                               priority int4 NULL,
                               trigger_state varchar(16) NOT NULL,
                               trigger_type varchar(8) NOT NULL,
                               start_time int8 NOT NULL,
                               end_time int8 NULL,
                               calendar_name varchar(200) NULL,
                               misfire_instr int2 NULL,
                               job_data bytea NULL,
                               CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group),
                               CONSTRAINT qrtz_triggers_sched_name_job_name_job_group_fkey FOREIGN KEY (sched_name,job_name,job_group) REFERENCES qrtz_job_details(sched_name,job_name,job_group)
);
CREATE INDEX idx_qrtz_t_c ON public.qrtz_triggers USING btree (sched_name, calendar_name);
CREATE INDEX idx_qrtz_t_g ON public.qrtz_triggers USING btree (sched_name, trigger_group);
CREATE INDEX idx_qrtz_t_j ON public.qrtz_triggers USING btree (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_t_jg ON public.qrtz_triggers USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_t_n_g_state ON public.qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_n_state ON public.qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_next_fire_time ON public.qrtz_triggers USING btree (sched_name, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_misfire ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st ON public.qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st_misfire ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);
CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_state ON public.qrtz_triggers USING btree (sched_name, trigger_state);


-- public.sais_field definition

-- Drop table

-- DROP TABLE sais_field;

CREATE TABLE sais_field (
                            id bigserial NOT NULL,
                            farm_id int8 NOT NULL,
                            field_name varchar(100) NOT NULL,
                            area numeric(12, 2) NULL,
                            longitude numeric(11, 7) NULL,
                            latitude numeric(10, 7) NULL,
                            grow_status varchar(20) DEFAULT 'UNSTARTED'::character varying NOT NULL,
                            boundary text NULL,
                            creator varchar(64) DEFAULT ''::character varying NULL,
                            create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            updater varchar(64) DEFAULT ''::character varying NULL,
                            update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            deleted int2 DEFAULT 0 NOT NULL,
                            tenant_id int8 DEFAULT 0 NOT NULL,
                            CONSTRAINT sar_field_grow_status_check CHECK (((grow_status)::text = ANY (ARRAY[('UNSTARTED'::character varying)::text, ('ONGOING'::character varying)::text, ('FINISHED'::character varying)::text, ('FALLOW'::character varying)::text]))),
	CONSTRAINT sar_field_pkey PRIMARY KEY (id),
	CONSTRAINT sar_field_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE
);


-- public.sais_growth_stage definition

-- Drop table

-- DROP TABLE sais_growth_stage;

CREATE TABLE sais_growth_stage (
                                   id bigserial NOT NULL,
                                   crop_id int8 NOT NULL,
                                   stage_name varchar(100) NOT NULL,
                                   stage_order int4 NOT NULL,
                                   duration_days int4 NOT NULL,
                                   soil_moisture_min numeric(5, 2) NULL,
                                   soil_moisture_max numeric(5, 2) NULL,
                                   soil_moisture_optimal numeric(5, 2) NULL,
                                   creator varchar(64) DEFAULT ''::character varying NULL,
                                   create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   updater varchar(64) DEFAULT ''::character varying NULL,
                                   update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   deleted int2 DEFAULT 0 NOT NULL,
                                   CONSTRAINT sar_growth_stage_check CHECK (((soil_moisture_min IS NULL) OR (soil_moisture_max IS NULL) OR (soil_moisture_min <= soil_moisture_max))),
                                   CONSTRAINT sar_growth_stage_crop_id_stage_order_key UNIQUE (crop_id, stage_order),
                                   CONSTRAINT sar_growth_stage_duration_days_check CHECK ((duration_days > 0)),
                                   CONSTRAINT sar_growth_stage_pkey PRIMARY KEY (id),
                                   CONSTRAINT sar_growth_stage_crop_id_fkey FOREIGN KEY (crop_id) REFERENCES sais_crop(id) ON DELETE CASCADE
);


-- public.sais_sensor definition

-- Drop table

-- DROP TABLE sais_sensor;

CREATE TABLE sais_sensor (
                             id bigserial NOT NULL,
                             sensor_code varchar(64) NOT NULL,
                             sensor_type int4 NOT NULL,
                             model varchar(100) NULL,
                             farm_id int8 NULL,
                             field_id int8 NULL,
                             is_mock bool DEFAULT true NOT NULL,
                             status int4 DEFAULT 1 NOT NULL,
                             creator varchar(64) DEFAULT ''::character varying NULL,
                             create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             updater varchar(64) DEFAULT ''::character varying NULL,
                             update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             deleted int2 DEFAULT 0 NOT NULL,
                             tenant_id int8 DEFAULT 0 NOT NULL,
                             CONSTRAINT sar_sensor_pkey PRIMARY KEY (id),
                             CONSTRAINT sar_sensor_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE,
                             CONSTRAINT sar_sensor_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE
);
CREATE INDEX idx_sensor_farm_id ON public.sais_sensor USING btree (farm_id);
CREATE INDEX idx_sensor_field_id ON public.sais_sensor USING btree (field_id);
CREATE INDEX idx_sensor_sensor_type ON public.sais_sensor USING btree (sensor_type);
CREATE INDEX idx_sensor_status ON public.sais_sensor USING btree (status);


-- public.sais_sensor_data definition

-- Drop table

-- DROP TABLE sais_sensor_data;

CREATE TABLE sais_sensor_data (
                                  id bigserial NOT NULL,
                                  sensor_id int8 NOT NULL,
                                  farm_id int8 NULL,
                                  field_id int8 NULL,
                                  data_type varchar(20) NOT NULL,
                                  value numeric(12, 4) NOT NULL,
                                  collected_at timestamp DEFAULT now() NOT NULL,
                                  creator varchar(64) DEFAULT ''::character varying NULL,
                                  create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                  updater varchar(64) DEFAULT ''::character varying NULL,
                                  update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                  deleted int2 DEFAULT 0 NOT NULL,
                                  tenant_id int8 DEFAULT 0 NOT NULL,
                                  CONSTRAINT sar_sensor_data_data_type_check CHECK (((data_type)::text = ANY (ARRAY[('SOIL_MOISTURE'::character varying)::text, ('HUMIDITY'::character varying)::text, ('TEMPERATURE'::character varying)::text]))),
	CONSTRAINT sar_sensor_data_pkey PRIMARY KEY (id),
	CONSTRAINT sar_sensor_data_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE,
	CONSTRAINT sar_sensor_data_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE,
	CONSTRAINT sar_sensor_data_sensor_id_fkey FOREIGN KEY (sensor_id) REFERENCES sais_sensor(id) ON DELETE CASCADE
);


-- public.sais_weather_data definition

-- Drop table

-- DROP TABLE sais_weather_data;

CREATE TABLE sais_weather_data (
                                   id bigserial NOT NULL,
                                   farm_id int8 NOT NULL,
                                   record_time timestamp NOT NULL,
                                   weather_desc varchar(100) NULL,
                                   temperature numeric(5, 2) NULL,
                                   humidity numeric(5, 2) NULL,
                                   rainfall numeric(6, 2) NULL,
                                   pressure numeric(7, 2) NULL,
                                   "source" varchar(50) NULL,
                                   creator varchar(64) DEFAULT ''::character varying NULL,
                                   create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   updater varchar(64) DEFAULT ''::character varying NULL,
                                   update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   deleted int2 DEFAULT 0 NOT NULL,
                                   tenant_id int8 DEFAULT 0 NOT NULL,
                                   forecast_date date NULL,
                                   temp_min numeric(6, 2) NULL,
                                   temp_max numeric(6, 2) NULL,
                                   CONSTRAINT sar_weather_data_pkey PRIMARY KEY (id),
                                   CONSTRAINT sar_weather_data_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE
);
CREATE INDEX idx_weather_farm_date ON public.sais_weather_data USING btree (farm_id, forecast_date);


-- public.qrtz_blob_triggers definition

-- Drop table

-- DROP TABLE qrtz_blob_triggers;

CREATE TABLE qrtz_blob_triggers (
                                    sched_name varchar(120) NOT NULL,
                                    trigger_name varchar(200) NOT NULL,
                                    trigger_group varchar(200) NOT NULL,
                                    blob_data bytea NULL,
                                    CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group),
                                    CONSTRAINT qrtz_blob_triggers_sched_name_trigger_name_trigger_group_fkey FOREIGN KEY (sched_name,trigger_name,trigger_group) REFERENCES qrtz_triggers(sched_name,trigger_name,trigger_group)
);


-- public.qrtz_cron_triggers definition

-- Drop table

-- DROP TABLE qrtz_cron_triggers;

CREATE TABLE qrtz_cron_triggers (
                                    sched_name varchar(120) NOT NULL,
                                    trigger_name varchar(200) NOT NULL,
                                    trigger_group varchar(200) NOT NULL,
                                    cron_expression varchar(120) NOT NULL,
                                    time_zone_id varchar(80) NULL,
                                    CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group),
                                    CONSTRAINT qrtz_cron_triggers_sched_name_trigger_name_trigger_group_fkey FOREIGN KEY (sched_name,trigger_name,trigger_group) REFERENCES qrtz_triggers(sched_name,trigger_name,trigger_group)
);


-- public.qrtz_simple_triggers definition

-- Drop table

-- DROP TABLE qrtz_simple_triggers;

CREATE TABLE qrtz_simple_triggers (
                                      sched_name varchar(120) NOT NULL,
                                      trigger_name varchar(200) NOT NULL,
                                      trigger_group varchar(200) NOT NULL,
                                      repeat_count int8 NOT NULL,
                                      repeat_interval int8 NOT NULL,
                                      times_triggered int8 NOT NULL,
                                      CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group),
                                      CONSTRAINT qrtz_simple_triggers_sched_name_trigger_name_trigger_group_fkey FOREIGN KEY (sched_name,trigger_name,trigger_group) REFERENCES qrtz_triggers(sched_name,trigger_name,trigger_group)
);


-- public.qrtz_simprop_triggers definition

-- Drop table

-- DROP TABLE qrtz_simprop_triggers;

CREATE TABLE qrtz_simprop_triggers (
                                       sched_name varchar(120) NOT NULL,
                                       trigger_name varchar(200) NOT NULL,
                                       trigger_group varchar(200) NOT NULL,
                                       str_prop_1 varchar(512) NULL,
                                       str_prop_2 varchar(512) NULL,
                                       str_prop_3 varchar(512) NULL,
                                       int_prop_1 int4 NULL,
                                       int_prop_2 int4 NULL,
                                       long_prop_1 int8 NULL,
                                       long_prop_2 int8 NULL,
                                       dec_prop_1 numeric(13, 4) NULL,
                                       dec_prop_2 numeric(13, 4) NULL,
                                       bool_prop_1 bool NULL,
                                       bool_prop_2 bool NULL,
                                       CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group),
                                       CONSTRAINT qrtz_simprop_triggers_sched_name_trigger_name_trigger_grou_fkey FOREIGN KEY (sched_name,trigger_name,trigger_group) REFERENCES qrtz_triggers(sched_name,trigger_name,trigger_group)
);


-- public.sais_crop_plan definition

-- Drop table

-- DROP TABLE sais_crop_plan;

CREATE TABLE sais_crop_plan (
                                id bigserial NOT NULL, -- Crop plan ID
                                crop_id int8 NOT NULL, -- Crop ID
                                field_id int8 NOT NULL, -- Field ID
                                grow_status int4 DEFAULT 1 NOT NULL, -- Growth status: 1=Unstarted, 2=Ongoing, 3=Finished
                                start_date date NOT NULL, -- Start date
                                end_date date NULL, -- End date
                                creator varchar(64) DEFAULT ''::character varying NULL,
                                create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                updater varchar(64) DEFAULT ''::character varying NULL,
                                update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                deleted int2 DEFAULT 0 NOT NULL,
                                tenant_id int8 DEFAULT 0 NOT NULL,
                                CONSTRAINT sar_crop_plan_check CHECK (((end_date IS NULL) OR (end_date >= start_date))),
                                CONSTRAINT sar_crop_plan_pkey PRIMARY KEY (id),
                                CONSTRAINT sar_crop_plan_crop_id_fkey FOREIGN KEY (crop_id) REFERENCES sais_crop(id) ON DELETE RESTRICT,
                                CONSTRAINT sar_crop_plan_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE
);
CREATE INDEX idx_crop_plan_crop_id ON public.sais_crop_plan USING btree (crop_id);
CREATE INDEX idx_crop_plan_field_id ON public.sais_crop_plan USING btree (field_id);
CREATE INDEX idx_crop_plan_status ON public.sais_crop_plan USING btree (grow_status);
COMMENT ON TABLE public.sais_crop_plan IS 'Crop Plan';

-- Column comments

COMMENT ON COLUMN public.sais_crop_plan.id IS 'Crop plan ID';
COMMENT ON COLUMN public.sais_crop_plan.crop_id IS 'Crop ID';
COMMENT ON COLUMN public.sais_crop_plan.field_id IS 'Field ID';
COMMENT ON COLUMN public.sais_crop_plan.grow_status IS 'Growth status: 1=Unstarted, 2=Ongoing, 3=Finished';
COMMENT ON COLUMN public.sais_crop_plan.start_date IS 'Start date';
COMMENT ON COLUMN public.sais_crop_plan.end_date IS 'End date';


-- public.sais_irrigation_device definition

-- Drop table

-- DROP TABLE sais_irrigation_device;

CREATE TABLE sais_irrigation_device (
                                        id bigserial NOT NULL,
                                        device_code varchar(64) NOT NULL,
                                        farm_id int8 NOT NULL,
                                        field_id int8 NOT NULL,
                                        flow_rate numeric(10, 2) NULL,
                                        is_watering bool DEFAULT false NOT NULL,
                                        status int4 DEFAULT 1 NOT NULL,
                                        creator varchar(64) DEFAULT ''::character varying NULL,
                                        create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        updater varchar(64) DEFAULT ''::character varying NULL,
                                        update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        deleted int2 DEFAULT 0 NOT NULL,
                                        tenant_id int8 DEFAULT 0 NOT NULL,
                                        sensor_id int8 NULL,
                                        simulate_fault bool DEFAULT false NOT NULL,
                                        CONSTRAINT sar_irrigation_device_device_code_key UNIQUE (device_code),
                                        CONSTRAINT sar_irrigation_device_pkey PRIMARY KEY (id),
                                        CONSTRAINT sar_irrigation_device_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE,
                                        CONSTRAINT sar_irrigation_device_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE,
                                        CONSTRAINT sar_irrigation_device_sensor_id_fkey FOREIGN KEY (sensor_id) REFERENCES sais_sensor(id) ON DELETE SET NULL
);
CREATE INDEX idx_irrigation_device_farm_id ON public.sais_irrigation_device USING btree (farm_id);
CREATE INDEX idx_irrigation_device_field_id ON public.sais_irrigation_device USING btree (field_id);
CREATE INDEX idx_irrigation_device_status ON public.sais_irrigation_device USING btree (status);


-- public.sais_irrigation_plan definition

-- Drop table

-- DROP TABLE sais_irrigation_plan;

CREATE TABLE sais_irrigation_plan (
                                      id bigserial NOT NULL,
                                      farm_id int8 NOT NULL,
                                      field_id int8 NOT NULL,
                                      device_id int8 NOT NULL,
                                      crop_plan_id int8 NULL,
                                      decision_source varchar(20) DEFAULT 'MANUAL'::character varying NOT NULL,
                                      decision_reason text NULL,
                                      planned_start_time timestamp NOT NULL,
                                      planned_duration int4 NOT NULL,
                                      status varchar(20) DEFAULT 'PENDING'::character varying NOT NULL,
                                      actual_start_time timestamp NULL,
                                      actual_end_time timestamp NULL,
                                      water_quantity numeric(12, 3) NULL,
                                      creator varchar(64) DEFAULT ''::character varying NULL,
                                      create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                      updater varchar(64) DEFAULT ''::character varying NULL,
                                      update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                      deleted int2 DEFAULT 0 NOT NULL,
                                      tenant_id int8 DEFAULT 0 NOT NULL,
                                      ack_received_at timestamp NULL,
                                      CONSTRAINT sar_irrigation_plan_decision_source_check CHECK (((decision_source)::text = ANY (ARRAY[('MANUAL'::character varying)::text, ('AI'::character varying)::text]))),
	CONSTRAINT sar_irrigation_plan_pkey PRIMARY KEY (id),
	CONSTRAINT sar_irrigation_plan_planned_duration_check CHECK ((planned_duration > 0)),
	CONSTRAINT sar_irrigation_plan_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('EXECUTING'::character varying)::text, ('COMPLETED'::character varying)::text, ('CANCELLED'::character varying)::text]))),
	CONSTRAINT sar_irrigation_plan_crop_plan_id_fkey FOREIGN KEY (crop_plan_id) REFERENCES sais_crop_plan(id) ON DELETE SET NULL,
	CONSTRAINT sar_irrigation_plan_device_id_fkey FOREIGN KEY (device_id) REFERENCES sais_irrigation_device(id) ON DELETE RESTRICT,
	CONSTRAINT sar_irrigation_plan_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE,
	CONSTRAINT sar_irrigation_plan_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE
);


-- public.sais_alert definition

-- Drop table

-- DROP TABLE sais_alert;

CREATE TABLE sais_alert (
                            id bigserial NOT NULL,
                            irrigation_plan_id int8 NULL,
                            farm_id int8 NULL,
                            field_id int8 NULL,
                            alert_type int2 NOT NULL,
                            "level" int2 DEFAULT 2 NOT NULL,
                            context text NOT NULL,
                            status int2 DEFAULT 0 NOT NULL,
                            triggered_at timestamp DEFAULT now() NOT NULL,
                            handled_at timestamp NULL,
                            creator varchar(64) DEFAULT ''::character varying NULL,
                            create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            updater varchar(64) DEFAULT ''::character varying NULL,
                            update_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            deleted int2 DEFAULT 0 NOT NULL,
                            tenant_id int8 DEFAULT 0 NOT NULL,
                            CONSTRAINT sar_alert_alert_type_check CHECK ((alert_type = ANY (ARRAY[1, 2, 3]))),
                            CONSTRAINT sar_alert_level_check CHECK ((level = ANY (ARRAY[1, 2, 3]))),
                            CONSTRAINT sar_alert_pkey PRIMARY KEY (id),
                            CONSTRAINT sar_alert_status_check CHECK ((status = ANY (ARRAY[0, 1, 2, 3]))),
                            CONSTRAINT sar_alert_farm_id_fkey FOREIGN KEY (farm_id) REFERENCES sais_farm(id) ON DELETE CASCADE,
                            CONSTRAINT sar_alert_field_id_fkey FOREIGN KEY (field_id) REFERENCES sais_field(id) ON DELETE CASCADE,
                            CONSTRAINT sar_alert_irrigation_plan_id_fkey FOREIGN KEY (irrigation_plan_id) REFERENCES sais_irrigation_plan(id) ON DELETE SET NULL
);