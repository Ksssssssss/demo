select snid,gameid	,
os,
clientid,
ds,
dr,
retention
from etl_dailyreport.retention_os_client_device where ds='${statDay}' ${snGameIdsSql}