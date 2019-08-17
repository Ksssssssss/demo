select dau_date,snid,gameid,ds,firstpay_date,retention_day,firstpay_users,
daupay_users,retention_uu,total_payment,
dau_pay from etl.payuser_retention_ltv
where ds='${statDay}' ${snGameIdsSql} 