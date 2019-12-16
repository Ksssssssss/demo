SELECT snid,
        gameid,
        ds,
        ifa,
        creative,
         numbers,
        frequency
FROM etl_dailyreport.ad_tracking_creative
where ds='${statDay}' ${snGameIdsSql}