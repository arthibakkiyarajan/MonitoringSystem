#########################################################_Monitoring_System_##########################################################################

1. Clone MonitoringSystem.
2. Open terminal and browse to location where MonitoringSystem is cloned. Execute cd MonitoringSystem/src command.
3. Execute ls -lrt in src folder. It will display files along with generate.sh and query.sh.
4. generate.sh is used to generate logs.
5. query.sh is used to query logs and display results. 

#########################################################_Generating_Logs_##########################################################################

1. Execute generate.sh by executing below command. This command generates logs for 1000 servers for one day. The command takes approximately 4 seconds to generate logs. This generates logs for 1000 sensors every one minute.  
	./generate.sh LOG_PATH
2. If LOG_PATH is not a valid path, following error message will be displayed.
	"Invalid path/No permission. Please enter a valid file path to generate logs."
3. If no LOG_PATH is specified, following error message will be displayed and user can enter a valid path in the terminal
	"Invalid path/No permission. Please enter a valid file path to generate logs."
4. If LOG_PATH is valid, the user will be asked to enter date (Format: yyyy-MM-dd). It is optional. If the user doesn't enter a date, default date(2014-10-31) will be used to generate logs. 
5. If valid date is entered then log generation process will get started and following message will be displayed.
	"Generating Logs...."
6. When log generation is done, "Done" message will be displayed in terminal.
7. Check in the specified folder(LOG_PATH) to make sure the logs are generated correctly.

#########################################################_Executing_Query_##########################################################################

1. Execute query.sh by executing below command. This command launches the interactive tool. 
	./query.sh LOG_PATH
2. If LOG_PATH is empty, error message will be displayed.

3. If valid LOG_PATH (path which was used to generate logs) is entered, it will load the tool. Once the tool is loaded, it displays message to enter query. The query should be entered in following format:
	QUERY [IP] [CPU] [START_DATE] [START_TIME] [END_DATE] [END_TIME]	
	QUERY xxx.xxx.x.xx 1 2014-10-31 00:00 2014-10-31 00:05
4. The above step displays result for the query. After displaying the result, it will ask the user to enter another query string. The user can enter another query string or exit by executing below command:
	EXIT

######################################################################################################################################################
