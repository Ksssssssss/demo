select snid,gameid,
ds,
os,
creative,
registered_ds,
income
from etl_dailyreport.registered_income_ratio_os_creative where ds='${statDay}' ${snGameIdsSql}