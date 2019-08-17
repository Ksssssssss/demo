select snid,gameid,ds,version,
case when dau is null then 0 else cast(dau as int) end as dau
from facts.equip_version_dau
where ds='${statDay}' ${snGameIdsSql}