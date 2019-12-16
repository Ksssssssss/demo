select snid,gameid	,
ds,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
creative,
dr,
retention
from etl_dailyreport.pay_install_retention_os_creative_device where ds='${statDay}' ${snGameIdsSql}