select snid,gameid,ds,user_level,new_payers,amount
	from facts.f_noclient_pay_level 
	where ds='${statDay}' ${snGameIdsSql}