select snid,gameid,ds,phylum,classfield,family,genus,users,time as times,num  as item_num,amount from facts.economy_expend_noclient 
where ds='${statDay}' ${snGameIdsSql}