select snid,gameid,'${statDay}' as day,'${hour}' as hour ,
case when channel is null then '' else channel end  as source,
case when dau is null then cast(0 as string) else dau end as dau,
case when dnu is null then cast(0 as string) else dnu end as dnu, 
case when dau is null then cast(0 as string) else dau end  - case when dnu is null then cast(0 as string) else dnu end  as loyal_user,
case when dau is null or dau =0 then cast(0 as double) else case when pay_users is null then cast(0 as double) else pay_users/dau end  end as pay_rate,
case when dau is null or dau =0 then cast(0 as double) else case when payment is null then cast(0 as double) else payment/dau end  end as arpu,
case when pay_users is null or pay_users=0 then cast(0 as double) else case when payment is null then cast(0 as double) else payment/pay_users end end as arppu,
case when pay_users is null then cast(0 as string) else pay_users end as pu,
case when payment is null then cast(0 as string) else payment end as payment,
case when pay_times is null then cast(0 as string) else pay_times end as payment_times,
case when new_payer is null then cast(0 as string) else new_payer end as new_pu,
case when new_payment is null then cast(0 as string) else new_payment end as new_payment,
case when install_payer is null then cast(0 as string) else install_payer end as install_pu,
case when install_payment is null then cast(0 as string)else install_payment end as install_pay_amount,
case when dnu is null then cast(0 as double) else case when install_payer is null then cast(0 as double) else install_payer/dnu end end as install_pay_rate,
case when d1 is null or d1='NaN' then cast(0 as string) else d1 end  as d1
from tmp.channel_daily_report where 1=1  ${snGameIdsSql} 