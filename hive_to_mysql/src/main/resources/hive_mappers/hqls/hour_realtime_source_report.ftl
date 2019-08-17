select a.snid as snid,a.gameid as gameid,a.ds as ds,
a.creative as createive, 
case when install is null then 0 else cast(install as int) end as install,
case when pay_users is null then 0 else cast(pay_users as int) end as install_payer,
case when pay_amount is null then 0.0 else cast(pay_amount as double) end as install_payment,
case when idfa is null then 0 else cast(idfa as int) end as idfa,
case when distinct_idfa is null then 0 else cast (distinct_idfa as int) end as distinct_idfa,
case when ip is null then 0 else cast(ip as int) end as ip,
case when distinct_ip is null then 0 else cast(distinct_ip as int) end as distinct_ip
from 
(select snid,gameid,ds,creative,install,pay_users,pay_amount
from facts.adtrack_install_payment 
where  ds='${statDay}' ${snGameIdsSql}
) a
left outer join 
(select snid,gameid,ds,creative,idfa,distinct_idfa,ip,distinct_ip 
from facts.adtrack_ip_idfa 
where  ds='${statDay}' ${snGameIdsSql}
) b on a.snid= b.snid and a.gameid=b.gameid and a.creative = b.creative