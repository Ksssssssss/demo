select snid, gameid, 
      source, paylevel, payer, 
      total_payment_amount, total_payment_cnt, ds as stat_day 
from facts.paylevel_amount_cnt_source_new
where ds='${statDay}' ${snGameIdsSql}