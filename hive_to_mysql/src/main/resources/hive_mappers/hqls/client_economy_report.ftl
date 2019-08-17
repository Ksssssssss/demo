select snid,gameid,clientid,ds,phylum,classfield,family,genus,users,time as times,num as item_num,amount from facts.economy_expend
where ds='${statDay}' ${snGameIdsSql}