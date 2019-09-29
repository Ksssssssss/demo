select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.pay_retention where ds='${statDay}' ${snGameIdsSql}