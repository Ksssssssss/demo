select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.retention where ds='${statDay}' ${snGameIdsSql}