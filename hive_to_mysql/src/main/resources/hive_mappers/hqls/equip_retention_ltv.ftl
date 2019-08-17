select snid,gameid,ds,
datediff(dau_date,install_date) as retention_day,
case when new_equip is null then 0 else cast(new_equip as int) end as new_equip,
case when install_equip is null then 0 else cast(install_equip as int) end as install_equip,
install_date,
case when retention_equip is null then 0 else cast(retention_equip as int) end as retention_equip,
case when total_payment is null then 0 else cast(total_payment as int) end as total_payment
from facts.equip_retention_ltv  where ds='${statDay}' ${snGameIdsSql}