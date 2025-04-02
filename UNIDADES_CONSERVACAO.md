
**Tabela Principal: `unidade_conservacao`**

```sql
CREATE TABLE unidade_conservacao (
    uc_id INT PRIMARY KEY,
    uc_cod_cnuc TEXT,
    uc_nom TEXT,
    uc_objetivo TEXT,
    dt_certificacao TIMESTAMP,
    dt_aprovacao TIMESTAMP,
    uc_plan_manejo_aprov BOOLEAN,  -- ou BOOLEANO, dependendo da sua configuração
    uc_existencia_conselho BOOLEAN, -- ou BOOLEANO
    uc_outros_instrumentos BOOLEAN, -- ou BOOLEANO
    ano_criacao TEXT,
    cod_wdpa TEXT,
    og_nome TEXT,
    esf_nome TEXT,
    catmanej_nome TEXT,
    catiucn_nome TEXT,
    grumanej_nome TEXT,
    sit_view_situacao TEXT,
    effectivity TEXT,
    in_shape_habilitado TEXT,
    pagina_uc TEXT,
    api_uc TEXT,
    logo_uc TEXT,
    ato_legal_criacao_full_name TEXT,
    ato_legal_criacao_link TEXT,
    area_uc_ato_legal_criacao TEXT,
    area_uc_ato_legal_recente TEXT,
    outros_atos_legais_full_name TEXT,
    outros_atos_legais_link TEXT,
    plano_manejo_full_name TEXT,
    plano_manejo_links TEXT,
    doc_conselho_full_name TEXT,
    doc_conselho_link TEXT,
    out_inst_gest_full_name TEXT,
    out_inst_gest_links TEXT,
    geo_area_hectares_uc TEXT,
    geo_area_hectares_za TEXT,
    uc_sit_fund TEXT,
    uc_pop_trad_ben_ou_res TEXT
);
```

**Tabela: `atos_legais`**

```sql
CREATE TABLE atos_legais (
    docleg_id INT PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    docleg_classdoc_id INT,
    docleg_numero TEXT,
    docleg_link TEXT,
    docleg_link_2 TEXT,
    docleg_dt_doc DATE,
    docleg_dt_publicacao DATE,
    docleg_nu_area_documento TEXT,
    tipodoc_nome TEXT,
    instpub_nome TEXT,
    docfin_nome TEXT,
    docfin_id INT,
    tipo_inst_nome TEXT,
    tipo TEXT
);
```

**Tabela: `planos_manejo`**

```sql
CREATE TABLE planos_manejo (
    docleg_id INT PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    docleg_classdoc_id INT,
    docleg_numero TEXT,
    docleg_link TEXT,
    docleg_link_2 TEXT,
    docleg_dt_doc DATE,
    docleg_dt_publicacao DATE,
    docleg_nu_area_documento TEXT,
    tipodoc_nome TEXT,
    instpub_nome TEXT,
    docfin_nome TEXT,
    docfin_id INT,
    tipo_inst_nome TEXT
);
```

**Tabela: `documentos_conselho`**

```sql
CREATE TABLE documentos_conselho (
    docleg_id INT PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    docleg_classdoc_id INT,
    docleg_numero TEXT,
    docleg_link TEXT,
    docleg_link_2 TEXT,
    docleg_dt_doc DATE,
    docleg_dt_publicacao DATE,
    docleg_nu_area_documento TEXT,
    tipodoc_nome TEXT,
    instpub_nome TEXT,
    docfin_nome TEXT,
    docfin_id INT,
    tipo_inst_nome TEXT
);
```

**Tabela: `outros_instrumentos_gestao`**

```sql
CREATE TABLE outros_instrumentos_gestao (
    docleg_id INT PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    docleg_classdoc_id INT,
    docleg_numero TEXT,
    docleg_link TEXT,
    docleg_link_2 TEXT,
    docleg_dt_doc DATE,
    docleg_dt_publicacao DATE,
    docleg_nu_area_documento TEXT,
    tipodoc_nome TEXT,
    instpub_nome TEXT,
    docfin_nome TEXT,
    docfin_id INT,
    tipo_inst_nome TEXT
);
```

**Tabela: `contatos`**

```sql
CREATE TABLE contatos (
    uc_id INT PRIMARY KEY REFERENCES unidade_conservacao(uc_id),
    end_cep TEXT,
    end_uf TEXT,
    end_municipio TEXT,
    end_bairro TEXT,
    end_logradouro TEXT,
    full_address TEXT,
    uc_site TEXT
);
```

**Tabela: `telefones`**

```sql
CREATE TABLE telefones (
    telefone_id SERIAL PRIMARY KEY,  -- SERIAL é auto-incremento no PostgreSQL
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    uctel_numero TEXT
);
```

**Tabela: `emails`**

```sql
CREATE TABLE emails (
    email_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    ucema_email TEXT
);
```

**Tabela: `biomas`**

```sql
CREATE TABLE biomas (
    bioma_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    bioma TEXT,
    area_bioma_ha DECIMAL(15,2)
);
```

**Tabela: `leg_solos`**

```sql
CREATE TABLE leg_solos (
    solo_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    solo TEXT
);
```

**Tabela: `climas`**

```sql
CREATE TABLE climas (
    clima_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    clima TEXT
);
```

**Tabela: `rios`**

```sql
CREATE TABLE rios (
    rio_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    rio TEXT
);
```

**Tabela: `bacias`**

```sql
CREATE TABLE bacias (
    bacia_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    bacia TEXT
);
```

**Tabela: `ufs_abrangidas`**

```sql
CREATE TABLE ufs_abrangidas (
    uf_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    nome_uf TEXT,
    geocode_uf INT
);
```

**Tabela: `municipios_abrangidos`**

```sql
CREATE TABLE municipios_abrangidos (
    municipio_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    nome_municipio TEXT,
    geocode_mun INT
);
```

**Tabela: `grupos_sociais`**

```sql
CREATE TABLE grupos_sociais (
    grusocial_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    grusocial_nome TEXT
);
```

**Tabela: `videos`**

```sql
CREATE TABLE videos (
    ucvideos_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    ucvideos_nome TEXT,
    ucvideos_link TEXT
);
```

**Tabela: `redes_sociais`**

```sql
CREATE TABLE redes_sociais (
    ucsocial_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    ucsocial_nome TEXT,
    ucsocial_link TEXT
);
```

**Tabela: `visitas`**

```sql
CREATE TABLE visitas (
    ucvisitas_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    ucvisitas_numero INT,
    ucvisitas_ano INT,
    ucvisitas_numero_ano TEXT
);
```

**Tabela: `ppgr` (Programas e Projetos de Gestão Regional)**

```sql
CREATE TABLE ppgr (
    ppgr_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    full_name_ppgr TEXT,
    nome_ppgiri TEXT,
    descricao TEXT,
    nome_instrumento TEXT,
    data_doc_legal DATE,
    doc_legal TEXT,
    nome_contato_1 TEXT,
    tel_1 TEXT,
    email_1 TEXT,
    website_1 TEXT,
    nome_contato_2 TEXT,
    tel_2 TEXT,
    email_2 TEXT,
    logo TEXT,
    tipo_nome TEXT,
    og_nome_ppgr TEXT,
    esf_nome_ppgr TEXT
);
```

**Tabela: `uc_site_links`**

```sql
CREATE TABLE uc_site_links (
    link_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    link_name TEXT,
    link_url TEXT
);
```

**Tabela: `social_networks_links`**

```sql
CREATE TABLE social_networks_links (
    link_id SERIAL PRIMARY KEY,
    uc_id INT REFERENCES unidade_conservacao(uc_id),
    network_name TEXT,
    network_url TEXT
);
```

