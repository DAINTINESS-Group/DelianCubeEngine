use pkdd99;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/date.csv'
INTO TABLE date FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
    SK_Day, @var1, Month, Year, date.All
)
SET date.day = STR_TO_DATE(@var1, '%d/%m/%Y')