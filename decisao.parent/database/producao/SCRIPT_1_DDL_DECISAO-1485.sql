/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     20/11/2013 18:12:27                          */
/*==============================================================*/


/*==============================================================*/
/* Table: RESTRICAO_CLASSE_RECURSO                              */
/*==============================================================*/
create table JUDICIARIO.RESTRICAO_CLASSE_RECURSO 
(
   SIG_CLASSE           VARCHAR2(6)          not null,
   SEQ_TIPO_RECURSO     NUMBER(10)           not null,
   USU_INCLUSAO         VARCHAR2(30),
   DAT_INCLUSAO         DATE,
   USU_ALTERACAO        VARCHAR2(30),
   DAT_ALTERACAO        DATE,
   constraint PK_RESTRICAO_CLASSE_RECURSO primary key (SIG_CLASSE, SEQ_TIPO_RECURSO)
);

comment on table JUDICIARIO.RESTRICAO_CLASSE_RECURSO is
'<COMENTARIO>

<NOME>RESTRICAO_CLASSE_RECURSO � Tipo de recurso restrito a determinada classe</NOME>

<ESQUEMA>JUDICIARIO</ESQUEMA>

<DESCRICAO>Cada registro dessa tabela representa um tipo de recurso, proveniente de JUDICIARIO.TIPO_RECURSO, de uso restrito da classe, proveniente de JUDICIARIO.CLASSE, associada.
A ideia � restringir certos tipos de recuros a classes espec�ficas, de forma que os recursos exclusivos de determinada classe sejam utilizados apenas por ela.
Como exemplo temos os recursos exclusivos da classe Execu��o Penal (EP), a saber:
a) Progress�o de regime;
b) Livramento condicional;
c) Indulto ou comuta��o;
d) Remi��o de penas;
e) Reconhecimento de homon�mia;
f) Autoriza��o de trabalho/estudo;
g) Extin��o da punibilidade;
h) Sa�da tempor�ria;
i) Transfer�ncia de local de cumprimento de pena;
j) Tratamento m�dico;
l) Aplica��o da lei mais ben�fica;
m) Reconhecimento de continuidade delitiva.</DESCRICAO>

<SISTEMA_PROP></SISTEMA_PROP>

<DESENVOLVIMENTO>SSNE</DESENVOLVIMENTO>

<GESTOR_INFO></GESTOR_INFO>

<SISTEMAS_USU></SISTEMAS_USU>

<DATAMARTS_USU></DATAMARTS_USU>

<CLASSIFICACAO_INFO><CLASSIFICACAO_INFO>

</COMENTARIO>';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.SIG_CLASSE is
'Identificador da classe cujo o tipo de recurso ser� restringido. Chave estrangeira com coluna SIG_CLASSE da tabela JUDICIARIO.CLASSE.';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.SEQ_TIPO_RECURSO is
'Identificador do tipo de recurso restrito � classe do registro. Chave estrangeira com a coluna SEQ_TIPO_RECURSO da tabela JUDICIARIO.TIPO_RECURSO.';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.USU_INCLUSAO is
'Coluna alimentada por uma trigger de auditoria com a sig_usuario que efetuou a inclus�o do registro.
Alimentada uma �nica vez, somente na inclus�o do registro.';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.DAT_INCLUSAO is
'Coluna alimentada por uma trigger de auditoria com a data que  o registro foi incluido.
Alimentada uma �nica vez na inclus�o do registro.';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.USU_ALTERACAO is
'Coluna alimentada por uma trigger de auditoria com a sig_usuario que efetuou a altera��o do registro.
Alimentada todas as vezes que o usu�rio efetuar uma altera��o  no registro.';

comment on column JUDICIARIO.RESTRICAO_CLASSE_RECURSO.DAT_ALTERACAO is
'Coluna alimentada por uma trigger de auditoria com a data que  o registro foi alterado.
Alimentada toda vez que ocorrer altera��o no registro.';

alter table JUDICIARIO.RESTRICAO_CLASSE_RECURSO
   add constraint FK_CLASSE_RECR foreign key (SIG_CLASSE)
      references JUDICIARIO.CLASSE (SIG_CLASSE);

alter table JUDICIARIO.RESTRICAO_CLASSE_RECURSO
   add constraint FK_TIPO_RECURSO_RECR foreign key (SEQ_TIPO_RECURSO)
      references JUDICIARIO.TIPO_RECURSO (SEQ_TIPO_RECURSO);

