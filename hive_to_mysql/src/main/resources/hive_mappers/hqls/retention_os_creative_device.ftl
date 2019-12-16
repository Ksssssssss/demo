select snid,gameid	,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
creative,
ds,
dr,
retention
from etl_dailyreport.retention_os_creative_device where ds='${statDay}' ${snGameIdsSql}