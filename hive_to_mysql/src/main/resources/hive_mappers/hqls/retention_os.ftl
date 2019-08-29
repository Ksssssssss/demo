select snid,gameid	,
os,
ds,
dr,
retention
from etl_dailyreport.retention_os where ds='${statDay}' ${snGameIdsSql}