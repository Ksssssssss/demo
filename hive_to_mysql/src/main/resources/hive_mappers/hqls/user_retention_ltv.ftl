select  snid,gameid,ds as stat_day ,install_date as install_day,install,datediff(ds,install_date) as retention_day,
		round(retention_uu/install,4) as retention_rate,
		retention_uu as retention_num,
		total_payment as total_amount
		from etl.a_user_retention_ltv where ds='${statDay}' ${snGameIdsSql}