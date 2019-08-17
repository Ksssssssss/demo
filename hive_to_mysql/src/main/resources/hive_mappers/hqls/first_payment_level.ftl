select snid,gameid,ds stat_day, first_payment_level level,count(case when frist_amount>0 then userid end) as  payer_num,
		sum(frist_amount) as total_amount
from aggr.a_user_new
where ds='${statDay}' and to_date(first_payment_timestamp)=ds ${snGameIdsSql}
group by snid,gameid,ds,first_payment_level