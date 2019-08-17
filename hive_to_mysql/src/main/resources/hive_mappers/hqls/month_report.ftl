select snid,gameid,install,total_payment as payment_amount,
mau,mnu,arpu,arppu,payer as pu,pay_rate,
		new_payer as new_pu,new_payment as new_pay_amount,install_payer as install_pu,
		install_payment as install_pay_amount,monthid as month
		from facts.f_game_month
	where  monthid='${statMonth}' ${snGameIdsSql}