select a.snid,a.gameid, install_date as install_day,
ds as stat_day,date_diff as retention_day,dlfrom as source,sum(install) as install,
sum(retention_uu) as retention_num,
round(sum(retention_uu)/sum(install),4) as retention_rate,
round(sum(total_amount),2) as total_amount
from
(select snid,gameid,install_date,
if(split(install_dlfrom,"\\.")[1] is null,  
								if(split(install_dlfrom,"-")[1] is null,
											install_dlfrom,	
											concat(split(install_dlfrom,"-")[0],'-',split(install_dlfrom,"-")[1])),
								concat(split(install_dlfrom,"\\.")[0],'.',split(install_dlfrom,"\\.")[1])) as dlfrom,	
	date_diff,install,
	retention_uu,total_amount,ds 
	from etl.a_user_retention_source_ltv
	where  ds='${statDay}' and 
	(date_diff<=90) ${snGameIdsSql}
)a
group by a.snid,a.gameid,install_date,ds,date_diff,dlfrom