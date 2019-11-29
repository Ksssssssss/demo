select snid,gameid	,
ds,
device,
device_install_numbers,
device_dau_numbers
from etl_dailyreport.device_distributions where ds='${statDay}' ${snGameIdsSql}