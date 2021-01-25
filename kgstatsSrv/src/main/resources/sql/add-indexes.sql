alter table player_vocabulary_stats add index (vocabulary_code);
alter table player_vocabulary_stats add index (vocabulary_code, races_count);
