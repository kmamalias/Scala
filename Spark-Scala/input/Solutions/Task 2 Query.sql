/*
Query for each user returning the second distinct hotel that they clicked on, or null if the user did not click on two distinct hotels.
*/
WITH 
  T1 AS
    (SELECT USER_ID, HOTEL, MIN(TIME) TIME
    FROM BIWETL.JUNK_CLICKLOG 
    WHERE ACTION = 'Click'
    GROUP BY USER_ID, HOTEL),
  T2 AS
    (SELECT USER_ID, TIME, HOTEL, ROW_NUMBER() OVER (PARTITION BY USER_ID ORDER BY TIME) RN 
    FROM T1)
SELECT USER_ID, MAX(CASE WHEN RN = 2 THEN HOTEL ELSE NULL END) AS SECOND_DISTINCT_HOTEL_CLICKED
FROM T2
WHERE RN <= 2
GROUP BY USER_ID
ORDER BY 1;