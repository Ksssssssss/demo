SELECT
clientid,
gameid,
ds,
snid,
dr,
sum(retention) retention
FROM etl_dailyreport.retention_os_client
where ds='${statDay}' ${snGameIdsSql}
GROUP BY ds,dr,snid,gameid,clientid