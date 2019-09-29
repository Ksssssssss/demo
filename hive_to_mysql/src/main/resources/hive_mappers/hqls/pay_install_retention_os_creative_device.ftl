select snid,gameid	,
ds,
os,
creative,
dr,
retention
from etl_dailyreport.pay_install_retention_os_creative_device where ds='${statDay}' ${snGameIdsSql}