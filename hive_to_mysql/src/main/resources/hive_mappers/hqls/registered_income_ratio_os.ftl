select snid,gameid,
ds,
os,
registered_ds,
income
from etl_dailyreport.registered_income_ratio_os where ds='${statDay}' ${snGameIdsSql}