select snid,gameid	,
ds,
visits,
average_visits,
average_stay_time
from etl_dailyreport.visit_trending where ds='${statDay}' ${snGameIdsSql}