select snid,gameid	,
ds,
os,
clientid,
dr,
retention
from etl_dailyreport.pay_retention_os_client_device where ds='${statDay}' ${snGameIdsSql}