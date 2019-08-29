select snid,gameid	,
os,
creative,
ds,
dr,
retention
from etl_dailyreport.retention_os_creative where ds='${statDay}' ${snGameIdsSql}