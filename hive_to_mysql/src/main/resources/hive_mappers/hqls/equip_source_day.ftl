select snid,gameid,ds,source,
case when dau is null then 0 else cast(dau as int) end as dau,
case when new_equip is null then 0 else cast(new_equip as int) end as new_equip,
case when install is null then 0 else cast(install as int) end as install,
case when uninstall is null then 0 else cast(uninstall as int) end as uninstall,
case when pay_equip is null then 0 else cast(pay_equip as int) end as pay_equip,
case when new_pay_equip is null then 0 else cast(new_pay_equip as int) end as new_pay_equip,
case when pay_amount is null then 0.0 else cast(pay_amount as double) end as pay_amount,
case when new_pay_amount is null then 0.0 else cast(new_pay_amount as double) end as new_pay_amount,
case when install_pay_amount is null then 0.0 else cast(install_pay_amount as double) end as install_pay_amount
from facts.f_equip_source_day where ds='${statDay}' ${snGameIdsSql}
