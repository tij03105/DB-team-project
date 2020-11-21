

create table MOVIE(
  Tconst CHAR(9) not null,
  Title VARCHAR(30),
  Title_Type VARCHAR(20),
  Is_Adult CHAR,
  Start_Year DATE,
  Runtime_Minutes NUMBER,
  Gcode CHAR(9) not null,
  primary key (Tconst)
);

create table GENRE(
  Genre_Code CHAR(9) not null,
  Genre VARCHAR(20),
  primary key (Genre_Code)
);

create table RATING(
  Tcon CHAR(9) not null,
  R_ID CHAR(9) not null,
  Average_Rating NUMBER,
  primary key (R_ID)
);

CREATE TABLE ACTOR(
Nconst			CHAR(9) NOT NULL,
Primary_Name 		VARCHAR(15) NOT NULL,
Name			VARCHAR(25) NOT NULL,
Birth_Year		DATE,
Death_Year		DATE,
Sex			CHAR,
PRIMARY KEY (Nconst));

CREATE TABLE EPISODE(
Episode_ID         CHAR(9) NOT NULL,
TVseries_Name         VARCHAR(30) NOT NULL,
Episode_Title         VARCHAR(30) NOT NULL,
Season_Number         NUMBER,
Episode_Number         NUMBER,
Is_Adult            CHAR,
Runtime_Minutes         NUMBER,
Tcon            CHAR(9) NOT NULL,
CONSTRAINT EpisodePK PRIMARY KEY (Episode_ID, Tcon));

CREATE TABLE VERSION(
Version_ID			CHAR(9) NOT NULL,
Nation				VARCHAR(10),
Original_Title			VARCHAR(40) NOT NULL,
Tcon				CHAR(9) NOT NULL,
PRIMARY KEY (Version_ID, Tcon));

CREATE TABLE PARTICIPATES(
Ncon				CHAR(9) NOT NULL,
Tcon				CHAR(9) NOT NULL,
Cast				VARCHAR(15) NOT NULL,
PRIMARY KEY (Ncon, Tcon));

create table ACCOUNT ( 
	ID		VARCHAR(15)	NOT NULL,
	PW		VARCHAR(20)	NOT NULL,
	Phone		VARCHAR(11)	NOT NULL,
	Name		VARCHAR(20)	NOT NULL,
	Address		VARCHAR(30),
	Sex		CHAR,
	Bdate		DATE,
	Job		VARCHAR(15),
	Membership	VARCHAR(10),
	Act_Limit		NUMBER,
	Rat_Limit		NUMBER,	  
	PRIMARY KEY (ID)
);

create table CREW (
	CrewID		CHAR(9)		NOT NULL,
	CrewType	VARCHAR(10),
	Name		VARCHAR(20)	NOT NULL,
	PRIMARY KEY (CrewID)
	);

create table MAKES (
	C_ID		CHAR(9)		NOT NULL,
	MTcon		CHAR(9)		NOT NULL,
	PRIMARY KEY (C_ID, MTcon)
	);

create table WATCHES (
	A_ID		VARCHAR(15)	NOT NULL,
	MTcon		CHAR(9)	NOT NULL,
	Rec_Time		VARCHAR(8),
	PRIMARY KEY (A_ID, MTcon)
	);

create table MYLIST(
	A_Id		VARCHAR(15)	NOT NULL,
	MTcon		CHAR(9)		NOT NULL, 
	PRIMARY KEY (A_ID, MTcon)
);

create table MANAGES(
	A_Id		VARCHAR(15)	NOT NULL,
	MTcon		CHAR(9)		NOT NULL,
	PRIMARY KEY (A_Id, MTcon)
);

create table PROVIDES(
	Rating 		NUMBER		NOT NULL,
	R_ID		CHAR(9)		NOT NULL,
	A_Id		VARCHAR(15)	NOT NULL,
	PRIMARY KEY (R_ID, A_Id)
);


ALTER TABLE MOVIE ADD FOREIGN KEY (Gcode) REFERENCES GENRE(Genre_Code) ON DELETE CASCADE;
ALTER TABLE RATING ADD FOREIGN KEY (TCON) REFERENCES MOVIE(TCONST) ON DELETE CASCADE;

ALTER TABLE EPISODE ADD FOREIGN KEY (Tcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE VERSION ADD FOREIGN KEY (Tcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE PARTICIPATES ADD FOREIGN KEY (Ncon) REFERENCES ACTOR(Nconst) ON DELETE CASCADE;
ALTER TABLE PARTICIPATES ADD FOREIGN KEY (Tcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;


ALTER TABLE MAKES ADD FOREIGN KEY(MTcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE MAKES ADD FOREIGN KEY(C_ID) REFERENCES CREW(CrewID) ON DELETE CASCADE;
ALTER TABLE WATCHES ADD FOREIGN KEY(MTcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE WATCHES ADD FOREIGN KEY(A_Id) REFERENCES ACCOUNT(ID) ON DELETE CASCADE;
ALTER TABLE MYLIST ADD FOREIGN KEY(MTcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE MYLIST ADD FOREIGN KEY(A_Id) REFERENCES ACCOUNT(ID) ON DELETE CASCADE;
ALTER TABLE MANAGES ADD FOREIGN KEY(A_Id) REFERENCES ACCOUNT(ID) ON DELETE CASCADE;
ALTER TABLE MANAGES ADD FOREIGN KEY(MTcon) REFERENCES MOVIE(Tconst) ON DELETE CASCADE;
ALTER TABLE PROVIDES ADD FOREIGN KEY(R_ID) REFERENCES RATING(R_ID) ON DELETE CASCADE;
ALTER TABLE PROVIDES ADD FOREIGN KEY(A_Id) REFERENCES ACCOUNT(ID) ON DELETE CASCADE;