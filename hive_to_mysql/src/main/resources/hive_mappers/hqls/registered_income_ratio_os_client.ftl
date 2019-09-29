select snid,gameid,
ds,
os,
clientid,
registered_ds,
income
from etl_dailyreport.registered_income_ratio_os_client where ds='${statDay}' ${snGameIdsSql}