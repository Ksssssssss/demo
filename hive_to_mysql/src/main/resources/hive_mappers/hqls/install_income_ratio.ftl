select snid,gameid,
ds,
install_ds,
income
from etl_dailyreport.install_income_ratio where ds='${statDay}' ${snGameIdsSql}