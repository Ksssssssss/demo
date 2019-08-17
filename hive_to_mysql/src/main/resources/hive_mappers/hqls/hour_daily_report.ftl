select a.snid,a.gameid,'${statDay}' day,'${hour}' hour,dau,dnu,
	case when pay_users is null then cast(0 as string) else pay_users end as pu,
	case when payment_amount is null then cast(0 as string) else payment_amount end as payment,
	case when dnu_payers is null  then cast(0 as string) else dnu_payers end as install_pu,
	case when dnu_amount is null then cast(0 as string) else dnu_amount end as install_pay_amount,
	case when first_role_choice is null then cast(0 as string) else first_role_choice end as role_choice,
	d1 as d1,
	case when new_payers is null then cast(0 as string) else new_payers end as new_pu,
	case when new_payment is null then cast(0 as string) else new_payment end as new_payment,
	case when pay_times is  null then cast(0 as string) else pay_times end as payment_times,
	case when dnu_payers is null or dnu=0 or dnu is null then cast( 0 as double) else dnu_payers/dnu end as install_pay_rate,
	case when payment_amount is null or pay_users=0 or pay_users is null then cast(0 as double) else payment_amount/pay_users end as arppu,
	case when payment_amount is null or dau=0 then cast(0 as double) else payment_amount/dau end as arpu,
	case when pay_users is null or dau=0 then cast(0 as double) else pay_users/dau end as pay_rate,
	dau-dnu as loyal_user,
	case when first_role_choice is null or dnu is null or dnu=0 then cast(0 as double) else first_role_choice/dnu end as role_choice_rate
	from 
	( select * from tmp.daily_report_tmp where 1=1  ${snGameIdsSql}  ) a 
		left outer join 
	( select * from tmp.gunfu_info where 1=1  ${snGameIdsSql} ) b on a.snid=b.snid and a.gameid=b.gameid
