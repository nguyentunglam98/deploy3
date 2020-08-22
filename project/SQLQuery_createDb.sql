CREATE DATABASE testdemo
CREATE TABLE Users (
    UserName varchar(255) primary key,
    [PassWord] varchar(255),
	[Name] varchar(255)
);
 go

CREATE TABLE Roles (
    RoleID int primary key,
	[RoleName] varchar(255)
);

go

CREATE TABLE UserRole (
    RoleID int ,
	UserName varchar(255),
	PRIMARY KEY (RoleID,UserName)
);
go
ALTER TABLE UserRole
ADD FOREIGN KEY (RoleID) REFERENCES Roles(RoleID);
go
ALTER TABLE UserRole
ADD FOREIGN KEY (UserName) REFERENCES Users(UserName);