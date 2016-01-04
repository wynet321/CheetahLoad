package com.cheetahload.db;

public final class TableDefinition {
	public static final String TIMER = "create table timer (test_name varchar(50), script_name varchar(20), vuser_name varchar(20),duration int)";
	public static final String CONFIGURATION = "create table configuration (test_name varchar(50), tester_name varchar(20), tester_mail varchar(50),test_suite_name varchar(20), vuser_count int, duration int, loops int, think_time int, log_level varchar(10), log_file_size int, log_write_rate int)";
}
