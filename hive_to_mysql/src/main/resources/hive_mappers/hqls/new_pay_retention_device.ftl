select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.new_pay_retention_device where ds='${statDay}' ${snGameIdsSql}