select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.pay_retention_device where ds='${statDay}' ${snGameIdsSql}