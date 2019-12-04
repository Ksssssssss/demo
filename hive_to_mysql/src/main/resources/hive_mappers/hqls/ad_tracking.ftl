SELECT snid,
        gameid,
        ds,
        ifa,
cast(count(DISTINCT userid) AS STRING) numbers,
cast(count(userid) AS STRING) frequency
FROM rawdatas.adtracking
where ds='${statDay}' ${snGameIdsSql}
GROUP BY ifa,
         snid,
         gameid,
         ds