select snid,gameid,
ds,
os,
clientid,
install_ds,
income
from etl_dailyreport.install_income_ratio_os_client where ds='${statDay}' ${snGameIdsSql}