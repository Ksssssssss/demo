SELECT ds,
snid,
gameid,
count(userid) AS exchange_times,
count(DISTINCT userid) AS exchange_count,
sum(amount) AS exchange_amount
FROM
(SELECT ds,
snid,
gameid,
economytime,
userid,
collect_set(amount)[0] amount
FROM rawdatas.economy
where ds='${statDay}' ${snGameIdsSql}
AND kingdom='inflow'
AND phylum='userService.exchangeGold'
GROUP BY ds,
snid,
gameid,
userid,
economytime) t_1
GROUP BY ds,
gameid,
snid