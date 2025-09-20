-- Para obter um processo "Protocolado"
SELECT p1.cod_situacao, sp.dsc_situacao, p1.sig_classe_proces, p1.num_processo
FROM judiciario.processo p1
INNER JOIN judiciario.situacao_processo sp ON sp.cod_situacao = p1.cod_situacao
WHERE p1.cod_situacao in ('V','W','R','P','K','I','D','A','J','B','C','S','G','F','E','L')
AND p1.seq_objeto_incidente IN
(
        SELECT p.seq_objeto_incidente
        FROM judiciario.processo p
        WHERE p.cod_situacao = p1.cod_situacao
        AND ROWNUM = 1
)


SELECT *
FROM ministros
WHERE dat_afast_ministro IS NULL AND cod_setor ='600000003' 

SELECT st.cod_ministro
FROM sit_min_processos st, ministros mn
WHERE st.sig_classe_proces = 'ADI'
AND st.num_processo = '1544'
AND st.cod_ocorrencia IN ('RE', 'RG', 'SU')
AND dat_ocorrencia =
(SELECT *--MAX (dat_ocorrencia)
FROM sit_min_processos
WHERE sig_classe_proces = 'ADI'
AND num_processo = '1544'
AND cod_ocorrencia IN ('RE', 'RG', 'SU'))
AND mn.cod_ministro = st.cod_ministro

SELECT *
FROM sit_min_processos
WHERE ROWNUM = 1

-- Listar processos de um determinado gabinete

SELECT p1.seq_objeto_incidente, p1.cod_situacao, sp.dsc_situacao, p1.sig_classe_proces, p1.num_processo
FROM judiciario.processo p1
INNER JOIN judiciario.situacao_processo sp ON sp.cod_situacao = p1.cod_situacao
WHERE p1.cod_situacao in ('V','W','R','P','K','I','D','A','J','B','C','S','G','F','E','L')
AND p1.seq_objeto_incidente IN
(
        SELECT seq_objeto_incidente
        FROM sit_min_processos st
        INNER JOIN ministros mn ON mn.cod_ministro = st.cod_ministro
        WHERE 1=1
        AND st.cod_ocorrencia IN ('RE', 'RG', 'SU')
        --AND st.sig_classe_proces = 'ARE'
        --AND ROWNUM <10
        AND st.cod_ministro = (
            SELECT cod_ministro
            FROM ministros
            WHERE dat_afast_ministro IS NULL AND cod_setor ='600000003'
        )
        GROUP BY seq_objeto_incidente, dat_ocorrencia HAVING dat_ocorrencia = MAX(dat_ocorrencia)
)
AND p1.sig_classe_proces <> 'ARE'



-- Detalhar um processo

SELECT *
FROM sit_min_processos A
WHERE 1=1
AND ROWNUM < 50
AND A.sig_classe_proces = 'ADI'
AND A.num_processo = 1537

-- Verificar os andamentos de um processo
SELECT ap.num_sequencia, ap.dat_andamento, ap.cod_andamento,
an.dsc_andamento, od.seq_origem_decisao, od.dsc_origem_decisao,
ap.num_seq_errado, ap.dsc_obser_and, ap.dsc_obs_interna,
ap.seq_andamento_processo, m.nom_ministro
--, td.seq_tipo_devolucao
--,td.dsc_tipo_devolucao
FROM andamento_processos ap,
andamentos an,
stf.origem_decisao od,
stf.ministros m--,
--stf.tipo_devolucao td
WHERE ap.sig_classe_proces = 'RE'
AND ap.num_processo = 582702
AND an.cod_andamento = ap.cod_andamento
AND od.seq_origem_decisao(+) = ap.seq_origem_decisao
AND m.cod_ministro(+) = ap.cod_presidente_interino
--AND td.seq_tipo_devolucao(+) = ap.seq_tipo_devolucao
ORDER BY num_sequencia ASC

-- Lista de andamentos autorizados para cada setor
SELECT *
FROM stf.autoriza_andamentos aa
INNER JOIN stf.andamentos a ON aa.cod_andamento = a.cod_andamento
WHERE a.FLG_AND_ESPECIAL = 'N'
AND aa.cod_setor = '600000154'
ORDER BY aa.cod_andamento


-- Lista setores que podem lançar um tipo de andamento
SELECT aa.cod_setor
FROM stf.autoriza_andamentos aa
INNER JOIN stf.andamentos a ON aa.cod_andamento = a.cod_andamento
WHERE 1=1
AND a.FLG_AND_ESPECIAL = 'N'
AND aa.cod_andamento = '6249'
ORDER BY 1

-- Para saber se um processo é findo
SELECT COUNT (1)
FROM stf.andamento_processos ap
WHERE ((cod_andamento = 7106) or (cod_andamento = 2309))
and sig_classe_proces = <SIG_CLASSE_PROCES>
and num_processo = <NUM_PROCESSO>
AND NOT EXISTS (
SELECT 1
FROM stf.andamento_processos ap2
WHERE ap2.num_seq_errado = ap.num_sequencia
AND ap2.seq_objeto_incidente = ap.seq_objeto_incidente)
GROUP BY sig_classe_proces, num_processo

---


SELECT * FROM stf.andamentos WHERE cod_andamento IN ('7600','7601','7700','6249','6247')
 


SELECT aa.cod_setor, m.dsc_setor
FROM stf.autoriza_andamentos aa
INNER JOIN stf.setores m ON m.cod_setor = aa.cod_setor
WHERE 1=1
AND aa.cod_andamento = '8233'
AND aa.cod_setor = '600000154'



SELECT *
FROM stf.andamentos an
WHERE cod_prox_andamento IS NOT NULL

SELECT * FROM andamentos
WHERE UPPER(dsc_andamento) LIKE '%APENS%'


