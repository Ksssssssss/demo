select a.snid,a.gameid,clientid,round(payment_amount,2) as payment_amount,
dau,install,dnu,round(arpu,2) as arpu,
round(arppu,2) as arppu,
payer as pu,payment_cnt,pay_rate,gunfu_user as roll_users,
gunfu_payer as roll_pay_users,round(gunfu_amount,2) as roll_amount,
new_payer_client as new_pu,
round(new_payment_client,2) as new_pay_amount,
install_payer as install_pu,
round(install_payment,2) as install_pay_amount,
acu,avg_user_time,pcu,ds as day
from
(select snid,gameid,ds,clientid,dau,dnu,install,
		payment_cnt,pay_rate,payment_amount,
		payer,arpu,arppu,gunfu_user,gunfu_payer,gunfu_amount,
		acu,pcu,avg_user_time,new_payer_client,new_payment_client,
		install_payer,install_payment
	from facts.f_game_client_day 
	where ds='${statDay}' ${snGameIdsSql}
)a 
