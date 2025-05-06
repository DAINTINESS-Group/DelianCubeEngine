DROP DATABASE foodmart_reduced;
CREATE DATABASE IF NOT EXISTS foodmart_reduced;

SET sql_mode = '';
SET GLOBAL sql_mode = '';

USE foodmart_reduced;
-- Δημιουργία πίνακα product
DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL,
  `product_class_id` int NOT NULL,
  `brand_name` varchar(60) DEFAULT NULL,
  `product_name` varchar(60) NOT NULL,
  `SKU` bigint NOT NULL,
  `SRP` decimal(10,4) DEFAULT NULL,
  `gross_weight` double DEFAULT NULL,
  `net_weight` double DEFAULT NULL,
  `recyclable_package` tinyint(1) DEFAULT NULL,
  `low_fat` tinyint(1) DEFAULT NULL,
  `units_per_case` smallint DEFAULT NULL,
  `cases_per_pallet` smallint DEFAULT NULL,
  `shelf_width` double DEFAULT NULL,
  `shelf_height` double DEFAULT NULL,
  `shelf_depth` double DEFAULT NULL,
  `product_subcategory` varchar(60) DEFAULT NULL,
  `product_category` varchar(60) NOT NULL,
  `product_department` varchar(60) NOT NULL,
  `product_family` varchar(60) DEFAULT NULL,
  `All` VARCHAR(5) NOT NULL,
  PRIMARY KEY `i_product_id` (`product_id`),
  KEY `i_prod_brand_name` (`brand_name`),
  KEY `i_prod_class_id` (`product_class_id`),
  KEY `i_product_name` (`product_name`),
  KEY `i_product_SKU` (`SKU`),
  KEY `i_product_subcategory` (`product_subcategory`),
  KEY `i_product_category` (`product_category`),
  KEY `i_product_department` (`product_department`),
  KEY `i_product_family` (`product_family`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Δημιουργία πίνακα date
DROP TABLE IF EXISTS `date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `date` (
  `time_id` int NOT NULL,
  `the_date` date DEFAULT NULL,
  `the_day` varchar(30) DEFAULT NULL,
  `the_month` varchar(30) DEFAULT NULL,
  `the_year` smallint DEFAULT NULL,
  `day_of_month` smallint DEFAULT NULL,
  `week_of_year` int DEFAULT NULL,
  `month_of_year` smallint DEFAULT NULL,
  `quarter` varchar(30) DEFAULT NULL,
  `year_and_month` varchar(7) DEFAULT NULL,
  `year_quarter` varchar(10) DEFAULT NULL,
  `All` VARCHAR(5) NOT NULL,
  PRIMARY KEY `i_time_id` (`time_id`),
  KEY `i_time_day` (`the_date`),
  KEY `i_time_year` (`the_year`),
  KEY `i_time_quarter` (`quarter`),
  KEY `i_time_month` (`month_of_year`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Δημιουργία πίνακα customer
DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL,
  `account_num` bigint NOT NULL,
  `lname` varchar(30) NOT NULL,
  `fname` varchar(30) NOT NULL,
  `mi` varchar(30) DEFAULT NULL,
  `address1` varchar(30) DEFAULT NULL,
  `address2` varchar(30) DEFAULT NULL,
  `address3` varchar(30) DEFAULT NULL,
  `address4` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `state_province` varchar(30) DEFAULT NULL,
  `postal_code` varchar(30) NOT NULL,
  `country` varchar(30) NOT NULL,
  `customer_region_id` int NOT NULL,
  `phone1` varchar(30) NOT NULL,
  `phone2` varchar(30) NOT NULL,
  `birthdate` date NOT NULL,
  `marital_status` varchar(30) NOT NULL,
  `yearly_income` varchar(30) NOT NULL,
  `gender` varchar(30) NOT NULL,
  `total_children` smallint NOT NULL,
  `num_children_at_home` smallint NOT NULL,
  `education` varchar(30) NOT NULL,
  `date_accnt_opened` date NOT NULL,
  `member_card` varchar(30) DEFAULT NULL,
  `occupation` varchar(30) DEFAULT NULL,
  `houseowner` varchar(30) DEFAULT NULL,
  `num_cars_owned` int DEFAULT NULL,
  `fullname` varchar(60) NOT NULL,
  `All` VARCHAR(5) NOT NULL,
  PRIMARY KEY `i_customer_id` (`customer_id`),
  KEY `i_cust_acct_num` (`account_num`),
  KEY `i_customer_fname` (`fname`),
  KEY `i_customer_lname` (`lname`),
  KEY `i_cust_child_home` (`num_children_at_home`),
  KEY `i_cust_postal_code` (`postal_code`),
  KEY `i_cust_region_id` (`customer_region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Δημιουργία πίνακα promotion
DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `promotion_id` int NOT NULL,
  `promotion_district_id` int DEFAULT NULL,
  `promotion_name` varchar(30) DEFAULT NULL,
  `media_type` varchar(30) DEFAULT NULL,
  `cost` decimal(10,4) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `All` VARCHAR(5) NOT NULL,
  PRIMARY KEY `i_promotion_id` (`promotion_id`),
  KEY `i_promo_dist_id` (`promotion_district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Δημιουργία πίνακα store
DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `store_id` int NOT NULL,
  `store_type` varchar(30) DEFAULT NULL,
  `region_id` int DEFAULT NULL,
  `store_name` varchar(30) DEFAULT NULL,
  `store_number` int DEFAULT NULL,
  `store_street_address` varchar(30) DEFAULT NULL,
  `store_city` varchar(30) DEFAULT NULL,
  `store_state` varchar(30) DEFAULT NULL,
  `store_postal_code` varchar(30) DEFAULT NULL,
  `store_country` varchar(30) DEFAULT NULL,
  `store_manager` varchar(30) DEFAULT NULL,
  `store_phone` varchar(30) DEFAULT NULL,
  `store_fax` varchar(30) DEFAULT NULL,
  `first_opened_date` date DEFAULT NULL,
  `last_remodel_date` date DEFAULT NULL,
  `store_sqft` int DEFAULT NULL,
  `grocery_sqft` int DEFAULT NULL,
  `frozen_sqft` int DEFAULT NULL,
  `meat_sqft` int DEFAULT NULL,
  `coffee_bar` tinyint(1) DEFAULT NULL,
  `video_store` tinyint(1) DEFAULT NULL,
  `salad_bar` tinyint(1) DEFAULT NULL,
  `prepared_food` tinyint(1) DEFAULT NULL,
  `florist` tinyint(1) DEFAULT NULL,
  `All` VARCHAR(5) NOT NULL,
  PRIMARY KEY `i_store_id` (`store_id`),
  KEY `i_store_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Δημιουργία πίνακα sales
DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sales_id` int NOT NULL,
  `product_id` int NOT NULL,
  `time_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  `store_id` int NOT NULL,
  `store_sales` decimal(10,4) NOT NULL,
  `store_cost` decimal(10,4) NOT NULL,
  `unit_sales` decimal(10,4) NOT NULL,
  PRIMARY KEY (sales_id),
  FOREIGN KEY (product_id) REFERENCES product(product_id),
  FOREIGN KEY (time_id) REFERENCES date(time_id),
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
  FOREIGN KEY (promotion_id) REFERENCES promotion(promotion_id),
  FOREIGN KEY (store_id) REFERENCES store(store_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

USE foodmart_reduced;
LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/product.csv'
INTO TABLE product FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
product_id, product_class_id, brand_name, product_name, SKU, SRP, gross_weight, net_weight, recyclable_package, low_fat, units_per_case, cases_per_pallet, shelf_width, shelf_height, shelf_depth, 
product_subcategory, product_category, product_department, product_family, product.All
);

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/date.csv'
INTO TABLE date FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
time_id, the_date, the_day, the_month, the_year, day_of_month, week_of_year, month_of_year, quarter, year_and_month, year_quarter, date.All
);

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/customer.csv'
INTO TABLE customer FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
customer_id, account_num, lname, fname, mi, address1, address2, address3, address4, city, state_province, postal_code, 
country, customer_region_id, phone1, phone2, birthdate, marital_status, yearly_income, gender, total_children, num_children_at_home, 
education, date_accnt_opened, member_card, occupation, houseowner, num_cars_owned, fullname, customer.All
)
;

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/promotion.csv'
INTO TABLE promotion FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
promotion_id, promotion_district_id, promotion_name, media_type, @cost, @start_date, @end_date, promotion.All
)
SET 
cost = NULLIF(@cost,''),
start_date = NULLIF(@start_date,''),
end_date = NULLIF(@end_date,'')
;

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/store.csv'
INTO TABLE store FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
store_id, store_type, region_id, store_name, store_number, store_street_address, store_city, 
store_state, store_postal_code, store_country, @store_manager, @store_phone, @store_fax,
@first_opened_date, @last_remodel_date, store_sqft, grocery_sqft, frozen_sqft, meat_sqft, 
coffee_bar, video_store, salad_bar, prepared_food, florist, store.All
)
SET 
store_manager = NULLIF(@store_manager,''), 
store_phone = NULLIF(@store_phone,''),
store_fax = NULLIF(@store_fax,''),
first_opened_date = NULLIF(@first_opened_date, ''),
last_remodel_date = NULLIF(@last_remodel_date, '')
;

LOAD DATA INFILE  'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/sales.csv'
INTO TABLE sales FIELDS TERMINATED BY ';' LINES TERMINATED BY '\r\n'  IGNORE 1 LINES
(
sales_id, product_id, time_id, customer_id, promotion_id, store_id, store_sales, store_cost, unit_sales
)
;

