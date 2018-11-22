use pkdd99;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/orders.csv'
INTO TABLE orders FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
order_id, account_id, bank_to, account_to, amount, Reason
)
