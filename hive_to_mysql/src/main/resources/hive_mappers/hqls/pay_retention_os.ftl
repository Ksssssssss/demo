select snid,gameid	,
ds,
os,
dr,
retention
from etl_dailyreport.pay_retention_os where ds='${statDay}' ${snGameIdsSql}