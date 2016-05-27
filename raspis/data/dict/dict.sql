select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.namer||':'||t.nameu,'-оюяя.',''),'.',''),'-оюя',''),'-2',''),'2',''),'-оюяя',''),'-онкеяяйхе',''),'-цкюбмюъ',''),'-цнкнбмю',''),'-мэчцюрх',''),'-мнпд',''),'-бяундмъ',''),' гюундмъ',''),'гюбнд',''),'-цкюб',''),'-цнкнб',''),'цкюбмши',''),'-цнк',''),'-1',''),'-2',''),' - яебепмюъ ок',''),'- о╡бм╡вмю ок',''),' оюя',''),' чг',''),' ог',''),' бяундмъ',''),'лрюпюяю','л. рюпюяю'),'-цпсгнб',''),'-бюмрюф',''),'-чфмюъ',''),'-о╡бд',''),'-сгкнбни',''),'-бсгк',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st1
union
select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.namer||':'||t.nameu,'-оюяя.',''),'.',''),'-оюя',''),'-2',''),'2',''),'-оюяя',''),'-онкеяяйхе',''),'-цкюбмюъ',''),'-цнкнбмю',''),'-мэчцюрх',''),'-мнпд',''),'-бяундмъ',''),' гюундмъ',''),'гюбнд',''),'-цкюб',''),'-цнкнб',''),'цкюбмши',''),'-цнк',''),'-1',''),'-2',''),' - яебепмюъ ок',''),'- о╡бм╡вмю ок',''),' оюя',''),' чг',''),' ог',''),' бяундмъ',''),'лрюпюяю','л. рюпюяю'),'-цпсгнб',''),'-бюмрюф',''),'-чфмюъ',''),'-о╡бд',''),'-сгкнбни',''),'-бсгк',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st2
order by 1