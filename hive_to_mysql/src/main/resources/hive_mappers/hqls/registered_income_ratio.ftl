select snid,gameid,
ds,
registered_ds,
income
from etl_dailyreport.registered_income_ratio where ds='${statDay}' ${snGameIdsSql}