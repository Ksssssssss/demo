select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.retention_device where ds='${statDay}' ${snGameIdsSql}