select snid,gameid,ds stat_day,level,count(distinct userid) as payer_num,round(sum(total_payment_amount),1) as total_amount
	from aggr.a_user_day_new where ds='${statDay}' and total_payment_amount>0 ${snGameIdsSql}
group by snid,gameid,ds,level