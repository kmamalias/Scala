/*
Assume that everytime a csv file is processed, we will collect information from that load.
At the beginning of the job, we will refer to the load history table to check if the same file has been processed already.

If an identical input file is transferred to the system, the procedure LOAD_CLICKLOG_MERGE, is executed. It is a merge script to upsert data into the Clicklog table.
In addition to the merge procedure, the following procedure will insert data into a target table:
-The procedure LOAD_TEN_MOST_SEARCHED_DEST, will insert the ten most searched destinations for each 10 minute interval since 00:00:00 to a target table.
-The procedure LOAD_SECOND_CLICKED_HOTEL, is the insert Query for each user returning the second distinct hotel that they clicked on, or null if the user did not click on two distinct hotels.

Please refer to file: "Query for Task 3.sql" for the scripts.

The following are the ddl script required for this task:
*/

CREATE TABLE LOAD_HIST
/* Will hold import history information. */
(
	"LOAD_DATE" DATE,
	"FILENAME"  VARCHAR2(100 BYTE),
	"RECORD_COUNT" NUMBER(*,0)
);

CREATE TABLE STG_CLICKLOG
/* Stage table. Data cleansing is performed here. Old data is truncated and new data is loaded. */
( 
	"USER_ID" VARCHAR2(50 BYTE) NOT NULL, 
	"TIME" INTERVAL DAY (0) TO SECOND (0) NOT NULL, 
	"ACTION" VARCHAR2(50 BYTE) NOT NULL, 
	"DESTINATION" VARCHAR2(100 BYTE), 
	"HOTEL" VARCHAR2(250 BYTE),
	"FILENAME" VARCHAR2(100 BYTE),
	"LOAD_DATE" DATE
);

CREATE UNIQUE INDEX STG_CLICKLOG_PK ON STG_CLICKLOG
("USER_ID", "TIME", "ACTION");

CREATE TABLE CLICKLOG
/* Target table. */
( 
	"USER_ID" VARCHAR2(50 BYTE) NOT NULL, 
	"TIME" INTERVAL DAY (0) TO SECOND (0) NOT NULL, 
	"ACTION" VARCHAR2(50 BYTE) NOT NULL, 
	"DESTINATION" VARCHAR2(100 BYTE), 
	"HOTEL" VARCHAR2(250 BYTE)
);

CREATE UNIQUE INDEX CLICKLOG_PK ON CLICKLOG
("USER_ID", "TIME", "ACTION");

CREATE TABLE TEN_MOST_SEARCHED_DEST
/* Target table for the ten most searched destination at 10min interval. */
(
	"ETL_DATE" DATE NOT NULL,
	"MIN_INTERVAL" VARCHAR2(10 BYTE) NOT NULL,
	"DESTINATION" VARCHAR2(100 BYTE) NOT NULL,
);

CREATE TABLE SECOND_CLICKED_HOTEL
/* Target table for each user and the second distinct hotel that they clicked on, NULL if the user did not click on two distinct hotels. */
(
	"ETL_DATE" DATE NOT NULL,
	"USER_ID" VARCHAR2(50 BYTE) NOT NULL,
	"SECOND_DISTINCT_HOTEL_CLICKED"  VARCHAR2(250 BYTE)
);




