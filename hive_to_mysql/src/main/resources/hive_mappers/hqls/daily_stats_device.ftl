select snid,gameid    ,
pay_amount,
pay_count    ,
pay_times   ,
pay_install_amount,
pay_install_count    ,
pay_install_times   ,
new_pay_amount,
new_pay_count    ,
new_pay_times   ,
dau_num    ,
install_num    ,
ds
from etl_dailyreport.daily_stats_device where ds='${statDay}' ${snGameIdsSql}