# msc-dispatcher

Uses Maven to manage depenencies

To run application (make sure java jdk is set to version 11)
> mvn spring-boot:run

To package application 
> mvn package 

To access H2 database panel (assuming application runs on localhost and 8080 port).
The H2 console access credentials are as per specified in application.properties file
> http://localhost:8080/h2-console 

Reports and Analytics files path should be manually configured through the application.properties file.  
Currently pre-configured with an example of the path. The configured path should be pointing the the expected folder where report files will be outputted by the pipeline application.   
> e.g. application_reports_path=/Users/mwd/Desktop/Computer_Science/MSC_PROJECT/dissertation/mock-reports
 
 # Test Runbook
 
 - This application exposes several test endpoints to allow self-evaluation of all functionality.
 To enable such configuration set the following properties in the application.properties file (NB. currently set as defaults for the ease of use, but might be subject to change in future versions)
 > hub_domain=http://localhost:8080
   hub_profiling_endpoint=mock-json
   hub_state_report_endpoint=mock-state
   hub_file_paths_endpoint=mock-file-paths
   hub_file_endpoint=mock-file

To evaluate the application behaviour it will be necessary to observe the application console as it's the only place where the logging takes place. 
1) To Change the state of application (assuming application is running on localhost i.e. 127.0.0.1 address): 

After starting-up the application it shuold be in the idle state, with minimal logging activity.
+ expected message in logs is: 
<pre> App currently is IDLE, and performance batch runner is set to: false </pre>
By making a request to:
>  "localhost:8080/app-start"

The applicatinon should be started

The scheduler will be started up, and all jobs will begin executing - this will be reflected in the application console. 
For expected outputs (assuming self-evaluation configuration) check <b>TestController</b> class.

+ expected example console view from in logs: 
<pre>
> App currently is RUNNING, and performance batch runner is set to: true
> Report files check start.
> Pinging hub server with application status.
> Initiating performance data collection.
> Data dispatch initiated.
> Dispatch undispatched files to the hub.
> Report files check executing
> Dispatching profiling data data.
> Gathering a new batch of performance data.
> Attempting send state data to the main hub.
> Attempting to gather performance data
> Attempting to dispatch files generated during pipeline execution.
> Successfully sent state data to the main hub.
> Attempting send performance data to the main hub.
> Successfully sent performance data to the main hub.
> Report files check start.
> App currently is RUNNING, and performance batch runner is set to: true
> Report files check executing
> Saving profiling data batch.
> Gathering a new batch of performance data.
> Attempting to gather performance data
> Report files check start.
> App currently is RUNNING, and performance batch runner is set to: true
> Attempting to dispatch files generated during pipeline execution.
> Attempting send report file foo.txt to the main hub.
> Received file: foo.txt, size: 124, on id: 
> App currently is RUNNING, and performance batch runner is set to: true
> Report files check start.
> Report files check executing @Thread-14
> Saving profiling data batch.
> Gathering a new batch of performance data.
> Attempting to gather performance data
</pre>