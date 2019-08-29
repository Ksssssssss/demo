select snid,gameid	,
os,
ds,
dr,
retention
from etl_dailyreport.retention_os_device where ds='${statDay}' ${snGameIdsSql}