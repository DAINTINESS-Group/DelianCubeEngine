1. First you need to setup the database.
The Adult_no_dublic.zip contains the cleaned version of the Adult data set without duplicates.
The files were created by exporting them ("dump") from a previously loaded v. in MySQL (Distrib 5.7.9).
There are just two files that are needed, (a)  the schema dump and (b) the data dump.
Since they are exported from mysql, they only need to be executed and they create the schema and load the data.
The latter is done via INSERT INTO rather than LOAD INFILE statements, however, since the db is small, we can live with it (I guess)(for the moment)

Note:
The PerTable_Schema folder is practically useless -- just contains the schema of each table in a separate file.

2. To work with the dataset, one needs to define a connection, hierarchies and cubes.
Given in the directory, ready for use: 
Connection info (dbc.ini)
For the cube adult_cube: adult.ini

