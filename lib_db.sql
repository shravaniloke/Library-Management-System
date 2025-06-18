create database if not exists library_javadb;
use library_javadb;


create table users(
uid int auto_increment primary key,
Username varchar(30) not null,
Password varchar(50)
);

select * from users;



CREATE TABLE if not exists students (
    stuid VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    course VARCHAR(20)
);

select * from students;


CREATE TABLE if not exists books (
    bookid VARCHAR(20) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100),
    available BOOLEAN DEFAULT TRUE
);

INSERT INTO books (bookid, title, author, available) VALUES
('B001', 'Java Programming', 'Herbert Schildt', true),
('B002', 'Database Systems', 'C.J. Date', true),
('B003', 'Python for Beginners', 'Guido van Rossum', true),
('B004', 'Data Structures', 'Seymour Lipschutz', true);

select * from books;



CREATE TABLE if not exists issue_books(
    issueid INT AUTO_INCREMENT PRIMARY KEY,
    bookid VARCHAR(20),
    stuid VARCHAR(20),
    issue_date DATE,
    FOREIGN KEY (bookid) REFERENCES books(bookid),
    FOREIGN KEY (stuid) REFERENCES students(stuid)
);

CREATE TABLE if not exists return_books (
    returnid INT AUTO_INCREMENT PRIMARY KEY,
    bookid VARCHAR(20),
    stuid VARCHAR(20),
    issue_date DATE,
    return_date DATE,
    fine INT
);

