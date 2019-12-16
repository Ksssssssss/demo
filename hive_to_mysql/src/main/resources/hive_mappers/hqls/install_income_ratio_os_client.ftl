select snid,gameid,
ds,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
clientid,
install_ds,
income
from etl_dailyreport.install_income_ratio_os_client where ds='${statDay}' ${snGameIdsSql}