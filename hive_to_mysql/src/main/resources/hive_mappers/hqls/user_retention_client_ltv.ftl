select snid,gameid,clientid,install_date as install_day,
install,date_diff as retention_day,retention_uu as retention_num,total_amount,ds as stat_day,
round(retention_uu/install,4) as retention_rate
from etl.a_user_retention_client_ltv 
where  ds='${statDay}' and  (date_diff<=30 or date_diff=45 or date_diff=60 or date_diff=90) ${snGameIdsSql}