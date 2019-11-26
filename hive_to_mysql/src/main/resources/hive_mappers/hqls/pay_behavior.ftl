select snid,gameid	,
ds,
userid,
amount,
paymenttime
from etl_dailyreport.cleaned_payment where ds='${statDay}' ${snGameIdsSql}