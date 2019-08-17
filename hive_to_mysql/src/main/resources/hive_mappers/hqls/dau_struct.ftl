select a.snid,a.gameid,dau_day,a.ds as stat_day,a.dau,b.struct_day,
		if(a.dau=0,0,round(struct_num/dau,3)) as struct_rate ,b.struct_num
from			
(select ds,snid,gameid,sum( if(retention_uu is NULL,0,retention_uu)) as dau
from etl.a_user_retention where ds='${statDay}' ${snGameIdsSql}
group by ds,snid,gameid
)a
left outer join
( select ds,snid,gameid, install_date as dau_day,datediff(stat_date,install_date) as struct_day,if(retention_uu is NULL,cast(0 as bigint),retention_uu) as struct_num
  from etl.a_user_retention where ds='${statDay}' and datediff(stat_date,install_date)<=90 
  union all
  select ds,snid,gameid,"later90" as dau_day,999 as struct_day,sum(if(retention_uu is NULL,0,retention_uu)) as struct_num
  from etl.a_user_retention where ds='${statDay}' and datediff(stat_date,install_date)>90
  group by ds,snid,gameid,"later90",999
 ) b
 on a.ds=b.ds and a.snid=b.snid and a.gameid=b.gameid