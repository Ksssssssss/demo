select snid,gameid	,
ds,
os,
clientid,
dr,
retention
from etl_dailyreport.pay_install_retention_os_client_device where ds='${statDay}' ${snGameIdsSql}