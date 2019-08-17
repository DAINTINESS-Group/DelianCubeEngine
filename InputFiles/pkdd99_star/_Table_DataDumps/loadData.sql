use pkdd99_star;

SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE LOAN;
TRUNCATE TABLE ORDERS;

TRUNCATE TABLE DATE;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/date.csv'
INTO TABLE date FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
    SK_Day, @var1, Month, Year, date.All
)
SET date.day = STR_TO_DATE(@var1, '%d/%m/%Y');


TRUNCATE TABLE PAYMENT_REASON;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/payment_reason.csv'
INTO TABLE payment_reason FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES;

TRUNCATE TABLE STATUS;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/status.csv'
INTO TABLE status FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES;

TRUNCATE TABLE ACCOUNT;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/account.csv'
INTO TABLE account FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES;

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/loan.csv'
INTO TABLE loan FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES;

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/orders.csv'
INTO TABLE orders FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
order_id, account_id, bank_to, account_to, amount, Reason_id
);
