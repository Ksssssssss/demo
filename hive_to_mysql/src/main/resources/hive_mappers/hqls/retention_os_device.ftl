select snid,gameid	,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
ds,
dr,
retention
from etl_dailyreport.retention_os_device where ds='${statDay}' ${snGameIdsSql}