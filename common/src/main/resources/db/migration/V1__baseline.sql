CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE agents(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE skills(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE agent_skill_map(
    agent_id INTEGER,
    skill_id INTEGER,
    PRIMARY KEY (agent_id, skill_id)
);

CREATE TABLE work_items(
   id SERIAL PRIMARY KEY,
   title VARCHAR(64) NOT NULL,
   priority INTEGER NOT NULL,
   started_at TIMESTAMP,
   completed_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP
);

CREATE TABLE work_item_skill_map(
    work_item_id INTEGER,
    skill_id INTEGER,
    PRIMARY KEY (work_item_id, skill_id)
);

CREATE TABLE work_item_agent_map(
    work_item_id INTEGER UNIQUE,
    agent_id INTEGER,
    PRIMARY KEY (work_item_id, agent_id)
);

-- finds all agents who match on required skills
CREATE OR REPLACE FUNCTION agents_having_skills(TEXT)
RETURNS SETOF agents AS $$
  DECLARE
    skill_ids int[] := string_to_array($1, ',')::int[];
  BEGIN
    RETURN QUERY
    SELECT DISTINCT ON (a.id) a.*
    FROM agents a
    INNER JOIN agent_skill_map asm ON asm.agent_id = a.id
    WHERE asm.skill_id = ANY(skill_ids)
    GROUP BY a.id
    HAVING array_agg(asm.skill_id) @> skill_ids
    ORDER BY a.id, a.created_at;
  END;
$$ LANGUAGE plpgsql;

--
-- Finds agents who match on required skills while having either
-- (1) no assigned in progress work, or (2) only lower priority work and
-- (3) preferring the agent who most recently started their work if
-- they do have a lower priority task.
--
CREATE OR REPLACE FUNCTION available_agents_having_skills_for_priority(TEXT, INTEGER)
RETURNS SETOF agents AS $$
  DECLARE
    required_skill_ids int[] := string_to_array($1, ',')::int[];
    target_priority int := $2;
  BEGIN
    RETURN QUERY
    SELECT a.*
    FROM agents a
         INNER JOIN agent_skill_map asm ON asm.agent_id = a.id
         INNER JOIN (
            SELECT a.id AS agent_id,
                   CASE
                       WHEN b.agent_id IS NULL THEN 0
                       ELSE b.cnt_in_progress
                   END AS cnt_in_progress,
                   b.max_priority,
                   b.max_started_at
            FROM agents a
                 LEFT OUTER JOIN (
                     SELECT wm.agent_id,
                            COUNT(w.id) AS cnt_in_progress,
                            MAX(w.priority) AS max_priority,
                            MAX(w.started_at) AS max_started_at
                     FROM work_items w
                          INNER JOIN work_item_agent_map wm ON w.id = wm.work_item_id
                     WHERE w.completed_at IS NULL
                     GROUP BY wm.agent_id
                 ) AS b ON a.id = b.agent_id
         ) AS summary ON a.id = summary.agent_id
    WHERE asm.skill_id = ANY(required_skill_ids)
          AND (summary.cnt_in_progress = 0 OR summary.max_priority < target_priority)
    GROUP BY a.id,
             summary.cnt_in_progress,
             summary.max_priority,
             summary.max_started_at
    HAVING array_agg(asm.skill_id) @> required_skill_ids
    ORDER BY summary.cnt_in_progress ASC,
             summary.max_started_at DESC,
             a.id ASC;
  END;
$$ LANGUAGE plpgsql;

-- finds all agents who match on required skills
CREATE OR REPLACE FUNCTION in_progress_work_items_for_agent(INTEGER)
RETURNS SETOF work_items AS $$
  DECLARE
    argument int := $1;
  BEGIN
    RETURN QUERY
    SELECT w.*
    FROM work_items w
         INNER JOIN work_item_agent_map wm ON w.id = wm.work_item_id
    WHERE wm.agent_id = argument
          AND w.completed_at IS NULL;
  END;
$$ LANGUAGE plpgsql;

-- populate skills table
INSERT INTO skills (name, created_at) values('Skill 1', CURRENT_TIMESTAMP);
INSERT INTO skills (name, created_at) values('Skill 2', CURRENT_TIMESTAMP);
INSERT INTO skills (name, created_at) values('Skill 3', CURRENT_TIMESTAMP);

-- populate agents table
INSERT INTO agents(name, created_at) values('Agent One', CURRENT_TIMESTAMP);
INSERT INTO agents (name, created_at) values('Agent Two', CURRENT_TIMESTAMP);
INSERT INTO agents (name, created_at) values('Agent Three', CURRENT_TIMESTAMP);
INSERT INTO agents (name, created_at) values('Agent Four', CURRENT_TIMESTAMP);
INSERT INTO agents (name, created_at) values('Agent Five', CURRENT_TIMESTAMP);
INSERT INTO agents (name, created_at) values('Agent Six', CURRENT_TIMESTAMP);

-- map agents with their skills
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(1,1);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(1,2);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(1,3);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(2,1);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(3,2);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(4,3);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(5,2);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(5,3);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(6,1);
INSERT INTO agent_skill_map (agent_id, skill_id) VALUES(6,2);