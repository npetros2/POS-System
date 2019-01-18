DROP TABLE IF EXISTS QUANTITY;
DROP TABLE IF EXISTS CATAGORY;
DROP TABLE IF EXISTS ITEMS;



create table ITEMS
  (ITEM_NAME varchar(40) NOT NULL,
  ITEM_PRICE int NOT NULL,
  PRIMARY KEY (ITEM_NAME));
  
create table CATAGORY
  (ITEM_NAME varchar(32) NOT NULL,
  ITEM_TYPE varchar(32) NOT NULL,
  FOREIGN KEY (ITEM_NAME) REFERENCES ITEMS (ITEM_NAME));
  
create table QUANTITY
  (ITEM_NAME varchar(32) NOT NULL,
  ITEM_AMOUNT INT NOT NULL,
  PRIMARY KEY (ITEM_NAME));
  
