# ANALYZE MQO EXPERIMENTS REPEATABILITY

## Dependencies
First, access the links: 
https://github.com/DAINTINESS-Group/DelianCubeEngine} where  Delian Cubes Engine's source code of is located and https://drive.google.com/drive/folders/10iaqgjJkEiJ-dner5I98kIqp7LBtKcX0?dmr=1&ec=wgc-drive-globalnav-goto to access the raw data files of each supported dataset.
The system that performs the experiments should support Java 1.8 and MySQL Server.

## MySQL Setup
After downloading the source code, navigate to the InputFiles directory, where there is a directory for each dataset supported by Delian. Each directory contains a MySQL connection file, a cube initialization file and a cube schema creation SQL script. <br>
To create a database to MySQL Server utilizing a dataset, load the data files on MySQL Server's upload directory and run the respective cube schema script found at the InputFiles directory.

## DELIAN CUBES ENGINE SETUP
Next, load Delian Cube Engine project on an IDE that supports Java.
At the src/java/main directory, locate the client.analyzemqoexperiments package and the mainengine package.
Package client.analyzemqoexperiments contains two clients (AnalyzeMQOQueryCharacteristicsExperiment.java and AnalyzeMQOPerformanceEvaluationExperiment.java), one for each type of experiment we conducted (Query Characteristics Experiment and Performance Evaluation Experiment). On the top of the main() class of the clients, there is the \textit{userInputList} where: <br>
(i) the schema name, <br>
(ii) the credentials of the MySQL Server's user, <br>
(iii) the cube name(provided by the cube initialization file located on the dataset's directory on InputFiles) and <br>
(iv) the directory name that is assigned to the dataset in Delian Cubes Engine are set. Tune the above parameters with respect to the system's MySQL Server credentials and the dataset's details.

## EXECUTION
Package mainengine contains the Server.java file which is the server of the Delian Cubes Engine. Before running the clients, it is necessary to run the server in order to establish a database connection. After tuning the MySQL connection as mentioned, execute the clients that run the experiments.<br> 
Each query in the workload will be executed five times and the console will print the auxiliary queries along with information about the time taken for each stage of the algorithm's execution (query construction, query execution, and result management), the number of tuples returned by the operator and the report file with the operator's result.