select snid,gameid,ds,install_date,retention_day,level,install,total_level,dau_level,lost_users,new_payers,new_payment
	from facts.f_level_install_payer 
	where ds='${statDay}' ${snGameIdsSql}