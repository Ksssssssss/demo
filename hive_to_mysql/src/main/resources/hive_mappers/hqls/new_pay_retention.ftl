select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.new_pay_retention where ds='${statDay}' ${snGameIdsSql}