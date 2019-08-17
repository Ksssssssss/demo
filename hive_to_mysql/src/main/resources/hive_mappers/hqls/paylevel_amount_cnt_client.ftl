select 
	snid, gameid, 
      clientid, paylevel, payer, 
      total_payment_amount, total_payment_cnt, ds as stat_day 
from facts.paylevel_amount_cnt_client_new
where ds='${statDay}' ${snGameIdsSql}