/* todo: use some out-of-the-box table component */
class TopTable {
    static COLUMNS = Object.freeze({
        NUMBER: {
            header: '#',
            handler: function (player, index) {
                return `<td class="right">${index + 1}</td>`;
            }
        },
        LOGIN: {
            header: 'Логин',
            handler: function (player) {
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
            handler: function (player) {
                return `<td class="right">${player.achievements_count}</td>`;
            }
        },
        TOTAL_RACES_COUNT: {
            header: 'Общий пробег',
            handler: function (player) {
                return `<td class="right">${player.total_races_count}</td>`;
            }
        },
        BEST_SPEED: {
            header: 'Рекорд в &laquo;Обычном&raquo;',
            handler: function (player) {
                return `<td class="right">${player.best_speed}</td>`;
            }
        },
        RATING_LEVEL: {
            header: 'Уровень',
            handler: function (player) {
                return `<td class="right">${player.rating_level}</td>`;
            }
        },
        FRIENDS_COUNT: {
            header: 'Друзей',
            handler: function (player) {
                return `<td class="right">${player.friends_count}</td>`;
            }
        },
        VOCABULARIES_COUNT: {
            header: 'Словарей',
            handler: function (player) {
                return `<td class="right">${player.vocabularies_count}</td>`;
            }
        },
        CARS_COUNT: {
            header: 'Машин',
            handler: function (player) {
                return `<td class="right">${player.cars_count}</td>`;
            }
        },
        REGISTERED: {
            header: 'Зарегистрирован',
            handler: function (player) {
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

class Paging {
    static SPACE_SEPARATOR = '&nbsp;&nbsp;&nbsp;';

    constructor(config) {
        // todo: make 3, 5, 3 page numbers count also configurable, not hardcoded
        this.config = config;
        // config.totalPages
        // config.currentPage
        // config.bindLinks
        // config.getPagingLink

        this.config.getPagingLink ||= this.getPagingLink; // default make a javascript href
    }

    append(containerId) {
        let pagingHtml = '';

        let firstPages = [1, 2, 3];
        let currentPages = [this.config.currentPage - 2, this.config.currentPage - 1, this.config.currentPage, this.config.currentPage + 1, this.config.currentPage + 2];
        let lastPages = [this.config.totalPages - 2, this.config.totalPages - 1, this.config.totalPages];

        let addedPages = [];

        firstPages.forEach(pageNumber => {
            if (pageNumber <= this.config.totalPages) {
                pagingHtml += this.getPageNumberHtml(containerId, pageNumber);
                addedPages.push(pageNumber);
            }
        });

        // separator
        if ((this.config.currentPage - 2) > 4) {
            pagingHtml += `...${Paging.SPACE_SEPARATOR}`;
        }

        currentPages.forEach(pageNumber => {
            if (pageNumber > 3 && pageNumber < this.config.totalPages - 2) {
                pagingHtml += this.getPageNumberHtml(containerId, pageNumber);
                addedPages.push(pageNumber);
            }
        });

        // separator
        if ((this.config.currentPage + 2) < this.config.totalPages - 3) {
            pagingHtml += `...${Paging.SPACE_SEPARATOR}`;
        }

        lastPages.forEach(pageNumber => {
            if (pageNumber > 3) {
                pagingHtml += this.getPageNumberHtml(containerId, pageNumber);
                addedPages.push(pageNumber);
            }
        });

        // pagingHtml = pagingHtml.substring(0, pagingHtml.length - Paging.SPACE_SEPARATOR.length);

        document.getElementById(containerId).innerHTML = pagingHtml;

        if (this.config.bindLinks) {
            this.bindLinks(containerId, addedPages);
        }
    }

    bindLinks(containerId, addedPageNumbers) {
        addedPageNumbers.forEach(pageNumber => {
            if (pageNumber === this.config.currentPage) { // current page - no onclick bound
                return;
            }

            const linkId = this.getLinkId(containerId, pageNumber);
            const link = document.getElementById(linkId);

            const table = this;

            link.onclick = function () {
                table.config.currentPage = pageNumber;
                table.append(containerId); // todo: must change both top and bottom pagings
            }
        });
    }

    getPageNumberHtml(containerId, pageNumber) {
        if (pageNumber === this.config.currentPage) {
            return `${pageNumber}${Paging.SPACE_SEPARATOR}`;
        }

        const linkId = this.getLinkId(containerId, pageNumber);

        return this.config.getPagingLink(linkId, pageNumber);
    }

    getPagingLink(linkId, pageNumber) {
        return `<a href="javascript:void(0);" id="${linkId}">${pageNumber}</a>${Paging.SPACE_SEPARATOR}`;
    }

    getLinkId(containerId, pageNumber) {
        return `${containerId}-page-${pageNumber}-link`;
    }
}
