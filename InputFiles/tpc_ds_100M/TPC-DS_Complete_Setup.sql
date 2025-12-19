DROP DATABASE IF EXISTS tpc_ds_cube_10m_NEW_indexed;
CREATE DATABASE tpc_ds_cube_10m_NEW_indexed;
USE  tpc_ds_cube_10m_NEW_indexed;

/* SCHEMA AND INDEXES INITIALIZATION */
create table date_dim
(
    d_date_sk                 integer               not null,
    d_date                    date                  not null,
	d_month_and_year          varchar(10)                   ,
    d_quarter_name            char(6)                       ,
    d_year                    integer                       ,
	`All`                     char(5)                       ,
    primary key (d_date_sk)									
);

CREATE INDEX idx_d_date ON date_dim(d_date);
CREATE INDEX idx_d_month_and_year ON date_dim(d_month_and_year);
CREATE INDEX idx_d_quarter_name ON date_dim(d_quarter_name);
CREATE INDEX idx_d_year ON date_dim(d_year);



create table time_dim
(
    t_time_sk                 integer               not null,
    t_time                    integer               not null,
    t_hour                    integer                       ,
    t_sub_shift               char(20)                      ,
    `All`                     char(5)                       ,
    primary key (t_time_sk)									
);

CREATE INDEX idx_t_time ON time_dim(t_time);
CREATE INDEX idx_t_hour ON time_dim(t_hour);
CREATE INDEX idx_t_sub_shift ON time_dim(t_sub_shift);

create table item
(
    i_item_sk                 integer               not null,
    i_product_name            char(50)                      ,
    i_category                char(50)                      ,
    `All`                     char(5)                       ,
    primary key (i_item_sk)									
);

CREATE INDEX idx_i_product_name ON item(i_product_name);
CREATE INDEX idx_i_category ON item(i_category);

create table store
(
    s_store_sk                integer               not null,
    s_store_name			  varchar(50)					,
    s_city                    varchar(60)                   ,
    s_county                  varchar(30)                   ,
    s_state                   char(2)                       ,
    s_country                 varchar(20)                   ,
    `All`                     char(5)                       ,
    primary key (s_store_sk)								
);

CREATE INDEX idx_s_city ON store(s_city);
CREATE INDEX idx_s_county ON store(s_county);
CREATE INDEX idx_s_state ON store(s_state);
CREATE INDEX idx_s_country ON store(s_country);

create table customer
(
    c_customer_sk             integer               not null,
    c_last_name               char(30)                      ,
    c_birth_month_and_year    char(10)						,
    c_birth_year			  char(4)						,
    `All`                     char(5)                       ,
    primary key (c_customer_sk)								
);

CREATE INDEX idx_c_last_name ON customer(c_last_name);
CREATE INDEX idx_c_birth_month_and_year ON customer(c_birth_month_and_year);
CREATE INDEX idx_c_birth_year ON customer(c_birth_year);

create table customer_address
(
    ca_address_sk             integer               not null,
    ca_state                  char(2)                       ,
    ca_country                varchar(20)                   ,
    `All`                     char(5)                       ,
    primary key (ca_address_sk)								
);

CREATE INDEX idx_ca_state ON customer_address(ca_state);
CREATE INDEX idx_ca_country ON customer_address(ca_country);

create table store_sales
(
    ss_sold_date_sk           integer                       ,
    ss_sold_time_sk           integer                       ,
    ss_item_sk                integer               not null,
    ss_customer_sk            integer               default null,
    ss_addr_sk                integer                       ,
    ss_store_sk               integer                       ,
    ss_ticket_number          integer               not null,
    ss_quantity               integer               not null,
    ss_wholesale_cost         decimal(7,2)          not null,
    primary key (ss_item_sk, ss_ticket_number)
);

/* LOAD DATA */
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/customer_dim_10m.csv'
INTO TABLE customer
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/customer_address_dim_10m.csv'
INTO TABLE customer_address
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/date_dim_10m.csv'
INTO TABLE date_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/item_dim_10m.csv'
INTO TABLE item
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/store_dim_10m.csv'
INTO TABLE store
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/time_dim_10m.csv'
INTO TABLE time_dim
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/store_sales_10m.csv'
INTO TABLE store_sales
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n';

/* ADD FOREIGN KEY CONSTRAINTS */
ALTER TABLE store_sales
ADD FOREIGN KEY (ss_sold_date_sk) REFERENCES date_dim(d_date_sk); 

ALTER TABLE store_sales
ADD FOREIGN KEY (ss_sold_time_sk) REFERENCES time_dim(t_time_sk); 

ALTER TABLE store_sales
ADD FOREIGN KEY (ss_item_sk) REFERENCES item(i_item_sk); 

ALTER TABLE store_sales
ADD FOREIGN KEY (ss_customer_sk) REFERENCES customer(c_customer_sk); 

ALTER TABLE store_sales
ADD FOREIGN KEY (ss_addr_sk) REFERENCES customer_address(ca_address_sk); 

ALTER TABLE store_sales
ADD FOREIGN KEY (ss_store_sk) REFERENCES store(s_store_sk); 
