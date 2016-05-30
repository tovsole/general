select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.nameu||':'||t.namer,'-πασσ.',''),'.',''),'-πασ',''),'-2',''),'2',''),'-πασσ',''),'-πομεσσλιε',''),'-ημαχξαρ',''),'-ηομοχξα',''),'-ξψΰηατι',''),'-ξοςδ',''),'-χσθοδξρ',''),' ϊαθοδξρ',''),'ϊαχοδ',''),'-ημαχ',''),'-ηομοχ',''),'ημαχξωκ',''),'-ηομ',''),'-1',''),'-2',''),' - σεχεςξαρ πμ',''),'- π?χξ?ώξα πμ',''),' πασ',''),' ΰϊ',''),' πϊ',''),' χσθοδξρ',''),'νταςασα','ν. ταςασα'),'-ηςυϊοχ',''),'-χαξταφ',''),'-ΰφξαρ',''),'-π?χδ',''),'-υϊμοχοκ',''),'-χυϊμ',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st1
union
select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.nameu||':'||t.namer,'-πασσ.',''),'.',''),'-πασ',''),'-2',''),'2',''),'-πασσ',''),'-πομεσσλιε',''),'-ημαχξαρ',''),'-ηομοχξα',''),'-ξψΰηατι',''),'-ξοςδ',''),'-χσθοδξρ',''),' ϊαθοδξρ',''),'ϊαχοδ',''),'-ημαχ',''),'-ηομοχ',''),'ημαχξωκ',''),'-ηομ',''),'-1',''),'-2',''),' - σεχεςξαρ πμ',''),'- π?χξ?ώξα πμ',''),' πασ',''),' ΰϊ',''),' πϊ',''),' χσθοδξρ',''),'νταςασα','ν. ταςασα'),'-ηςυϊοχ',''),'-χαξταφ',''),'-ΰφξαρ',''),'-π?χδ',''),'-υϊμοχοκ',''),'-χυϊμ',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st2
order by 1