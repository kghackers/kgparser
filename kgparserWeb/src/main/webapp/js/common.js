let CONTEXT_PATH = '';
const ROUND_PAGE_URL = '/round.jsp';


const DISABLED_CLASS = 'disabled';

const RESULTS_TABLE_CLASS = 'results';

const PROFILE_IMG_URL = './img/profile2.png';
const PROFILE_IMG_WIDTH = '11';
const PROFILE_IMG_HEIGHT = '16';

function removeChildren(containerId) {
    const dh = DomHelper;

    const container = dh.getEl(containerId);
    dh.removeChildrenHierarchy(container);
}

function appendText(containerId, text) {
    const dh = DomHelper;

    const container = dh.getEl(containerId);
    dh.removeChildrenHierarchy(container);

    container.appendChild(dh.createTN(text));
}

function bindShowHideLink(linkId, divId) {
    const dh = DomHelper;

    const divSelector = '#' + divId;
    $(divSelector).hide(); // default div is hidden

    $('#' + linkId).click(function () {
        $(divSelector).toggle();

        const link = dh.getEl(linkId);
        dh.removeChildrenHierarchy(link);

        if ($(divSelector).is(":hidden")) {
            link.appendChild(dh.createTN('Показать'));
        }
        else {
            link.appendChild(dh.createTN('Скрыть'));
        }
    });
}

function getRoundPageLink(competitionId, roundNumber) {
    return `${CONTEXT_PATH + ROUND_PAGE_URL}?competitionId=${competitionId}&roundNumber=${roundNumber}`;
}
