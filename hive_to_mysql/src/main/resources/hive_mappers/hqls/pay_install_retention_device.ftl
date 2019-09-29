select snid,gameid	,
ds,
dr,
retention
from etl_dailyreport.pay_install_retention_device where ds='${statDay}' ${snGameIdsSql}