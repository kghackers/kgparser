const RANK_COLORS = {
    novice: '#8D8D8D',
    amateur: '#4F9A97',
    cabman: '#187818',
    pro: '#8C8100',
    racer: '#BA5800',
    maniac: '#BC0143',
    superman: '#5E0B9E',
    cyberracer: '#00037C',
    extracyber: '#061956'
};

const STATS_DATA = {




/*
-- total: 358804
select
p.rank_level,
case
    when p.rank_level = 1 then 'Новичок'
    when p.rank_level = 2 then 'Любитель'
    when p.rank_level = 3 then 'Таксист'
    when p.rank_level = 4 then 'Профи'
    when p.rank_level = 5 then 'Гонщик'
    when p.rank_level = 6 then 'Маньяк'
    when p.rank_level = 7 then 'Супермен'
    when p.rank_level = 8 then 'Кибергонщик'
    when p.rank_level = 9 then 'Экстракибер'
end as rank_display_name,
case
    when p.rank_level = 1 then 'novice'
    when p.rank_level = 2 then 'amateur'
    when p.rank_level = 3 then 'cabman'
    when p.rank_level = 4 then 'pro'
    when p.rank_level = 5 then 'racer'
    when p.rank_level = 6 then 'maniac'
    when p.rank_level = 7 then 'superman'
    when p.rank_level = 8 then 'cyberracer'
    when p.rank_level = 9 then 'extracyber'
end as rank_name,
-- p.title,
count(*) as players_count
from player p
where (1 = 1)
and (p.get_summary_error is null) -- exclude non-existing users
and (p.get_index_data_error is null) -- exclude users with failure on /get-index-data
and (not (p.blocked > 0)) -- exclude blocked users
and (p.total_races_count > 0) -- exclude users without races
group by p.rank_level
order by p.rank_level;
 */
    playersByRank: [
        {
            "rank_level": 1,
            "rank_display_name": "Новичок",
            "rank_name": "novice",
            "players_count": 78118
        },
        {
            "rank_level": 2,
            "rank_display_name": "Любитель",
            "rank_name": "amateur",
            "players_count": 106686
        },
        {
            "rank_level": 3,
            "rank_display_name": "Таксист",
            "rank_name": "cabman",
            "players_count": 98285
        },
        {
            "rank_level": 4,
            "rank_display_name": "Профи",
            "rank_name": "pro",
            "players_count": 50124
        },
        {
            "rank_level": 5,
            "rank_display_name": "Гонщик",
            "rank_name": "racer",
            "players_count": 19690
        },
        {
            "rank_level": 6,
            "rank_display_name": "Маньяк",
            "rank_name": "maniac",
            "players_count": 4153
        },
        {
            "rank_level": 7,
            "rank_display_name": "Супермен",
            "rank_name": "superman",
            "players_count": 1298
        },
        {
            "rank_level": 8,
            "rank_display_name": "Кибергонщик",
            "rank_name": "cyberracer",
            "players_count": 363
        },
        {
            "rank_level": 9,
            "rank_display_name": "Экстракибер",
            "rank_name": "extracyber",
            "players_count": 87
        }
    ]
}
