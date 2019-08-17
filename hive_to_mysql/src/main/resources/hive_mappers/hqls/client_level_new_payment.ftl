select snid,gameid,ds,clientid ,user_level,new_payers,amount
	from facts.f_client_pay_level 
	where ds='${statDay}' ${snGameIdsSql}