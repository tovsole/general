create or replace package trainparser_sk is

    c_np            number;
    c_start_date    date := to_date('12.12.2015','dd.mm.yyyy');--to_date('01.06.2014','dd.mm.yyyy');
    c_stop_date     date := to_date('11.12.2016','dd.mm.yyyy');--to_date('31.05.2015','dd.mm.yyyy');
  -- Author  : SERGEY
  -- Created : 23.12.2014 9:46:41

  -- ��������� ������ ���������� ������
    function strtoschedule(
        p_np        number
        ) return varchar2;


      -- ��������� ������ ���������� ����� �� ����� 2
   procedure Tester3 (
      p_str                varchar2                   -- ������ �����������
   );


  procedure parse_all;
  
end trainparser_sk;
/
create or replace package body trainparser_sk is

    -- ������ �� ���� ��� ��� ������
    function replace_day(
        p_str   varchar2
    ) return varchar2 as
        l_str   varchar2(500);
        l_n     number;
        l_instr number;
    begin
        if p_str = '-2' then
            return -2;
        end if;
        l_str   :=  p_str;
        for i in (
        select '������������'   as zn   , '7' as d from dual union all
        select '�����������'   as zn   , '7' as d from dual union all
        select '�����������'   as zn   , '7' as d from dual union all
        select '��Ĳ���'   as zn   , '7' as d from dual union all
        select '��������'              , '6' from dual union all
        select '������'              , '6' from dual union all
        select '��������'              , '5' from dual union all
        select '�"�������'              , '5' from dual union all
        select '������'              , '5' from dual union all
        select '���������'              , '4' from dual union all
        select '��������'              , '4' from dual union all
        select '���������'              , '4' from dual union all
        select '������'                 , '3' from dual union all
        select '�����'                 , '3' from dual union all
        select '����'                 , '3' from dual union all
        select '���������'              , '2' from dual union all
        select '��������'              , '2' from dual union all
        select '���������'              , '2' from dual union all
        select '�����������'          , '1' as zn from dual union all
        select '�����������'          , '1' as zn from dual
        )
        loop
            if instr(l_str,i.zn) > 0 then
                l_n := nvl(l_n,0) + 1;
                if instr(l_str,'#0') = 0 then
                    l_str := substr(l_str,1,instr(l_str,i.zn)+length(i.zn))||' #0 '||substr(l_str,instr(l_str,i.zn)+length(i.zn)+2);
                end if;

                if  instr(l_str,i.zn) - instr(l_str,' � ',-1) <=3 then
                    l_str := substr(l_str,1,instr(l_str,' � ',-1))||' , '||substr(l_str,instr(l_str,i.zn));
                end if;

                l_instr := instr(l_str,i.zn);
                l_str := replace(l_str,i.zn,i.d);
                if l_instr > instr(l_str,'#0') then
                    l_str := trim(substr(l_str,1,instr(l_str,'#0') - 1))||
                             ','||i.d||
                             substr(l_str,instr(l_str,'#0'),2)||
                             substr(l_str,l_instr+1);

                end if;
            end if;
        end loop;
        if nvl(l_n,0) > 0 then
          l_str := substr(l_str,1,l_instr-1)||
                   regexp_replace(substr(l_str,l_instr-1,instr(l_str,'#0')),'( )+','')||
                   substr(l_str,instr(l_str,'#0')+2);
        end if;
        dbms_output.put_line(l_str);
        return l_str;
    exception
      when others then
       return -2;
    end;

    -- ������ �� ���� ��� �����
    function replace_mounth(
        p_str   varchar2
    ) return varchar2 as
        l_str   varchar2(500);
        l_var1  varchar2(50);
    begin
        if p_str = '-2' then
            return -2;
        end if;
        l_str   :=  p_str;
        for i in (
        select '������'      as mn , ' ,1-31/01/' as zn from dual union all
        select '�������'    as mn , ' ,1-28/02/' from dual union all
        select '����'      as mn , ' ,1-31/03/' from dual union all
        select '��������'  as mn , ' ,1-31/03/' from dual union all
        select '������'      as mn , ' ,1-30/04/' from dual union all
        select '�²����'    as mn , ' ,1-30/04/' from dual union all
        select '���'      as mn , ' ,1-31/05/' from dual union all
        select '�������'    as mn , ' ,1-31/05/' from dual union all
        select '����'      as mn , ' ,1-30/06/' from dual union all
        select '�������'    as mn , ' ,1-30/06/' from dual union all
        select '����'      as mn , ' ,1-31/07/' from dual union all
        select '������'      as mn , ' ,1-31/07/' from dual union all
        select '������'      as mn , ' ,1-31/08/' from dual union all
        select '�������'    as mn , ' ,1-31/08/' from dual union all
        select '��������'  as mn , ' ,1-30/09/' from dual union all
        select '��������'  as mn , ' ,1-30/09/' from dual union all
        select '�������'  as mn , ' ,1-31/10/' from dual union all
        select '�������'  as mn , ' ,1-31/10/' from dual union all
        select '������'      as mn , ' ,1-30/11/' from dual union all
        select '��������'   as mn , ' ,1-30/11/' from dual union all
        select '�������'  as mn , ' ,1-31/12/' from dual union all
        select '�������'  as mn , ' ,1-31/12/' from dual union all
        --
        select '������'      as mn , ' ,1-31/01/' from dual union all
        select '�������'    as mn , ' ,1-28/02/' from dual union all
        select '�����'      as mn , ' ,1-31/03/' from dual union all
        select '������'      as mn , ' ,1-30/04/' from dual union all
        select '���'      as mn , ' ,1-31/05/' from dual union all
        select '�������'  as mn , ' ,1-31/10/' from dual union all
        select '������'      as mn , ' ,1-30/11/' from dual union all
        select '�������'  as mn , ' ,1-31/12/' from dual union all
        --
        select '������'      as mn , ' /01/' from dual union all
        select '�������'  as mn , ' /02/' from dual union all
        select '�����'      as mn , ' /03/' from dual union all
        select '������'      as mn , ' /04/' from dual union all
        select '���'      as mn , ' /05/' from dual union all
        select '����'      as mn , ' /06/' from dual union all
        select '����'      as mn , ' /07/' from dual union all
        select '�������'  as mn , ' /08/' from dual union all
        select '��������'  as mn , ' /09/' from dual union all
        select '�������'  as mn , ' /10/' from dual union all
        select '������'      as mn , ' /11/' from dual union all
        select '�������'  as mn , ' /12/' from dual)
        loop
            if instr(l_str,i.mn) > 0 then
                l_var1 := substr(l_str,regexp_instr(l_str,'[0-9]{4}',instr(l_str,i.mn)),4);
                l_str := regexp_replace(l_str,'( *'||i.mn||'[, ]*)',i.zn||l_var1);
                l_str := regexp_replace(l_str,'('||l_var1||')+',l_var1);
            end if;
        end loop;
        return l_str;
    exception
      when others then
       return -2;
    end;

    -- �������� ������� �� ��������
    function replace_lex(
        p_str varchar2
        ) return varchar2 as

        l_str       varchar2(500);
        --l_str2      varchar2(500);
        l_str_w2    varchar2(500);
        l_str_w3    varchar2(500);
        l_str_d1    varchar2(500);
        l_cnt_s     number;
        l_d         varchar2(500);

        --l_length    number;

    begin
        if p_str = '-2' then
            return -2;
        end if;
        -- �������� � ������� �������
        l_str := replace(replace(upper(p_str)
                        ,'�������������')
                        ,'���������');
        --����� ��������������� �������
        l_str := replace_day(l_str);
        l_str := replace_mounth(l_str);
        --

        l_str :=    replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
                    replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
                    replace(replace(replace(replace(replace(replace(replace(replace(
                upper(l_str)
                      ,'��� ���� ��������','#3, ')
                      ,'��� ���� ��������','#3, ')
                      ,'�������','#8 ')
                      ,'²�̲����','#8 ')
                      ,'�����','#8 ')
                      ,'���������','#9 ')
                      ,'�������','#9 ')
                      ,'�������������','#9 ')
                      ,'�� ��������','#* ')
                      ,'�� ��������','#* ')
                      ,'�� ������','#+ ')
                      ,'��������','#* ')
                      ,'��������','#* ')
                      ,'������','#+ ' )
                      ,'�� ������','#+ ' )
                      ,'�� ���� ������','#0 ')
                      ,'���� ������','#0 ')
                      ,'��','')
                      ,'��������','#* ')
                      ,'��','#4 ')
                      ,'�������������')
                      ,'���������')
                      ,'����������')
                      ,'�','#1 ')
                      ,' � ','#1 ')
                      ,'� ','#1 ')
                      ,' C ','#1 ')
                      ,' CO ','#1 ')
                      ,'�� ','#1 ')
                      ,'C�','#1 ')
                      ,'C ','#1 ')
                      ,':',', ');



        --������� �� ���� ������
        l_cnt_s := length(l_str) - length(replace(l_str,'#'));
        if instr(l_str,'#0') > 0 then
           loop
                if instr(l_str,'#0') > 0 and substr(l_str,instr(l_str,'#0')-1,1) = ' ' then
                   l_str := substr(l_str,1,instr(l_str,'#0')-2)||substr(l_str,instr(l_str,'#0'));
                else
                exit;
                end if;
           end loop;
           l_str := substr(l_str,1,instr(substr(l_str,1,instr(l_str,'#0')),' ',-1))||'#0'
                       ||replace(substr(l_str,instr(substr(l_str,1,instr(l_str,'#0')),' ',-1)+1,instr(l_str,'#0')-instr(substr(l_str,1,instr(l_str,'#0')),' ',-1)-1),',')
                       ||substr(l_str,instr(l_str,'#0')+2,length(l_str));
        end if;
        -- ������� (��� ���� ��������)
        if instr(l_str,'#3') > 0 then
            l_str := regexp_replace(l_str,'( )+','',instr(l_str,'#3'));
            l_str := regexp_replace(l_str,'(,)','d',instr(l_str,'#3'));
        end if;

        -- ���� ���� ������� ���������� �� ���� � ����� ������
        if l_cnt_s > 0 then
            loop
                l_str := l_str||' ';
                l_str := trim(substr(l_str,1,instr(l_str,'#')-1)||
                            substr(l_str,instr(l_str,' ',instr(l_str,'#')),length(l_str))||' '||
                            substr(l_str,instr(l_str,'#'),instr(l_str,' ',instr(l_str,'#')) - instr(l_str,'#')));

                l_cnt_s := l_cnt_s - 1;
                if l_cnt_s = 0 then exit; end if;
            end loop;
        end if;
        -- ������ �� ������ ��������
        l_str := regexp_replace(l_str,'( )+','');
        l_str := regexp_replace(l_str,'(,)+',',');
        l_str := replace(l_str,';',' ');
        l_str := replace(l_str,'#',' #');
        l_str := regexp_replace(l_str,'(2014)+','2014');
        l_str := regexp_replace(l_str,'(2015)+','2015');

        -- �������� �� ������� ������ ��� �����
        if regexp_instr(l_str,'[, ]/[0-9]{2}') > 0 then
            l_str := substr(l_str,1,regexp_instr(l_str,'[, ]/[0-9]{2}'))
                        ||' 1-31'||
                    ltrim(substr(l_str,regexp_instr(l_str,'[, ]/[0-9]{2}')+1));
        end if;

        -- ��������� ������� ������ ������ ������ �����
        if regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]') > 0 then
        loop
            l_str_w2 := substr(l_str,1,instr(l_str,'/',regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]')+1) + 2);
            l_str_w3 := substr(l_str,instr(l_str,'/',regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]')+3));
            l_str_d1 := ', '||substr(
                                substr(l_str,1,instr(l_str,'/',regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]'))-1)
                                ,instr(substr(l_str,1,instr(l_str,'/',regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]'))),' ',-1)
                                );
            l_d := substr(l_str_d1,instr(l_str_d1,'-')+1);
            /*if to_number(l_d) > to_number(to_char(last_day(to_date('01'||substr(l_str_w3,1,3)||regexp_substr(l_str_w3,'[0-9]{4}'),'dd/mm/yyyy')),'dd')) then
                l_str_d1 := substr(l_str_d1,1,instr(l_str_d1,'-'))
                            ||to_char(last_day(to_date('01'||substr(l_str_w3,1,3)||regexp_substr(l_str_w3,'[0-9]{4}'),'dd/mm/yyyy')),'dd');
            end if;*/
            l_str := l_str_w2||l_str_d1||l_str_w3;
        if regexp_instr(l_str,'[0-9]/[0-9]{2}/[0-9]{2}[,/ ]') = 0 then exit; end if;
        end loop;
        end if;
        -- ��������������� ������
        l_cnt_s := regexp_instr(' '||l_str,'[, -][0-9]{2}/');
        l_str := replace(l_str,'�');
        -- �������� ������������ ����� ������
        if regexp_instr(l_str,'[, -][0-9]{2}/') > 0 then
            loop
                l_cnt_s := case when l_cnt_s <= 1 then 2 else l_cnt_s end;
                l_d := substr(l_str,regexp_instr(' '||l_str,'[, -][0-9]{2}/',l_cnt_s - 1),2);
                if to_number(l_d) > to_number(to_char(last_day(to_date('01'||
                                    substr(l_str,regexp_instr(' '||l_str,'[, -][0-9]{2}/',l_cnt_s)+3,2)
                                    ||
                                    regexp_substr(l_str,'[0-9]{4}',l_cnt_s),'dd/mm/yyyy')),'dd')) then

                    l_str := substr(l_str,1,l_cnt_s-1)||
                             to_number(to_char(last_day(to_date('01'||
                                    substr(l_str,regexp_instr(' '||l_str,'[, -][0-9]{2}/',l_cnt_s)+3,2)
                                    ||
                                    regexp_substr(l_str,'[0-9]{4}',l_cnt_s),'dd/mm/yyyy')),'dd'))
                             ||substr(l_str,l_cnt_s+2);
                end if;
                l_cnt_s := regexp_instr(' '||l_str,'[, -][0-9]{2}/',l_cnt_s+1);
            if l_cnt_s = 0 then
                exit;
            end if;
            end loop;
        end if;
        --
        if regexp_instr(l_str,'[0-9]') > 1 and regexp_instr(l_str,'[#;]') > 1
            and regexp_instr(l_str,'[0-9]') < instr(l_str,'#')
            then
            l_str := substr(l_str,regexp_instr(l_str,'[0-9]'));
        end if;
        --
        --������� ��������� ������� ��� ��� �� �����
        if instr(l_str,',',-1) > 0 then
          if regexp_instr(l_str,'[0-9]{1}/[0-9]{2}/[0-9]{4}',instr(l_str,',',-1)) = 0 then
              l_str := substr(l_str,1,instr(l_str,',',-1)-1)||substr(l_str,instr(l_str,',',-1)+1);
          end if;
        end if;
    return l_str;

    exception
      when others then
       dbms_output.put_line(sqlerrm);
       return -2;
    end;



    -- ��������� ��������� ���������� � ���� 01
    procedure pars_data2 (
        p_str           varchar2,
        p_np            number,
        p_result in out varchar2,
        p_lex1 in out   date
        ) as

        l_case          varchar2(32000);
        l_case_buff     varchar2(32000);
        l_case_buff2     varchar2(32000);
        l_str   varchar2(500);

        l_i_str varchar2(100);

        l_exe   varchar2(32000);

        l_lex0  varchar2(10);
        l_lex2  number;
        l_lex3  number;
        l_lexm  number;
        l_lexp  number;
        l_lex9  number;
        l_lex8  number;

        l_cnt_d number;

    begin
        if p_str = '-2' then
            p_result :=  -2;
            goto M1;
        end if;
        -- ��������� ������� ���
        if regexp_instr(p_str,'[0-9]{2}/[0-9]{2}/[0-9]{4}') >= 1 or regexp_instr(p_str,'[0-9]{1}/[0-9]{2}/[0-9]{4}') >=1 then
            l_cnt_d := 1;
            -- ������ �� �����
            for i in (
            with res as (SELECT p_str as str FROM dual)
                ,res2 as (select trim(regexp_substr(str, '[^,/ -]+', 1, level)) as str
                                 ,lag(trim(regexp_substr(str, '[^[0-9 a-z A-Z]', 1, level))) over (order by rownum) as zn
                                 ,trim(regexp_substr(str, '[^[0-9 a-z A-Z]', 1, level)) as zn1
                                 ,length(trim(regexp_substr(str, '[^,/ -]+', 1, level))) as leng
                                 ,rownum as rn
                                 ,case when length(trim(regexp_substr(str, '[^,/ -]+', 1, level))) = 4 then 1 else 0 end yr
                              from res
                              CONNECT BY regexp_instr(str, '[^,/ -]+', 1, level) > 0)
                ,res3 as (select r.*
                          ,lpad(
                           case when case when rn = 1 then zn1 else zn end in (',','-') and leng <= 2 or rn = 1 then
                              str||'.'||(select max(str) keep (dense_rank first order by rn) from res2 r1 where r1.rn > r.rn and yr <> 1 and zn = '/')
                              ||'.'||(select max(str) keep (dense_rank first order by rn) from res2 r1 where r1.rn > r.rn and yr = 1 and zn = '/')
                           end,10
                           ,'0') as res1
                           ,case when case when rn = 1 then zn1 else zn end in (',','-') and leng <= 2 then
                                      case when rn = 1 then zn1 else zn end
                            end as res1_tp
                          from res2 r)
            select r3.*
                  ,case when lead(res1_tp) over (order by rn) = '-' then
                        ' case when dt between to_date('''||res1||''',''dd.mm.yyyy'') and to_date('''||lead(res1) over (order by rn)||''',''dd.mm.yyyy'') then 1 else 0 end '
                        when nvl(lead(res1_tp) over (order by rn),',') <> '-' and res1_tp = ',' then
                        ' case when dt = to_date('''||res1||''',''dd.mm.yyyy'') then 1 else 0 end '
                        when nvl(res1_tp,'0') = '0' then
                        ' case when dt = to_date('''||res1||''',''dd.mm.yyyy'') then 1 else 0 end '
                   end as result1
                from res3 r3
                where res1 is not null)
            loop
                if trim(i.result1) is not null then
                    l_case := l_case||i.result1||' + ';
                end if;
            end loop;
            l_case := substr(l_case,1,length(l_case)-2);
        end if;
        -- ������ �� ��������
        for i in (
        with res as (SELECT p_str as str FROM dual)
            ,res2 as (select trim(regexp_substr(str, '[^,/ -]+', 1, level)) as str
                             ,lag(trim(regexp_substr(str, '[^[0-9 a-z A-Z]', 1, level))) over (order by rownum) as zn
                             ,trim(regexp_substr(str, '[^[0-9 a-z A-Z]', 1, level)) as zn1
                             ,length(trim(regexp_substr(str, '[^,/ -]+', 1, level))) as leng
                             ,rownum as rn
                             ,case when length(trim(regexp_substr(str, '[^,/ -]+', 1, level))) = 4 then 1 else 0 end yr
                          from res
                          CONNECT BY regexp_instr(str, '[^,/ -]+', 1, level) > 0)
            ,res3 as (select r.*
                      ,lpad(
                       case when case when rn = 1 then zn1 else zn end in (',','-') and leng <= 2 or rn = 1 then
                          str||'.'||(select max(str) keep (dense_rank first order by rn) from res2 r1 where r1.rn > r.rn and yr <> 1 and zn = '/')
                          ||'.'||(select max(str) keep (dense_rank first order by rn) from res2 r1 where r1.rn > r.rn and yr = 1 and zn = '/')
                       end,10
                       ,'0') as res1
                       ,case when case when rn = 1 then zn1 else zn end in (',','-') and leng <= 2 then
                                  case when rn = 1 then zn1 else zn end
                        end as res1_tp
                      from res2 r)
        select r3.*
            from res3 r3
            where str like '%#%')
        loop
            if i.str = '#1' then
                l_case := replace(l_case,'=','>=');
                p_lex1 := to_date(substr(l_case,regexp_instr(l_case,'[0-9]{2}.[0-9]{2}.[0-9]{4}'),10),'DD.MM.YYYY');
            elsif i.str = '#8' then
                l_case := replace(nvl(l_case,9),'then 1 else 0 end','then 9 else 0 end');
                l_lex8 := 1;
            elsif i.str like '#3%' then
                l_lex3 := 1;
                l_i_str := replace(i.str,'d',' ');
            elsif i.str = '#4' then
                l_case := replace(l_case,'=','<=');
            elsif i.str = '#*' then
                l_lexm := 1;
            elsif i.str = '#+' then
                l_lexp := 1;
            elsif i.str = '#2' then
                l_lex2 := 1;
            elsif i.str like '#0%' then
                l_lex0 := i.str;
            elsif i.str like '#9%' then
                l_lex9 := 1;
            end if;
            dbms_output.put_line(i.str);
        end loop;

        if length(l_lex0) > 1  and nvl(l_lex8,0) <> 1 then
            l_case := ' case when '||nvl(l_case,1)||' >= 1 and regexp_instr('''||l_lex0||''',''[''||to_char(dt,''d'')||'']'') > 0 then '||case when l_case = '9' then 9 else 1 end||' else 0 end ';
        elsif length(l_lex0) > 1  and nvl(l_lex8,0) = 1 then
            l_case := ' case when '||nvl(l_case,0)||' >= 1 and regexp_instr('''||l_lex0||''',''[''||to_char(dt,''d'')||'']'') > 0 then '||case when l_case = '9' then 9 else 1 end||' else 0 end ';
        end if;


        if nvl(l_lex9,0) = 1 then
            if trim(nvl(l_case,'9')) = '9' then
                l_case := ' case when dt between to_date('''||to_char(c_start_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') - 5 and to_date('''||to_char(c_stop_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') + 5 then 1 else 0 end ';
            else
                null;
            end if;
        end if;

        if nvl(l_lex3,0) = 1 then
            l_case_buff2 := l_case;
        end if;

        if nvl(l_lexm,0) = 1 then
            l_case := ' case when '||nvl(l_case,0)||' >= 1 and mod(to_char(dt,''dd''),2) = 1 then 1 else 0 end ';
        elsif nvl(l_lexp,0) = 1 then
            l_case := ' case when '||nvl(l_case,0)||' >= 1 and mod(to_char(dt,''dd''),2) = 0 then 1 else 0 end ';
        end if;

        if nvl(l_lex3,0) = 1 then
            if l_cnt_d = 1 then
                l_case_buff := l_case;
                l_case := 'case when'||chr(13)||'case when'||chr(13)||l_case_buff2||
                           ' > 0 then '||chr(13);
            end if;
            l_case := l_case||chr(13)||
                        'case when '||chr(13);


            for i in (select *
                        from (select
                                  case when mod(lag(to_char(dt,'dd')) over (order by dt),2) = mod(to_char(dt,'dd'),2) then dt - 5 else null end as dt1,
                                   case when mod(lag(to_char(dt,'dd')) over (order by dt),2) = mod(to_char(dt,'dd'),2) then dt + 5 else null end as dt2
                             from (
                                select c_start_date - 5 + level - 1 as dt
                                    from dual
                                    connect by level <= c_stop_date - c_start_date + 1 + 10)
                             order by dt)
                        where dt1 is not null)
            loop
                l_case := l_case||chr(13)||' case when dt between to_date('''||to_char(i.dt1,'dd.mm.yyyy')||''',''dd.mm.yyyy'') and to_date('''||to_char(i.dt2,'dd.mm.yyyy')||''',''dd.mm.yyyy'') then 1 else 0 end +';
            end loop;
            l_case := substr(l_case,1,length(l_case)-1)||' > 0 then '||chr(13);
            for i in (with res as (SELECT l_i_str as str FROM dual )
                           ,res2 as (select regexp_substr(str, '[^ ]+', 1, level) as tt
                                      from res
                                      CONNECT BY regexp_instr(str, '[^ ]+', 1, level) > 0)
                      select *
                          from res2
                          where tt not in ('#31','#32'))
            loop
                l_case := l_case||chr(13)||'case when nvl(regexp_instr('''||lpad(i.tt,2,'0')||''',''''||to_char(dt,''dd'')||''''),0) > 0 then 3 else 0 end+';
            end loop;
            l_case := substr(l_case,1,length(l_case)-1)||chr(13)||
                      ' else 0 end ';
            if l_cnt_d = 1 then
                l_case := l_case||' else 0 end '||chr(13);
                l_case := l_case|| '> 0 then 3 else
                                    case when
                                            '||l_case_buff||' > 0
                                    then 1 else 0
                                    end end';
            end if;
        end if;


        --������� ������ ��� �������
        if p_np = 50791 then
            l_case := replace(l_case,'>= 1','>= 2');
        end if;
        /*if p_np = 50538 then
            l_case := replace(l_case,'0 >= 1','1 >= 1');
        end if;*/
        --
        l_exe := '
        select replace(wm_concat(
        --****

        '||trim(nvl(l_case,0))||'

        --****
        ),'','','''') as res
        from (
            select to_date('''||to_char(c_start_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') - 5 + level - 1 as dt
            from dual
            connect by level <= to_date('''||to_char(c_stop_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') - to_date('''||to_char(c_start_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') + 1 + 10)
            order by dt';
        dbms_output.put_line(l_exe);
        execute immediate l_exe into l_str;

        if instr(l_str,4) > 0 or instr(l_str,3) > 0 then
            dbms_output.put_line(l_str);
            l_str := replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
                    l_str
                    ,'40110104','10000001')
                    ,'40010104','10000001')
                    ,'14','01')
                    ,'41','10')
                    ,'141','010')
                    ,'4','1')
                    ,'30110103','10000001')
                    ,'30010103','10000001')
                    ,'131','010')
                    ,'13','01')
                    ,'31','10')
                    ,'3','1')
                    ;
        end if;
            --dbms_output.put_line(l_str||'test_2');
        p_result := l_str;
        <<M1>>
        null;
    exception
      when others then
       dbms_output.put_line(sqlerrm);   
       p_result := -2;
    end;

    -- ��������� ��������� ���������� � ���� 01
    function pars_data (
        p_str   varchar2,
        p_np    number
        ) return varchar2 as

        l_cnt   number;
        l_str   varchar2(500);
        l_poz   number;

        l_str_w1  varchar2(500);
        l_str_w2  varchar2(500);

        l_lex1    date;
        l_exe     varchar2(32000);


    begin
        if p_str = '-2' then
            return -2;
        end if;
        l_cnt := length(p_str) - length(replace(p_str,';',''));
        if instr(trim(p_str),';',-1) = length(trim(p_str)) then
            l_cnt := l_cnt - 1;
        end if;
        l_poz := 1;
        if l_cnt = 0 then
            -- ������������ ������ ��� ����
            l_str := replace_lex(p_str);
            l_str := replace(l_str,';');
            dbms_output.put_line(l_str);
            pars_data2(l_str,p_np,l_str_w2,l_lex1);
        else
            l_cnt := l_cnt + 1;
            for i in 1..l_cnt
            loop
                l_str := substr(p_str,l_poz
                                ,case when instr(p_str,';',l_poz+1) = 0
                                        then length(p_str)
                                        else instr(p_str,';',l_poz+1) - l_poz
                                 end);

                dbms_output.put_line(l_str);
                l_poz := instr(p_str,';',l_poz+1);
                if l_str_w1 is null then
                    l_str := replace(l_str,';');
                    -- ������������ ������ ��� ����
                    l_str := replace_lex(l_str);
                    if regexp_instr(l_str,'[0-9]') < instr(l_str,'#') then
                        l_str := substr(l_str,regexp_instr(l_str,'[0-9]'));
                    end if;
                    dbms_output.put_line(l_str);
                    l_lex1 := null;
                    pars_data2(l_str,p_np,l_str_w1,l_lex1);
                    dbms_output.put_line(l_str_w1);
                else
                    if l_str_w2 is null then
                        l_str_w2 := l_str_w1;
                        -- ������������ ������ ��� ����
                        l_str := replace_lex(l_str);
                        dbms_output.put_line(l_str);
                        l_str := replace(l_str,';');
                        if substr(l_str,regexp_instr(l_str,'[0-9]')) < substr(l_str,regexp_instr(l_str,'[#]')) then
                            l_str := substr(l_str,regexp_instr(l_str,'[0-9]'));
                        end if;
                        l_lex1 := null;
                        pars_data2(l_str,p_np,l_str_w1,l_lex1);
                        dbms_output.put_line(l_str_w1);
                    end if;
                end if;
                if l_str_w2 is not null and l_str_w1 is not null then
                    if l_lex1 is not null and i > 2 then
                        l_exe := '
                            select replace(wm_concat(
                            --****

                            case when dt >= :dt then 1 else 0 end

                            --****
                            ),'','','''') as res
                            from (
                                select to_date(''12.12.2015'',''dd.mm.yyyy'') - 5 + level - 1 as dt
                                from dual
                                connect by level <= to_date('''||to_char(c_stop_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') - to_date('''||to_char(c_start_date,'dd.mm.yyyy')||''',''dd.mm.yyyy'') + 1 + 10)
                                order by dt';
                            dbms_output.put_line(l_exe);
                            execute immediate l_exe into l_str using l_lex1;
                            dbms_output.put_line(l_str);
                            dbms_output.put_line(l_str_w2);
                            for i in 1..length(l_str_w2)
                            loop
                                if substr(l_str,i,1) = 1 then
                                   l_str_w2 := regexp_replace(l_str_w2,'.',0, 1, i);
                                end if;
                            end loop;
                            dbms_output.put_line(l_str_w2);
                    end if;
                    dbms_output.put_line('****');
                    dbms_output.put_line(l_str_w1);
                    dbms_output.put_line(l_str_w2);
                    dbms_output.put_line('****');
                    for i in 1..length(l_str_w2)
                    loop
                        if substr(l_str_w2,i,1) <> 0 or substr(l_str_w1,i,1) <> 0 then
                           l_str_w2 := regexp_replace(l_str_w2,'.',case when substr(l_str_w1,i,1)+substr(l_str_w2,i,1) >= 9 then 0 else substr(l_str_w1,i,1)+substr(l_str_w2,i,1) end, i, 1);
                        end if;
                    end loop;
                    l_str_w1 := null;
                end if;
            end loop;
        end if;
        return l_str_w2;
    exception
      when others then
       return -2;
    end;

     -- ��������� ������ ���������� ����� �� ����� 2
   procedure Tester3 (
      p_str                varchar2                   -- ������ �����������
   )
   is
      l_str                varchar2(366) := p_str;
      tmp                  varchar2(366);
      --l_days               number;
      l_start              date := c_start_date;
      --l_stop               date := last_day(l_start);
      l_day_col            number := c_start_date - trunc(c_start_date,'mm');
      l_buff               number;
   begin
      dbms_output.put_line(chr(13)||'SCHEDULE('||length(l_str)||'): ');
      dbms_output.put_line('XX.YYYY: |         |         |         |');

      tmp := substr(to_char((l_start),'DD.MM.YYYY'),4)||': '||trim(lpad(' ',l_day_col + 1,'0'));
      for ii in 0..length(p_str)
         loop
            l_buff := 0;
            -- ������� �����
            if substr(to_char((l_start + ii),'DD.MM.YYYY'),1,2) = '01'
               then if length(tmp) > 0
                        then dbms_output.put_line(tmp);
                    end if;
                    if ii < length(p_str)
                       then tmp := substr(to_char((l_start + ii),'DD.MM.YYYY'),4)||': '||substr(l_str,ii+1,1);
                            l_buff := 1;
                    end if;
               else tmp := tmp || substr(l_str,ii+1,1);
            end if;
         end loop;
      if l_buff = 0 then
        dbms_output.put_line(tmp);
      end if;
      dbms_output.put_line('XX.YYYY: |         |         |         |');
   end;

    -- ��������� ������ ���������� ������
    function strtoschedule(
        p_np        number
        ) return varchar2 as
        l_str       varchar2(500);
        --l_case      varchar2(32000);
        l_str_output    varchar2(500);
    begin
        if p_np in (49226,50502,1165,1170,1173,1191,1207,1208)
        then
            return -2;
        end if;

        EXECUTE IMMEDIATE 'ALTER SESSION SET NLS_DATE_LANGUAGE = RUSSIAN';



        select upper(regexp_replace(/*replace(*/replace(replace(replace(ts.period_r
                ,'(�����',';�����'),'�����',';�����'),')','')
                /*,'������������� �� ��������,','������������� �� ��������;')*/,'(;)+',';'))
                ,ts.period_r
            into l_str,l_str_output
            from trains ts
            where ts.id = p_np;

        dbms_output.put_line('---*---');
        dbms_output.put_line(p_np);
        dbms_output.put_line(l_str);
        --l_str_output := l_str;

        --���������� ��� �������
        --*-
            /*if p_np = 1251 then
                l_str := replace(l_str,':',';2016 ');
            end if;*/

        if p_np = 373 then
           l_str := replace(l_str,'� 24,',';����� 24,');
        end if;
        if p_np = 394 and l_str = '13/12/2015-25/03/2016 ;����� 29/02/2016  �� ��������, ��� ���� �������� 27,29,1; ������������� 4,6,8,10,12,14,16/01/2016' then
            l_str := '13/12/2015-25/03/2016 �� ��������, ��� ���� �������� 27,29,1 ;����� 29/02/2016 ; ������������� 4,6,8,10,12,14,16/01/2016';
        end if;

        if p_np = 1630 and l_str = '27/03-9/12/2016 �� �������� ;����� 29/03, 5/04/2016, ��� ���� �������� 29,1,3' then
            l_str := '27/03-9/12/2016 �� ��������, ��� ���� �������� 29,1,3 ;����� 29/03, 5/04/2016';
        end if;
        if p_np = 1546 and l_str = ' � 27/03/2016 �� ������������, ��������� � ��������' then
            l_str := ' � 27/03/2016 �� ���������, �������� � ������������';
        end if;

        if p_np = 1275 then
            l_str := replace(l_str,';����� 25/01/2016, 29/02/2016 �� ��������, ��� ���� �������� 29,1,3','�� ��������, ��� ���� �������� 29,1,3 ;����� 25/01/2016, 29/02/2016 ');
        end if;
        if p_np = 1276 then
            l_str := replace(l_str,';����� 25/01/2016, 1/03/2016 �� ��������, ��� ���� �������� 31,3,5',' �� ��������, ��� ���� �������� 31,3,5 ;����� 25/01/2016, 1/03/2016');
        end if;

        if p_np = 54126 then
           l_str := replace(l_str,'( ;����� 29/02/2016','')||' ;����� 29/02/2016';
        end if;


        if l_str like '%� � %' then
            l_str := substr(l_str,1,instr(l_str,'� �')-1)||
                     substr(l_str,regexp_instr(l_str,'�� ',instr(l_str,'� �')),regexp_instr(l_str||';',';',instr(l_str,'� �')) - regexp_instr(l_str,'�� ',instr(l_str,'� �')) + 1)||
                     substr(l_str,instr(l_str,'� �'));
        end if;
        if l_str like '%� �� %' then
            l_str := substr(l_str,1,instr(l_str,'� �� ')-1)||
                     substr(l_str,regexp_instr(l_str,'�� ',instr(l_str,'� �� ')),regexp_instr(l_str||';',';',instr(l_str,'� �� ')) - regexp_instr(l_str,'�� ',instr(l_str,'� �� ')) + 1)||
                     substr(l_str,instr(l_str,'� �� '));
        end if;

        if p_np = 311 and l_str = ' � 13/12/2015 ���������, ;����� ��������� � ������ � 22,23,29,30/12/2015, 23,26/03,27/08/2016' then
           l_str := '� 13/12/2015 ���������, ;����� ��������� � ������ ;����� 22,23,29,30/12/2015, 23,26/03,27/08/2016';
        end if;

        if p_np = 1840 and l_str = '29/03-9/12/2016 ���������, ;����� ������ � ����������� � 29/08/2016' then
           l_str := '29/03-9/12/2016 ���������, ;����� ������ � ����������� ;����� 29/08/2016';
        end if;

        if p_np = 1839 and l_str = '27/03-7/12/2016 ���������, ;����� ��������� � ������ � 27/08/2016' then
           l_str := '27/03-7/12/2016 ���������, ;����� ��������� � ������ ;����� 27/08/2016';
        end if;


        if p_np = 373 and l_str = ' � 14/12/2015 ���������, ;����� ������ � ����������� � 24,25,31/12/2015,1/01,25,28/03,29/08/2016' then
           l_str := ' � 14/12/2015 ���������, ;����� ������ � ����������� ;����� 24,25,31/12/2015 ;����� 1/01,25,28/03,29/08/2016';
        end if;

        if p_np = 1384 and l_str = ' 22/12/2015-18/03/2016 �� ������������, ��������� � ��������' then
           l_str := ' 22/12/2015-18/03/2016 �� ���������, �������� � ������������';
        end if;


        if p_np = 1136 then
            l_str := replace(l_str,'12-30/12/2015,','12-30/12/2015 �� ������;');
        end if;

        if p_np = 1276 then
            l_str := replace(l_str,';�����  1/03/2016,',';�����  1/03/2016;');
        end if;

        if p_np = 417 and l_str = '13/12/2015-9/12/2016 �� �������� ;����� 29/02/2016, ��� ���� �������� 29,1,3' then
            l_str := '13/12/2015-9/12/2016 �� �������� ��� ���� �������� 29,1,3 ;����� 29/02/2016';
        end if;

        --l_str := replace(l_str,' � ',' ; ');

            if p_np = 1024 then
                l_str := replace(l_str,'26/05/2016,','26/05/2016;');
            end if;
            if p_np in (387) then
                l_str := replace(l_str,'����� 29/02/2016,','����� 29/02/2016;');
            end if;

            if p_np in (53439) and l_str = ' � 13/12/2015 �� �������� ;����� 29/02/2016, ��� ���� �������� 27,29,1,3;  30/12/2015, 4,6,10/01/2016' then
                l_str := '� 13/12/2015 �� �������� ��� ���� �������� 27,29,1,3; ;����� 29/02/2016;  30/12/2015, 4,6,10/01/2016';
            end if;


            if p_np = 54337 then
                l_str := replace(l_str,'���������, �')||',���������';
            end if;

            if p_np = 53304 then
                l_str := replace(l_str,'��������, �','��������; �');
                l_str := replace(l_str,'������������,') || ', ������������';
                l_str := substr(l_str,1,instr(l_str,';') - 1)|| ', ������������;'||
                         substr(l_str,instr(l_str,';') + 1,length(l_str) - instr(l_str,';')  - 14);
            end if;

            if p_np = 54273 then
                null;
                -- � 25/10/2015 ��  ������������, �������� �  ��������
                l_str := replace(l_str,'�������� �  ��������','�������� � ��������');
                --l_str := replace(l_str,'�',',');
            end if;

            if p_np = 50790 then
                l_str := replace(l_str,':',';');
            end if;

            if p_np in (52966) then
                l_str := substr(l_str,1,instr(l_str,';�����')-1)||
                         substr(l_str,instr(l_str,',',instr(l_str,';�����'))+1)||
                         substr(l_str,instr(l_str,';�����'),instr(l_str,',',instr(l_str,';�����')) - instr(l_str,';�����'))
                        ;
                dbms_output.put_line(l_str);
            end if;
            if p_np in (54350,54313) then
                l_str := substr(l_str,1,instr(l_str,';�����')-1)||
                         substr(l_str,instr(l_str,'5,',instr(l_str,';�����'))+2)||
                         substr(l_str,instr(l_str,';�����'),instr(l_str,'5,',instr(l_str,';�����')) - instr(l_str,';�����')+1)
                        ;
                dbms_output.put_line(l_str);
            end if;
            if p_np in (54233) then
                l_str := replace(l_str,'��,','��;�����');
            end if;

            if p_np = 50791 then
                l_str := substr(l_str,1,instr(l_str,';'))||
                          substr(l_str,1,instr(l_str,':'))||' '||
                          replace(substr(l_str,instr(l_str,';')),';');

            end if;

            if p_np in (52602,52601) then
                l_str := replace(l_str,'��� ����',';��� ����');
            end if;
        --*-

        --����������
        -- ��������� ������ � ���� 01
        if instr(l_str,'������ �����������') > 0 then
            l_str := substr(l_str,1,instr(l_str,'������ �����������') - 1);
        end if;
        if instr(l_str,'���������') > 0 then
            l_str := substr(l_str,1,instr(l_str,'���������') - 1);
        end if;
        l_str := replace(l_str,'��������');
        /*if instr(l_str,'��� ����') > 0 then
           l_str := replace(l_str,'��� ����',';��� ����');
        end if;*/

        if instr(l_str,'�������') > 0 or instr(l_str,'�����') > 0 and instr(l_str,';') = 0 then
           l_str := replace(replace(l_str,'�������',';�������')
                                         ,'�����',';�����');
           l_str := regexp_replace(l_str,'(;)+',';');
           if instr(l_str,';�������') > 0 and
                instr(l_str,';�������') - instr(substr(l_str,1,instr(l_str,';�������')),',',-1) <= 3 then

                l_str := substr(l_str,1,instr(substr(l_str,1,instr(l_str,';�������')),',',-1)-1)
                        ||substr(l_str,instr(substr(l_str,1,instr(l_str,';�������')),',',-1)+1);
           end if;
           if instr(l_str,';�����') > 0 and
                instr(l_str,';�����') - instr(substr(l_str,1,instr(l_str,';�����')),',',-1) <= 3 then
                l_str := substr(l_str,1,instr(substr(l_str,1,instr(l_str,';�����')),',',-1)-1)
                        ||substr(l_str,instr(substr(l_str,1,instr(l_str,';�����')),',',-1)+1);
           end if;

        end if;
        -- ������� ������
            l_str := replace(l_str,'20214','2014');
            l_str := replace(l_str,'20114','2014');
        --
        l_str := pars_data(l_str,p_np);
        --���������� ��� �������
        if p_np = 50790 then
            l_str := replace(replace(l_str,'1','0'),'2','1');
        end if;


        --

        --
        dbms_output.put_line(l_str);
        l_str := substr(l_str,6,length(l_str) - 10);
        dbms_output.put_line(l_str);
        l_str := regexp_replace(l_str,'[2-9]','1');
        dbms_output.put_line(chr(10)||'--***--'||chr(10)||l_str_output);
        Tester3(l_str);
        if  instr(l_str,'1') = 0 then
            l_str := '-2';
        end if;
        return l_str;
    exception
      when others then
       return -2;
    end;

    procedure parse_all
    is
      str varchar2(1000);
    begin
      for ii in (select * from trains order by id)
      loop  
         str := trainparser_sk.strtoschedule(ii.id); 
         update trains 
           set period_b= str
           where id=ii.id; 
      end loop; 
    end parse_all;

end trainparser_sk;
/
