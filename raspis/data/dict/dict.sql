select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.namer||':'||t.nameu,'-����.',''),'.',''),'-���',''),'-2',''),'2',''),'-����',''),'-���������',''),'-�������',''),'-�������',''),'-�������',''),'-����',''),'-�������',''),' �������',''),'�����',''),'-����',''),'-�����',''),'�������',''),'-���',''),'-1',''),'-2',''),' - �������� ��',''),'- ϲ�Ͳ��� ��',''),' ���',''),' ��',''),' ��',''),' �������',''),'�������','�. ������'),'-������',''),'-������',''),'-�����',''),'-ϲ��',''),'-�������',''),'-����',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st1
union
select distinct  
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(t.namer||':'||t.nameu,'-����.',''),'.',''),'-���',''),'-2',''),'2',''),'-����',''),'-���������',''),'-�������',''),'-�������',''),'-�������',''),'-����',''),'-�������',''),' �������',''),'�����',''),'-����',''),'-�����',''),'�������',''),'-���',''),'-1',''),'-2',''),' - �������� ��',''),'- ϲ�Ͳ��� ��',''),' ���',''),' ��',''),' ��',''),' �������',''),'�������','�. ������'),'-������',''),'-������',''),'-�����',''),'-ϲ��',''),'-�������',''),'-����',''),' 1','') as str
from stations t,trains_prod tt
where t.st1=tt.st2
order by 1