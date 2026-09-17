-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S6: Views
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------


-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie

CREATE OR REPLACE VIEW deelnemers AS
SELECT i.cursist, i.cursus, i.begindatum, u.docent, u.locatie
FROM inschrijvingen i
JOIN uitvoeringen u ON i.cursus = u.cursus
AND i.begindatum = u.begindatum;

-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view (behandeld in de les):

CREATE OR REPLACE VIEW personeel AS
SELECT mnr, voorl, naam AS medewerker, afd, functie
FROM medewerkers;

SELECT p.medewerker AS cursist, d.cursus, d.begindatum, d.locatie
FROM deelnemers d
JOIN personeel p ON d.cursist = p.mnr
ORDER BY d.begindatum, p.medewerker;

-- 3. Is de view "deelnemers" updatable ? Waarom ?

SELECT table_name, is_updatable, is_insertable_into
FROM information_schema.views
WHERE table_name IN ('deelnemers', 'personeel');

-- Output van query hierboven toont aan dat deelnemers niet updatable is:
-- table_name | is_updatable | is_insertable_into
--------------+--------------+--------------------
-- deelnemers | NO           | NO
-- personeel  | YES          | YES

 -- Waarom: een view is in PostgreSQL alleen automatisch updatable als hij uit
 -- precies één tabel of updatable view leest. "deelnemers" combineert
 -- inschrijvingen en uitvoeringen, dus de database kan niet bepalen welke
 -- onderliggende rij de UPDATE zou moeten krijgen.

-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen: 
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt.

CREATE OR REPLACE VIEW dagcursussen AS
SELECT code, omschrijving, type
FROM cursussen
WHERE lengte = 1;

SELECT * FROM dagcursussen ORDER BY code;

-- 2. Maak een tweede view met de naam "daguitvoeringen". 
--    Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt

CREATE OR REPLACE VIEW daguitvoeringen AS
SELECT u.cursus, u.begindatum, u.docent, u.locatie
FROM uitvoeringen u
JOIN dagcursussen d ON u.cursus = d.code;

SELECT * FROM daguitvoeringen ORDER BY begindatum;

-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT

DROP VIEW dagcursussen RESTRICT;
--ERROR:  view daguitvoeringen depends on view dagcursussencannot drop view dagcursussen because other objects depend on it
--
--ERROR:  cannot drop view dagcursussen because other objects depend on it
--SQL state: 2BP01
--Detail: view daguitvoeringen depends on view dagcursussen
--Hint: Use DROP ... CASCADE to drop the dependent objects too.

--------------------------------------------------------------------------------

DROP VIEW dagcursussen CASCADE;
--NOTICE:  drop cascades to view daguitvoeringen
--DROP VIEW
--
--Query returned successfully in 65 msec.