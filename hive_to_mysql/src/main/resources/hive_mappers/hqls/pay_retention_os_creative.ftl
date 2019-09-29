select snid,gameid	,
ds,
os,
creative,
dr,
retention
from etl_dailyreport.pay_retention_os_creative where ds='${statDay}' ${snGameIdsSql}