SELECT
clientid,
gameid,
ds,
snid,
dr,
sum(retention) retention
FROM etl_dailyreport.pay_install_retention_os_client
where ds='${statDay}' ${snGameIdsSql}
GROUP BY ds,dr,snid,gameid,clientid