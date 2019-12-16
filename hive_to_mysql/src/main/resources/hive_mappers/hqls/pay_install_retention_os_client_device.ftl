select snid,gameid	,
ds,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
clientid,
dr,
retention
from etl_dailyreport.pay_install_retention_os_client_device where ds='${statDay}' ${snGameIdsSql}