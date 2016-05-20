create or replace package trainparser_sk is

    c_np            number;
    c_start_date    date := to_date('12.12.2015','dd.mm.yyyy');--to_date('01.06.2014','dd.mm.yyyy');
    c_stop_date     date := to_date('11.12.2016','dd.mm.yyyy');--to_date('31.05.2015','dd.mm.yyyy');
  -- Author  : SERGEY
  -- Created : 23.12.2014 9:46:41

  -- ïğîöåäóğà ïàğñèò ğàñïèñàíèå ïîåçäà
    function strtoschedule(
        p_np        number
        ) return varchar2;


      -- ïğîöåäóğà âûâîäà ğåçóëüòàòà òåñòà íà ıêğàí 2
   procedure Tester3 (
      p_str                varchar2                   -- ñòğîêà ğåçóëüòàòîâ
   );


  procedure parse_all;
  
end trainparser_sk;
/
create or replace package body trainparser_sk is

    -- ìåíÿåì íà ñâîé ëàä äíè íåäåëè
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
        select 'ÂÎÑÊĞÅÑÅÍÜßÌ'   as zn   , '7' as d from dual union all
        select 'ÂÎÑÊĞÅÑÅÍÜß'   as zn   , '7' as d from dual union all
        select 'ÂÎÑÊĞÅÑÅÍÈÉ'   as zn   , '7' as d from dual union all
        select 'ÍÅÄ²ËßÕ'   as zn   , '7' as d from dual union all
        select 'ÑÓÁÁÎÒÀÌ'              , '6' from dual union all
        select 'ÑÓÁÁÎÒ'              , '6' from dual union all
        select 'ÏßÒÍÈÖÀÌ'              , '5' from dual union all
        select 'Ï"ßÒÍÈÖßÕ'              , '5' from dual union all
        select 'ÏßÒÍÈÖ'              , '5' from dual union all
        select '×ÅÒÂÅĞÃÀÌ'              , '4' from dual union all
        select '×ÅÒÂÅĞÃÀ'              , '4' from dual union all
        select '×ÅÒÂÅĞÃÎÂ'              , '4' from dual union all
        select 'ÑĞÅÄÀÌ'                 , '3' from dual union all
        select 'ÑĞÅÄÛ'                 , '3' from dual union all
        select 'ÑĞÅÄ'                 , '3' from dual union all
        select 'ÂÒÎĞÍÈÊÀÌ'              , '2' from dual union all
        select 'ÂÒÎĞÍÈÊÀ'              , '2' from dual union all
        select 'ÂÒÎĞÍÈÊÎÂ'              , '2' from dual union all
        select 'ÍÅÄÅËÜÍÈÊÀÌ'          , '1' as zn from dual union all
        select 'ÍÅÄÅËÜÍÈÊÎÂ'          , '1' as zn from dual
        )
        loop
            if instr(l_str,i.zn) > 0 then
                l_n := nvl(l_n,0) + 1;
                if instr(l_str,'#0') = 0 then
                    l_str := substr(l_str,1,instr(l_str,i.zn)+length(i.zn))||' #0 '||substr(l_str,instr(l_str,i.zn)+length(i.zn)+2);
                end if;

                if  instr(l_str,i.zn) - instr(l_str,' È ',-1) <=3 then
                    l_str := substr(l_str,1,instr(l_str,' È ',-1))||' , '||substr(l_str,instr(l_str,i.zn));
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

    -- ìåíÿåì íà ñâîé ëàä ìåñÿö
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
        select 'ßÍÂÀĞÜ'      as mn , ' ,1-31/01/' as zn from dual union all
        select 'ÔÅÂĞÀËÜ'    as mn , ' ,1-28/02/' from dual union all
        select 'ÌÀĞÒ'      as mn , ' ,1-31/03/' from dual union all
        select 'ÁÅĞÅÇÅÍÜ'  as mn , ' ,1-31/03/' from dual union all
        select 'ÀÏĞÅËÜ'      as mn , ' ,1-30/04/' from dual union all
        select 'ÊÂ²ÒÅÍÜ'    as mn , ' ,1-30/04/' from dual union all
        select 'ÌÀÉ'      as mn , ' ,1-31/05/' from dual union all
        select 'ÒĞÀÂÅÍÜ'    as mn , ' ,1-31/05/' from dual union all
        select 'ÈŞÍÜ'      as mn , ' ,1-30/06/' from dual union all
        select '×ÅĞÂÅÍÜ'    as mn , ' ,1-30/06/' from dual union all
        select 'ÈŞËÜ'      as mn , ' ,1-31/07/' from dual union all
        select 'ËÈÏÅÍÜ'      as mn , ' ,1-31/07/' from dual union all
        select 'ÀÂÃÓÑÒ'      as mn , ' ,1-31/08/' from dual union all
        select 'ÑÅĞÏÅÍÜ'    as mn , ' ,1-31/08/' from dual union all
        select 'ÑÅÍÒßÁĞÜ'  as mn , ' ,1-30/09/' from dual union all
        select 'ÂÅĞÅÑÅÍÜ'  as mn , ' ,1-30/09/' from dual union all
        select 'ÎÊÒßÁĞÜ'  as mn , ' ,1-31/10/' from dual union all
        select 'ÆÎÂÒÅÍÜ'  as mn , ' ,1-31/10/' from dual union all
        select 'ÍÎßÁĞÜ'      as mn , ' ,1-30/11/' from dual union all
        select 'ËÈÑÒÎÏÀÄ'   as mn , ' ,1-30/11/' from dual union all
        select 'ÄÅÊÀÁĞÜ'  as mn , ' ,1-31/12/' from dual union all
        select 'ÃĞÓÄÅÍÜ'  as mn , ' ,1-31/12/' from dual union all
        --
        select 'ßÍÂÀĞÅ'      as mn , ' ,1-31/01/' from dual union all
        select 'ÔÅÂĞÀËÅ'    as mn , ' ,1-28/02/' from dual union all
        select 'ÌÀĞÒÅ'      as mn , ' ,1-31/03/' from dual union all
        select 'ÀÏĞÅËÅ'      as mn , ' ,1-30/04/' from dual union all
        select 'ÌÀÅ'      as mn , ' ,1-31/05/' from dual union all
        select 'ÎÊÒßÁĞÅ'  as mn , ' ,1-31/10/' from dual union all
        select 'ÍÎßÁĞÅ'      as mn , ' ,1-30/11/' from dual union all
        select 'ÄÅÊÀÁĞÅ'  as mn , ' ,1-31/12/' from dual union all
        --
        select 'ßÍÂÀĞß'      as mn , ' /01/' from dual union all
        select 'ÔÅÂĞÀËß'  as mn , ' /02/' from dual union all
        select 'ÌÀĞÒÀ'      as mn , ' /03/' from dual union all
        select 'ÀÏĞÅËß'      as mn , ' /04/' from dual union all
        select 'ÌÀß'      as mn , ' /05/' from dual union all
        select 'ÈŞÍß'      as mn , ' /06/' from dual union all
        select 'ÈŞËß'      as mn , ' /07/' from dual union all
        select 'ÀÂÃÓÑÒÀ'  as mn , ' /08/' from dual union all
        select 'ÑÅÍÒßÁĞß'  as mn , ' /09/' from dual union all
        select 'ÎÊÒßÁĞß'  as mn , ' /10/' from dual union all
        select 'ÍÎßÁĞß'      as mn , ' /11/' from dual union all
        select 'ÄÅÊÀÁĞß'  as mn , ' /12/' from dual)
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

    -- îñíîâíàÿ ôóíêöèÿ ïî ëåêñèìàì
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
        -- ïğèâîäèì â ïîğÿäîê ëåêñèìû
        l_str := replace(replace(upper(p_str)
                        ,'ÄÎÏÎËÍÈÒÅËÜÍÎ')
                        ,'ÄÎÄÀÒÊÎÂÎ');
        --âûçîâ âñïîìîãàòåëüíûõ ôóíêöûé
        l_str := replace_day(l_str);
        l_str := replace_mounth(l_str);
        --

        l_str :=    replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
                    replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
                    replace(replace(replace(replace(replace(replace(replace(replace(
                upper(l_str)
                      ,'ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ','#3, ')
                      ,'ÏĞÈ ÄÂÎÕ ÍÅÏÀĞÍÈÕ','#3, ')
                      ,'ÎÒÌÅÍÅÍ','#8 ')
                      ,'Â²ÄÌ²ÍÅÍÎ','#8 ')
                      ,'ÊĞÎÌÅ','#8 ')
                      ,'ÅÆÅÄÍÅÂÍÎ','#9 ')
                      ,'ÙÎÄÅÍÍÎ','#9 ')
                      ,'ÊĞÓÃËÎÃÎÄÈ×ÍÎ','#9 ')
                      ,'ÏÎ ÍÅÏÀĞÍÈÕ','#* ')
                      ,'ÏÎ ÍÅ×ÅÒÍÛÌ','#* ')
                      ,'ÏÎ ×ÅÒÍÛÌ','#+ ')
                      ,'ÍÅÏÀĞÍÈÕ','#* ')
                      ,'ÍÅ×ÅÒÍÛÌ','#* ')
                      ,'×ÅÒÍÛÌ','#+ ' )
                      ,'ÏÎ ÏÀĞÍÈÕ','#+ ' )
                      ,'ÏÎ ÄÍßÌ ÍÅÄÅËÈ','#0 ')
                      ,'ÄÍßÌ ÍÅÄÅËÈ','#0 ')
                      ,'ÏÎ','')
                      ,'ÍÅ×ÅÒÍÛÕ','#* ')
                      ,'ÄÎ','#4 ')
                      ,'ÄÎÏÎËÍÈÒÅËÜÍÎ')
                      ,'ÄÎÄÀÒÊÎÂÎ')
                      ,'ÏĞÈÇÍÀ×ÅÍÎ')
                      ,'Ç','#1 ')
                      ,' Ñ ','#1 ')
                      ,'Ñ ','#1 ')
                      ,' C ','#1 ')
                      ,' CO ','#1 ')
                      ,'ÑÎ ','#1 ')
                      ,'CÎ','#1 ')
                      ,'C ','#1 ')
                      ,':',', ');



        --ëåêñèìà ïî äíàì íåäåëè
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
        -- ëåêñèìà (ïğè äâóõ íå÷åòíûõ)
        if instr(l_str,'#3') > 0 then
            l_str := regexp_replace(l_str,'( )+','',instr(l_str,'#3'));
            l_str := regexp_replace(l_str,'(,)','d',instr(l_str,'#3'));
        end if;

        -- åñëè åñòü ëåêñèìû ïåğåìåùàåì èõ âñåõ â êîíåö ñòğîêè
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
        -- çàìåíà íå íóæíûõ ñèìâîëîâ
        l_str := regexp_replace(l_str,'( )+','');
        l_str := regexp_replace(l_str,'(,)+',',');
        l_str := replace(l_str,';',' ');
        l_str := replace(l_str,'#',' #');
        l_str := regexp_replace(l_str,'(2014)+','2014');
        l_str := regexp_replace(l_str,'(2015)+','2015');

        -- ïğîâåğêà íà íàëè÷èå ìåñÿöà áåç ÷èñëà
        if regexp_instr(l_str,'[, ]/[0-9]{2}') > 0 then
            l_str := substr(l_str,1,regexp_instr(l_str,'[, ]/[0-9]{2}'))
                        ||' 1-31'||
                    ltrim(substr(l_str,regexp_instr(l_str,'[, ]/[0-9]{2}')+1));
        end if;

        -- äîáàâëÿåì êàæäîìó ìåñÿöó íóæíûé ïåğèîä ÷èñåë
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
        -- âñïîìîãàòåëüíàÿ çàìåíà
        l_cnt_s := regexp_instr(' '||l_str,'[, -][0-9]{2}/');
        l_str := replace(l_str,'Â');
        -- ïğîâåğêà ïğàâèëüíîñòè ÷èñëà ìåñÿöà
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
        --óáåğàåì ïîñëåäíşş çàïÿòóş îíà íàì íå íóæíà
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



    -- ïğîöåäóğà ïåğåâîäèò ğàñïèñàíèå ê âèäó 01
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
        -- ïğîâåğÿåì íàëè÷èÿ äàò
        if regexp_instr(p_str,'[0-9]{2}/[0-9]{2}/[0-9]{4}') >= 1 or regexp_instr(p_str,'[0-9]{1}/[0-9]{2}/[0-9]{4}') >=1 then
            l_cnt_d := 1;
            -- êóğñîğ ïî äàòàì
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
        -- êóğñîğ ïî ëåêñèìàì
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


        --÷àñòíûé ñóë÷àé äëÿ ïîåçäîâ
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

    -- ïğîöåäóğà ïåğåâîäèò ğàñïèñàíèå ê âèäó 01
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
            -- ïåğåñòàèâàåì ñòğîêó ïîä ñåáÿ
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
                    -- ïåğåñòàèâàåì ñòğîêó ïîä ñåáÿ
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
                        -- ïåğåñòàèâàåì ñòğîêó ïîä ñåáÿ
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

     -- ïğîöåäóğà âûâîäà ğåçóëüòàòà òåñòà íà ıêğàí 2
   procedure Tester3 (
      p_str                varchar2                   -- ñòğîêà ğåçóëüòàòîâ
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
            -- âûâîäèì ìåñÿö
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

    -- ïğîöåäóğà ïàğñèò ğàñïèñàíèå ïîåçäà
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
                ,'(ÊĞÎÌÅ',';ÊĞÎÌÅ'),'ÊĞÎÌÅ',';ÊĞÎÌÅ'),')','')
                /*,'ÊĞÓÃËÎÃÎÄÈ×ÍÎ ÏÎ ÍÅ×ÅÒÍÛÌ,','ÊĞÓÃËÎÃÎÄÈ×ÍÎ ÏÎ ÍÅ×ÅÒÍÛÌ;')*/,'(;)+',';'))
                ,ts.period_r
            into l_str,l_str_output
            from trains ts
            where ts.id = p_np;

        dbms_output.put_line('---*---');
        dbms_output.put_line(p_np);
        dbms_output.put_line(l_str);
        --l_str_output := l_str;

        --èñêëş÷åíèÿ äëÿ ïîåçäîâ
        --*-
            /*if p_np = 1251 then
                l_str := replace(l_str,':',';2016 ');
            end if;*/

        if p_np = 373 then
           l_str := replace(l_str,'È 24,',';ÊĞÎÌÅ 24,');
        end if;
        if p_np = 394 and l_str = '13/12/2015-25/03/2016 ;ÊĞÎÌÅ 29/02/2016  ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 27,29,1; ÄÎÏÎËÍÈÒÅËÜÍÎ 4,6,8,10,12,14,16/01/2016' then
            l_str := '13/12/2015-25/03/2016 ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 27,29,1 ;ÊĞÎÌÅ 29/02/2016 ; ÄÎÏÎËÍÈÒÅËÜÍÎ 4,6,8,10,12,14,16/01/2016';
        end if;

        if p_np = 1630 and l_str = '27/03-9/12/2016 ÏÎ ÍÅ×ÅÒÍÛÌ ;ÊĞÎÌÅ 29/03, 5/04/2016, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3' then
            l_str := '27/03-9/12/2016 ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3 ;ÊĞÎÌÅ 29/03, 5/04/2016';
        end if;
        if p_np = 1546 and l_str = ' Ñ 27/03/2016 ÏÎ ÂÎÑÊĞÅÑÅÍÜßÌ, ÂÒÎĞÍÈÊÀÌ È ÏßÒÍÈÖÀÌ' then
            l_str := ' Ñ 27/03/2016 ÏÎ ÂÒÎĞÍÈÊÀÌ, ÏßÒÍÈÖÀÌ È ÂÎÑÊĞÅÑÅÍÜßÌ';
        end if;

        if p_np = 1275 then
            l_str := replace(l_str,';ÊĞÎÌÅ 25/01/2016, 29/02/2016 ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3','ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3 ;ÊĞÎÌÅ 25/01/2016, 29/02/2016 ');
        end if;
        if p_np = 1276 then
            l_str := replace(l_str,';ÊĞÎÌÅ 25/01/2016, 1/03/2016 ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 31,3,5',' ÏÎ ÍÅ×ÅÒÍÛÌ, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 31,3,5 ;ÊĞÎÌÅ 25/01/2016, 1/03/2016');
        end if;

        if p_np = 54126 then
           l_str := replace(l_str,'( ;ÊĞÎÌÅ 29/02/2016','')||' ;ÊĞÎÌÅ 29/02/2016';
        end if;


        if l_str like '%È Ñ %' then
            l_str := substr(l_str,1,instr(l_str,'È Ñ')-1)||
                     substr(l_str,regexp_instr(l_str,'ÏÎ ',instr(l_str,'È Ñ')),regexp_instr(l_str||';',';',instr(l_str,'È Ñ')) - regexp_instr(l_str,'ÏÎ ',instr(l_str,'È Ñ')) + 1)||
                     substr(l_str,instr(l_str,'È Ñ'));
        end if;
        if l_str like '%È ÑÎ %' then
            l_str := substr(l_str,1,instr(l_str,'È ÑÎ ')-1)||
                     substr(l_str,regexp_instr(l_str,'ÏÎ ',instr(l_str,'È ÑÎ ')),regexp_instr(l_str||';',';',instr(l_str,'È ÑÎ ')) - regexp_instr(l_str,'ÏÎ ',instr(l_str,'È ÑÎ ')) + 1)||
                     substr(l_str,instr(l_str,'È ÑÎ '));
        end if;

        if p_np = 311 and l_str = ' Ñ 13/12/2015 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ×ÅÒÂÅĞÃÎÂ È ÏßÒÍÈÖ È 22,23,29,30/12/2015, 23,26/03,27/08/2016' then
           l_str := 'Ñ 13/12/2015 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ×ÅÒÂÅĞÃÎÂ È ÏßÒÍÈÖ ;ÊĞÎÌÅ 22,23,29,30/12/2015, 23,26/03,27/08/2016';
        end if;

        if p_np = 1840 and l_str = '29/03-9/12/2016 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ÑÓÁÁÎÒ È ÂÎÑÊĞÅÑÅÍÈÉ È 29/08/2016' then
           l_str := '29/03-9/12/2016 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ÑÓÁÁÎÒ È ÂÎÑÊĞÅÑÅÍÈÉ ;ÊĞÎÌÅ 29/08/2016';
        end if;

        if p_np = 1839 and l_str = '27/03-7/12/2016 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ×ÅÒÂÅĞÃÎÂ È ÏßÒÍÈÖ È 27/08/2016' then
           l_str := '27/03-7/12/2016 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ×ÅÒÂÅĞÃÎÂ È ÏßÒÍÈÖ ;ÊĞÎÌÅ 27/08/2016';
        end if;


        if p_np = 373 and l_str = ' Ñ 14/12/2015 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ÑÓÁÁÎÒ È ÂÎÑÊĞÅÑÅÍÈÉ È 24,25,31/12/2015,1/01,25,28/03,29/08/2016' then
           l_str := ' Ñ 14/12/2015 ÅÆÅÄÍÅÂÍÎ, ;ÊĞÎÌÅ ÑÓÁÁÎÒ È ÂÎÑÊĞÅÑÅÍÈÉ ;ÊĞÎÌÅ 24,25,31/12/2015 ;ÊĞÎÌÅ 1/01,25,28/03,29/08/2016';
        end if;

        if p_np = 1384 and l_str = ' 22/12/2015-18/03/2016 ÏÎ ÂÎÑÊĞÅÑÅÍÜßÌ, ÂÒÎĞÍÈÊÀÌ È ÏßÒÍÈÖÀÌ' then
           l_str := ' 22/12/2015-18/03/2016 ÏÎ ÂÒÎĞÍÈÊÀÌ, ÏßÒÍÈÖÀÌ È ÂÎÑÊĞÅÑÅÍÜßÌ';
        end if;


        if p_np = 1136 then
            l_str := replace(l_str,'12-30/12/2015,','12-30/12/2015 ÏÎ ×ÅÒÍÛÌ;');
        end if;

        if p_np = 1276 then
            l_str := replace(l_str,';ÊĞÎÌÅ  1/03/2016,',';ÊĞÎÌÅ  1/03/2016;');
        end if;

        if p_np = 417 and l_str = '13/12/2015-9/12/2016 ÏÎ ÍÅ×ÅÒÍÛÌ ;ÊĞÎÌÅ 29/02/2016, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3' then
            l_str := '13/12/2015-9/12/2016 ÏÎ ÍÅ×ÅÒÍÛÌ ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 29,1,3 ;ÊĞÎÌÅ 29/02/2016';
        end if;

        --l_str := replace(l_str,' È ',' ; ');

            if p_np = 1024 then
                l_str := replace(l_str,'26/05/2016,','26/05/2016;');
            end if;
            if p_np in (387) then
                l_str := replace(l_str,'ÊĞÎÌÅ 29/02/2016,','ÊĞÎÌÅ 29/02/2016;');
            end if;

            if p_np in (53439) and l_str = ' Ñ 13/12/2015 ÏÎ ÍÅ×ÅÒÍÛÌ ;ÊĞÎÌÅ 29/02/2016, ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 27,29,1,3;  30/12/2015, 4,6,10/01/2016' then
                l_str := 'Ñ 13/12/2015 ÏÎ ÍÅ×ÅÒÍÛÌ ÏĞÈ ÄÂÓÕ ÍÅ×ÅÒÍÛÕ 27,29,1,3; ;ÊĞÎÌÅ 29/02/2016;  30/12/2015, 4,6,10/01/2016';
            end if;


            if p_np = 54337 then
                l_str := replace(l_str,'ÂÒÎĞÍÈÊÀÌ, È')||',ÂÒÎĞÍÈÊÀÌ';
            end if;

            if p_np = 53304 then
                l_str := replace(l_str,'ÑÓÁÁÎÒÀÌ, Ñ','ÑÓÁÁÎÒÀÌ; Ñ');
                l_str := replace(l_str,'ÂÎÑÊĞÅÑÅÍÜßÌ,') || ', ÂÎÑÊĞÅÑÅÍÜßÌ';
                l_str := substr(l_str,1,instr(l_str,';') - 1)|| ', ÂÎÑÊĞÅÑÅÍÜßÌ;'||
                         substr(l_str,instr(l_str,';') + 1,length(l_str) - instr(l_str,';')  - 14);
            end if;

            if p_np = 54273 then
                null;
                -- Ñ 25/10/2015 ÏÎ  ÂÎÑÊĞÅÑÅÍÜßÌ, ÏßÒÍÈÖÀÌ È  ÑÓÁÁÎÒÀÌ
                l_str := replace(l_str,'ÏßÒÍÈÖÀÌ È  ÑÓÁÁÎÒÀÌ','ÑÓÁÁÎÒÀÌ È ÏßÒÍÈÖÀÌ');
                --l_str := replace(l_str,'È',',');
            end if;

            if p_np = 50790 then
                l_str := replace(l_str,':',';');
            end if;

            if p_np in (52966) then
                l_str := substr(l_str,1,instr(l_str,';ÊĞÎÌÅ')-1)||
                         substr(l_str,instr(l_str,',',instr(l_str,';ÊĞÎÌÅ'))+1)||
                         substr(l_str,instr(l_str,';ÊĞÎÌÅ'),instr(l_str,',',instr(l_str,';ÊĞÎÌÅ')) - instr(l_str,';ÊĞÎÌÅ'))
                        ;
                dbms_output.put_line(l_str);
            end if;
            if p_np in (54350,54313) then
                l_str := substr(l_str,1,instr(l_str,';ÊĞÎÌÅ')-1)||
                         substr(l_str,instr(l_str,'5,',instr(l_str,';ÊĞÎÌÅ'))+2)||
                         substr(l_str,instr(l_str,';ÊĞÎÌÅ'),instr(l_str,'5,',instr(l_str,';ÊĞÎÌÅ')) - instr(l_str,';ÊĞÎÌÅ')+1)
                        ;
                dbms_output.put_line(l_str);
            end if;
            if p_np in (54233) then
                l_str := replace(l_str,'ÎÒ,','ÎÒ;ÊĞÎÌÅ');
            end if;

            if p_np = 50791 then
                l_str := substr(l_str,1,instr(l_str,';'))||
                          substr(l_str,1,instr(l_str,':'))||' '||
                          replace(substr(l_str,instr(l_str,';')),';');

            end if;

            if p_np in (52602,52601) then
                l_str := replace(l_str,'ÏĞÈ ÄÂÓÕ',';ÏĞÈ ÄÂÓÕ');
            end if;
        --*-

        --èñêëş÷åíèÿ
        -- èçìèíåíèå ñòğîêè ê âèäó 01
        if instr(l_str,'ÁÓÄÜÒÅ ÂÍÈÌÀÒÅËÜÍÛ') > 0 then
            l_str := substr(l_str,1,instr(l_str,'ÁÓÄÜÒÅ ÂÍÈÌÀÒÅËÜÍÛ') - 1);
        end if;
        if instr(l_str,'ÏÅĞÅÑÀÄÊÀ') > 0 then
            l_str := substr(l_str,1,instr(l_str,'ÏÅĞÅÑÀÄÊÀ') - 1);
        end if;
        l_str := replace(l_str,'ÍÀÇÍÀ×ÅÍ');
        /*if instr(l_str,'ÏĞÈ ÄÂÓÕ') > 0 then
           l_str := replace(l_str,'ÏĞÈ ÄÂÓÕ',';ÏĞÈ ÄÂÓÕ');
        end if;*/

        if instr(l_str,'ÎÒÌÅÍÅÍ') > 0 or instr(l_str,'ÊĞÎÌÅ') > 0 and instr(l_str,';') = 0 then
           l_str := replace(replace(l_str,'ÎÒÌÅÍÅÍ',';ÎÒÌÅÍÅÍ')
                                         ,'ÊĞÎÌÅ',';ÊĞÎÌÅ');
           l_str := regexp_replace(l_str,'(;)+',';');
           if instr(l_str,';ÎÒÌÅÍÅÍ') > 0 and
                instr(l_str,';ÎÒÌÅÍÅÍ') - instr(substr(l_str,1,instr(l_str,';ÎÒÌÅÍÅÍ')),',',-1) <= 3 then

                l_str := substr(l_str,1,instr(substr(l_str,1,instr(l_str,';ÎÒÌÅÍÅÍ')),',',-1)-1)
                        ||substr(l_str,instr(substr(l_str,1,instr(l_str,';ÎÒÌÅÍÅÍ')),',',-1)+1);
           end if;
           if instr(l_str,';ÊĞÎÌÅ') > 0 and
                instr(l_str,';ÊĞÎÌÅ') - instr(substr(l_str,1,instr(l_str,';ÊĞÎÌÅ')),',',-1) <= 3 then
                l_str := substr(l_str,1,instr(substr(l_str,1,instr(l_str,';ÊĞÎÌÅ')),',',-1)-1)
                        ||substr(l_str,instr(substr(l_str,1,instr(l_str,';ÊĞÎÌÅ')),',',-1)+1);
           end if;

        end if;
        -- ÷àñòíûå ñëó÷àè
            l_str := replace(l_str,'20214','2014');
            l_str := replace(l_str,'20114','2014');
        --
        l_str := pars_data(l_str,p_np);
        --èñêëş÷åíèÿ äëÿ ïîåçäîâ
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
