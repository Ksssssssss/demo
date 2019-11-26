select snid,gameid,
ds,
os,
creative,
install_ds,
income
from etl_dailyreport.install_income_ratio_os_creative where ds='${statDay}' ${snGameIdsSql}