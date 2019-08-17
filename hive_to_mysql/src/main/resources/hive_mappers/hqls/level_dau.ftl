
select snid,gameid,ds,dau_level,dau,total_dau
	from facts.f_level_dau 
	where ds='${statDay}' ${snGameIdsSql}