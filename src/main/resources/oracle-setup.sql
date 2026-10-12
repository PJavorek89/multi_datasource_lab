-- Připojení: jdbc:oracle:thin:@localhost:1521/FREEPDB1, user legacy / legacy
-- Spouštěj v IntelliJ Database konzoli (nebo SQL Developer)

-- 1) Tabulka "legacy" katalogu dílů
CREATE TABLE parts (
  part_no     VARCHAR2(20)  PRIMARY KEY,
  name        VARCHAR2(100) NOT NULL,
  category    VARCHAR2(30)  NOT NULL,
  plant       VARCHAR2(10)  NOT NULL,
  price       NUMBER(10,2)  NOT NULL,
  created_at  DATE          NOT NULL
);

-- 2) 1M řádků testovacích dat
INSERT /*+ APPEND */ INTO parts
SELECT 'P' || LPAD(LEVEL, 9, '0'),
       'Part ' || LEVEL,
       CASE MOD(LEVEL, 5)
         WHEN 0 THEN 'ENGINE' WHEN 1 THEN 'BRAKES' WHEN 2 THEN 'BODY'
         WHEN 3 THEN 'ELECTRIC' ELSE 'INTERIOR' END,
       'PL' || MOD(LEVEL, 20),
       ROUND(DBMS_RANDOM.VALUE(1, 5000), 2),
       DATE '2020-01-01' + MOD(LEVEL, 2000)
FROM dual CONNECT BY LEVEL <= 1000000;
COMMIT;

BEGIN DBMS_STATS.GATHER_TABLE_STATS(USER, 'PARTS'); END;
/

-- 3) Exekuční plány – po každém dotazu spusť DISPLAY_CURSOR a porovnej
--    (sleduj operaci: TABLE ACCESS FULL vs INDEX RANGE SCAN, sloupce E-Rows vs A-Rows, Buffers)

-- 3a) bez indexu → očekávej TABLE ACCESS FULL
SELECT /*+ gather_plan_statistics */ * FROM parts WHERE name = 'Part 4242';
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(format => 'ALLSTATS LAST'));

-- 3b) s indexem → očekávej INDEX RANGE SCAN
CREATE INDEX ix_parts_name ON parts(name);
SELECT /*+ gather_plan_statistics */ * FROM parts WHERE name = 'Part 4242';
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(format => 'ALLSTATS LAST'));

-- 3c) funkce nad sloupcem → index se NEpoužije, zpět FULL scan
SELECT /*+ gather_plan_statistics */ * FROM parts WHERE UPPER(name) = 'PART 4242';
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(format => 'ALLSTATS LAST'));

-- 3d) function-based index → opět index
CREATE INDEX ix_parts_upper_name ON parts(UPPER(name));
SELECT /*+ gather_plan_statistics */ * FROM parts WHERE UPPER(name) = 'PART 4242';
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(format => 'ALLSTATS LAST'));

-- Pokud DISPLAY_CURSOR hlásí chybějící oprávnění, připoj se jako SYSTEM do FREEPDB1 a spusť:
--   GRANT SELECT ANY DICTIONARY TO legacy;
-- Alternativa bez oprávnění (jen odhad, bez skutečných čísel):
--   EXPLAIN PLAN FOR SELECT * FROM parts WHERE name = 'Part 4242';
--   SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);

-- 4) Analytické funkce + CTE: 3 nejdražší díly v každé kategorii
WITH ranked AS (
  SELECT p.*, ROW_NUMBER() OVER (PARTITION BY category ORDER BY price DESC) AS rn
  FROM parts p
)
SELECT category, part_no, name, price FROM ranked WHERE rn <= 3 ORDER BY category, rn;

-- 5) Stránkování (Oracle 12c+)
SELECT * FROM parts ORDER BY part_no OFFSET 20 ROWS FETCH NEXT 10 ROWS ONLY;