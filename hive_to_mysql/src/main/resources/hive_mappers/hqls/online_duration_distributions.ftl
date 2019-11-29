select snid,gameid	,
ds,
install_ds ,
duration,
duration_numbers,
duration_retention_numbers
from etl_dailyreport.online_duration_distributions where ds='${statDay}' ${snGameIdsSql}