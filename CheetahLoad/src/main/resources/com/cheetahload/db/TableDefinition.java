package main.resources.com.cheetahload.db;

public enum TableDefinition {
	SUITE("create table suite(id int not null auto_increment, name varchar(20), test_cases json, primary key(id))"), CONFIGURATION(
			"create table configuration(id int not null auto_increment, test_name varchar(50), tester_name varchar(20), tester_mail varchar(50),suite_id int, vuser_count int, duration int, loops int, think_time int, random_think_time varchar(5),log_level varchar(10), log_file_size int, log_write_rate int, start_time datetime, end_time datetime, primary key(id), foreign key(suite_id) references suite(id) on delete cascade)"), TIMER(
					"create table timer(id int not null auto_increment,script_name varchar(20), vuser_name varchar(20),duration int,configuration_id int, primary key(id), foreign key (configuration_id) references configuration(id) on delete cascade)"), TRANSACTION(
							"create table transaction(id int not null auto_increment, script_name varchar(20), vuser_name varchar(20), transaction_name varchar(20), duration int,configuration_id int, primary key(id), foreign key (configuration_id) references configuration(id) on delete cascade)");
	private String sql;

	private TableDefinition(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
}
