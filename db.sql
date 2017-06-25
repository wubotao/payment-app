--Database script

--drop database payment;

create database payment;

use payment;

create table record (
    id int(20) primary key auto_increment,
    first_name varchar(255),
    last_name varchar(255),
    phone varchar(20),
    amount decimal(10, 2),
    currency varchar(10),
    payment_type int(1),
    payment_id varchar(255),
    status int(1),
    error varchar(255),
    create_time datetime
);
