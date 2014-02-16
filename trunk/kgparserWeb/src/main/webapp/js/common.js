var CONTEXT_PATH = '';
var ROUND_PAGE_URL = '/round.jsp';


var DISABLED_CLASS = 'disabled';

var RESULTS_TABLE_CLASS = 'results';

var PROFILE_IMG_URL = './img/profile2.png';
var PROFILE_IMG_WIDTH = '11';
var PROFILE_IMG_HEIGHT = '16';

function removeChildren(containerId) {
	var dh = DomHelper;

	var container = dh.getEl(containerId);
	dh.removeChildrenHierarchy(container);
}

function appendText(containerId, text) {
	var dh = DomHelper;

	var container = dh.getEl(containerId);
	dh.removeChildrenHierarchy(container);

	container.appendChild( dh.createTN(text) );
}

function bindShowHideLink(linkId, divId) {
	var dh = DomHelper;

	var divSelector = '#' + divId;
	$(divSelector).hide(); // default div is hidden

	$('#' + linkId).click(function() {
		$(divSelector).toggle();

		var link = dh.getEl(linkId);
		dh.removeChildrenHierarchy(link);

		if ( $(divSelector).is(":hidden") )
		{
			link.appendChild( dh.createTN('Показать') );
		}
		else
		{
			link.appendChild( dh.createTN('Скрыть') );
		}
	});
}

function getRoundPageLink(competitionId, roundNumber) {
	return CONTEXT_PATH + ROUND_PAGE_URL + '?competitionId=' + competitionId + '&roundNumber=' + roundNumber;
}
