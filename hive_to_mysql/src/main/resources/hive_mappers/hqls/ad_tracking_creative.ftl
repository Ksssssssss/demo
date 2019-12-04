SELECT snid,
        gameid,
        ds,
        ifa,
        creative,
         numbers,
frequency
FROM rawdatas.adtracking_creative
where ds='${statDay}' ${snGameIdsSql}