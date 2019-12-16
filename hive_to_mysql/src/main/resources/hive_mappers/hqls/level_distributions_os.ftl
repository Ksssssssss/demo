SELECT gameid,snid,
CASE
WHEN lower(os)='android' THEN 'android'
WHEN lower(os)='ios' THEN 'iOS'
ELSE os
END AS os,
ds,level,install_level_numbers,dau_level_numbers,pay_level_numbers,pay_install_level_numbers,new_pay_level_numbers
from etl_dailyreport.level_distributions_os
where ds='${statDay}' ${snGameIdsSql}