select snid,gameid,
ds,
os,
install_ds,
income
from etl_dailyreport.install_income_ratio_os where ds='${statDay}' ${snGameIdsSql}