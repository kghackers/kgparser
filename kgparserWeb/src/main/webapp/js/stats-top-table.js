/* todo: use some out-of-the-box table component */
class TopTable {
    static COLUMNS = Object.freeze({
        NUMBER: {
            header: '#',
            handler: function(player, index) {
                return `<td class="right">${index + 1}</td>`;
            }
        },
        LOGIN: {
            header: 'Логин',
            handler: function(player) {
                const rankClass = getRank(player.rank_level);

                // https://stackoverflow.com/a/15551842/8534088 — how to open a link in new tab/window
                return `<td class="${rankClass}">${player.login}&nbsp;
                    <a href="https://klavogonki.ru/u/#/${player.player_id}/" target="_blank" rel="noopener noreferrer">
                        <img src="img/info.png" alt="Профиль" title="Профиль" width="10" height="10"/>
                    </a>
                </td>`;
            }
        },
        ACHIEVEMENTS_COUNT: {
            header: 'Достижений',
            handler: function(player) {
                return `<td class="right">${player.achievements_count}</td>`;
            }
        },
        TOTAL_RACES_COUNT: {
            header: 'Общий пробег',
            handler: function(player) {
                return `<td class="right">${player.total_races_count}</td>`;
            }
        },
        BEST_SPEED: {
            header: 'Рекорд в &laquo;Обычном&raquo;',
            handler: function(player) {
                return `<td class="right">${player.best_speed}</td>`;
            }
        },
        RATING_LEVEL: {
            header: 'Уровень',
            handler: function(player) {
                return `<td class="right">${player.rating_level}</td>`;
            }
        },
        FRIENDS_COUNT: {
            header: 'Друзей',
            handler: function(player) {
                return `<td class="right">${player.friends_count}</td>`;
            }
        },
        VOCABULARIES_COUNT: {
            header: 'Словарей',
            handler: function(player) {
                return `<td class="right">${player.vocabularies_count}</td>`;
            }
        },
        CARS_COUNT: {
            header: 'Машин',
            handler: function(player) {
                return `<td class="right">${player.cars_count}</td>`;
            }
        },
        REGISTERED: {
            header: 'Зарегистрирован',
            handler: function(player) {
                /* todo: date format! */
                return `<td class="right">${player.registered}</td>`;
            }
        }
    });

    constructor(config) {
        this.config = config;
        // config.data
        // config.columns
    }

    append(containerId) {
        let tableHtml = '<table class="data">';

        tableHtml += '<tr>';
        this.config.columns.forEach(column => {
            tableHtml += '<th>';
            tableHtml += column.header;
            tableHtml += '</th>';
        });
        tableHtml += '</tr>';


        this.config.data.forEach((player, index) => {
            tableHtml += '<tr>'

            this.config.columns.forEach(column => {
                tableHtml += column.handler(player, index);
            });

            tableHtml += '</tr>'
        });

        tableHtml += '</table>';

        document.getElementById(containerId).innerHTML = tableHtml;
    }
}


