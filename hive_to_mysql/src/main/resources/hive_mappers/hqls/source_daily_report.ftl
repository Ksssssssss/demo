
select tb1.*,
case when tb2.idfa is null then 0 else cast(tb2.idfa as int) end as idfa,
case when  tb2.distinct_idfa is null then 0 else cast(tb2.distinct_idfa as int) end as distinct_idfa,
case when tb2.distinct_ip is null then 0 else cast(tb2.distinct_ip as int) end as distinct_ip
from 
(
select  ds as day,t2.snid,t2.gameid,dlfrom as source,sum(dau) as dau,sum(dnu) as dnu,sum(install) as install,
		sum(payment_cnt) as payment_cnt,
		round(sum(payment_amount),2) as payment_amount,
		sum(payer) as pu,
		round(sum(payer)/sum(dau),3) as pay_rate,
		sum(gunfu_user) as roll_users,
		sum(gunfu_payer) as roll_pay_users,
		sum(gunfu_amount) as roll_amount,
		sum(new_payer) as new_pu,
		sum(new_payment) as new_pay_amount,
		sum(install_payer) as install_pu,
		round(sum(install_payment),2) as install_pay_amount,
		round(sum(payment_amount)/sum(dau),2) as arpu,
		case when sum(payer)=0 then 0.0 else round(sum(payment_amount)/sum(payer),2) end as arppu,
		max(acu) as acu,max(pcu) as pcu, max(avg_user_time) avg_user_time,
		sum(role_choice) role_choice
from
(select snid,gameid,ds,
if(split(install_dlfrom,"\\.")[1] is null,  
								if(split(install_dlfrom,"-")[1] is null,
											install_dlfrom,	
											concat(split(install_dlfrom,"-")[0],'-',split(install_dlfrom,"-")[1])),
								concat(split(install_dlfrom,"\\.")[0],'.',split(install_dlfrom,"\\.")[1])) as dlfrom,	
	
	dau,dnu,install,payment_cnt,payment_amount,payer,gunfu_user,gunfu_payer,acu,pcu,avg_user_time,
	gunfu_amount,new_payer,new_payment,install_payer,install_payment,role_choice
	from(
		select snid,gameid,ds,if(source="unknown" or source="",if(dl_from="unknown" or dl_from="","unknown",upper(dl_from)),upper(source)) as install_dlfrom,
         dau,dnu,install,payment_cnt,payment_amount,payer,gunfu_user,gunfu_payer,acu,pcu,avg_user_time,
		 case when gunfu_amount is null then cast(0.0 as float) else gunfu_amount end as gunfu_amount,
		 case when new_payer is null then 0 else new_payer end as new_payer,
		 case when new_payment is null then cast(0 as float) else new_payment end as new_payment,
		 case when  install_payer is null then 0 else install_payer end as install_payer,
		 case when install_payment is null then cast(0 as float) else install_payment end as install_payment,
		 role_choice
		 from facts.f_game_source_day where ds='${statDay}' ${snGameIdsSql})t1
)t2
group by t2.snid,t2.gameid,dlfrom,ds
) tb1 
left outer join 
(select ds,snid,gameid,upper(creative) as creative,idfa,distinct_idfa,distinct_ip from facts.adtrack_ip_idfa where ds='${statDay}' ${snGameIdsSql} ) tb2 
on tb1.day = tb2.ds and tb1.snid=tb2.snid and tb1.gameid=tb2.gameid and tb1.source = tb2.creative

