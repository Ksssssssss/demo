select snid,gameid	,
ds,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
dr,
retention
from etl_dailyreport.pay_retention_os_device where ds='${statDay}' ${snGameIdsSql}