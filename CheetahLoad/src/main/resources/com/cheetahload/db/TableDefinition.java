package main.resources.com.cheetahload.db;

public final class TableDefinition {
	public static final String TIMER = "create table timer(id integer primary key autoincrement,script_name varchar(20), vuser_name varchar(20),duration int)";
	public static final String TRANSACTION = "create table tranx(script_name varchar(20), vuser_name varchar(20), transaction_name varchar(20), duration int)";
	public static final String CONFIGURATION = "create table configuration(test_name varchar(50), tester_name varchar(20), tester_mail varchar(50),test_suite_name varchar(20), vuser_count int, duration int, loops int, think_time int, random_think_time varchar(5),log_level varchar(10), log_file_size int, log_write_rate int, start_time text, end_time text)";
}
