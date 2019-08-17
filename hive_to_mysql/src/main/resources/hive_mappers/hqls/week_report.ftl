select snid,gameid,total_payment as payment_amount,wau,install,wnu,arpu,arppu,
	payer as pu,payment_cnt,pay_rate,gunfu_user as roll_users,
	gunfu_payer as roll_pay_users,gunfu_amount as roll_amount,
	new_payer as new_pu,new_payment as new_pay_amount,
	install_payer as install_pu, if(install_payment is null,0,install_payment) as install_pay_amount,weekid week
	from facts.f_game_week where  weekid="${statWeek}" ${snGameIdsSql}