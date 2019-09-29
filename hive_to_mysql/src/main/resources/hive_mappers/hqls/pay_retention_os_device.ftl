select snid,gameid	,
ds,
os,
dr,
retention
from etl_dailyreport.pay_retention_os_device where ds='${statDay}' ${snGameIdsSql}