SELECT creative,
snid,
gameid,
ds,
sum(dau_num) dau_num,
sum(install_num) install_num,
sum(pay_count) pay_count,
sum(pay_amount) pay_amount,
sum(pay_times) pay_times,
sum(pay_install_amount) pay_install_amount,
sum(pay_install_count) pay_install_count,
sum(pay_install_times) pay_install_times,
sum(new_pay_count) new_pay_count,
sum(new_pay_amount) new_pay_amount,
sum(new_pay_times) new_pay_times
FROM etl_dailyreport.daily_os_creative_stats
where ds='${statDay}' ${snGameIdsSql}
GROUP BY creative,
snid,
gameid,
ds