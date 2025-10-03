DROP DATABASE IF EXISTS northwind_cube;
CREATE DATABASE northwind_cube;
USE northwind_cube;

DROP TABLE IF EXISTS `categories_dim`;
CREATE TABLE `categories_dim` (
  `CategoryID` int NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(15) NOT NULL,
  `c_desc` longtext,
  `All` char(5),
  PRIMARY KEY (`CategoryID`)
) AUTO_INCREMENT=9;

DROP TABLE IF EXISTS `customers_dim`;
CREATE TABLE `customers_dim` (
  `CustomerID` varchar(5) NOT NULL,
  `CompanyName` varchar(40) NOT NULL,
  `ContactName` varchar(30) DEFAULT NULL,
  `ContactTitle` varchar(30) DEFAULT NULL,
  `Address` varchar(60) DEFAULT NULL,
  `customer_city` varchar(15) DEFAULT NULL,
  `Region` varchar(15) DEFAULT NULL,
  `PostalCode` varchar(10) DEFAULT NULL,
  `customer_country` varchar(15) DEFAULT NULL,
  `Phone` varchar(24) DEFAULT NULL,
  `Fax` varchar(24) DEFAULT NULL,
  `All` char(5),
  PRIMARY KEY (`CustomerID`)
);

DROP TABLE IF EXISTS `employees_dim`;
CREATE TABLE `employees_dim` (
  `EmployeeID` int NOT NULL AUTO_INCREMENT,
  `LastName` varchar(20) NOT NULL,
  `FirstName` varchar(10) NOT NULL,
  `Title` varchar(30) DEFAULT NULL,
  `TitleOfCourtesy` varchar(25) DEFAULT NULL,
  `BirthDate` datetime DEFAULT NULL,
  `HireDate` datetime DEFAULT NULL,
  `Address` varchar(60) DEFAULT NULL,
  `employee_city` varchar(15) DEFAULT NULL,
  `Region` varchar(15) DEFAULT NULL,
  `PostalCode` varchar(10) DEFAULT NULL,
  `employee_country` varchar(15) DEFAULT NULL,
  `HomePhone` varchar(24) DEFAULT NULL,
  `Extension` varchar(4) DEFAULT NULL,
  `Notes` longtext,
  `ReportsTo` int DEFAULT NULL,
  `All` char(5),
  PRIMARY KEY (`EmployeeID`)
) AUTO_INCREMENT=10;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `OrderID` int NOT NULL,
  `o_customerID` varchar(5) DEFAULT NULL,
  `o_employeeID` int DEFAULT NULL,
  `o_orderDate` datetime DEFAULT NULL,
  `o_requiredDate` datetime DEFAULT NULL,
  `o_shippedDate` datetime DEFAULT NULL,
  `o_shipVia` int DEFAULT NULL,
  `o_freight` decimal(19,4) DEFAULT '0.0000',
  `o_shipName` varchar(40) DEFAULT NULL,
  `o_shipAddress` varchar(60) DEFAULT NULL,
  `o_shipCity` varchar(15) DEFAULT NULL,
  `o_shipRegion` varchar(15) DEFAULT NULL,
  `o_shipPostalCode` varchar(10) DEFAULT NULL,
  `o_shipCountry` varchar(15) DEFAULT NULL,
  `o_unitPrice` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `o_quantity` int NOT NULL DEFAULT '1',
  `o_discount` float NOT NULL DEFAULT '0',
  `ProductID` int NOT NULL,
  `o_productName` varchar(40) NOT NULL,
  `o_supplierID` int DEFAULT NULL,
  `o_categoryID` int DEFAULT NULL,
  `o_quantityPerUnit` varchar(20) DEFAULT NULL,
  `o_unitsInStock` int DEFAULT '0',
  `o_unitsOnOrder` int DEFAULT '0',
  `o_reorderLevel` int DEFAULT '0',
  `o_discontinued` tinyint DEFAULT '0',
  PRIMARY KEY (`OrderID`,`ProductID`)
);

DROP TABLE IF EXISTS `suppliers_dim`;
CREATE TABLE `suppliers_dim` (
  `SupplierID` int NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(40) NOT NULL,
  `ContactName` varchar(30) DEFAULT NULL,
  `ContactTitle` varchar(30) DEFAULT NULL,
  `Address` varchar(60) DEFAULT NULL,
  `supplier_city` varchar(15) DEFAULT NULL,
  `Region` varchar(15) DEFAULT NULL,
  `PostalCode` varchar(10) DEFAULT NULL,
  `supplier_country` varchar(15) DEFAULT NULL,
  `Phone` varchar(24) DEFAULT NULL,
  `Fax` varchar(24) DEFAULT NULL,
  `HomePage` longtext,
  `All` char(5),
  PRIMARY KEY (`SupplierID`)
) AUTO_INCREMENT=30;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/categories_dim.csv'
INTO TABLE categories_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/customers_dim.csv'
INTO TABLE customers_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/employees_dim.csv'
INTO TABLE employees_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/suppliers_dim.csv'
INTO TABLE suppliers_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/orders.csv'
INTO TABLE orders
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

alter table orders add constraint o_categories foreign key  (o_categoryID) references categories_dim (CategoryID);
alter table orders add constraint o_customers foreign key  (o_customerID) references customers_dim (CustomerID);
alter table orders add constraint o_suppliers foreign key  (o_supplierID) references suppliers_dim (SupplierID);
alter table orders add constraint o_employees foreign key  (o_employeeID) references employees_dim (EmployeeID);
