SELECT
creative,
gameid,
ds,
snid,
dr,
sum(retention) retention
FROM etl_dailyreport.pay_retention_os_creative
where ds='${statDay}' ${snGameIdsSql}
GROUP BY ds,dr,snid,gameid,creative