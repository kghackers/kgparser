
// ==UserScript==
// @name KG_SaveGameResult
// @namespace http://klavogonki.alexzh.ru
// @description Добавляет ссылку на сохранение результатов заезда в JSON формате
// @author voidmain
// @license MIT
// @version 1.3
// @include http://klavogonki.ru/g/*
// @grant none
// @run-at document-end
// ==/UserScript==
function exec(fn) {
	var script = document.createElement('script');
	script.setAttribute("type", "application/javascript");
	script.textContent = '(' + fn + ')();';
	document.body.appendChild(script); // run the script
	document.body.removeChild(script); // clean up
}
function main() {
	function getGameData() {
		var aeroRegex = new RegExp("'.+?'");
		return {
			'id': game.id,
			'beginTime': game.begintimeServer*1000,
			'gameInfo': {
				'type': game.params.gametype,
				'levelFrom': game.params.level_from,
				'levelTo': game.params.level_to,
				'timeout': game.params.timeout,
				'isPremiumAbra': game.params.premium_abra == true,
				'isQualification': game.params.qual == 'on',
				'mode': game.params.type
			},
			'places': game.places,
			'players': game.players.map(function(p) {
				return {
					'finishedTime': p.info.finished,
					'speed': Math.round(game.charsTotal*60000/(p.info._finished - game.begintimeServer*1000)),
					'errorsCount': p.info.errors,
					'errorsPercent': Math.round(p.info.errors*10000/game.charsTotal)/100,
					'user': p.info.user ? {
						'id': p.info.user.id,
						'login': p.info.user.login,
						'level': p.info.user.level,
						'registered': p.info.user.registered,
						'startDate': p.info.user.startdate,
						'numRaces': p.info.user.num_races,
						'avgSpeed': p.info.user.avg_speed,
						'bestSpeed': p.info.user.best_speed,
						'avgError': p.info.user.avg_error,
						'totalTime': p.info.user.haul.total,
						'qual': p.info.user.qual,
						'car': {
							'type': p.info.user.car,
							'color': p.info.user.color,
							'tuning': p.info.user.tuning,
							'aeroUrl': p.info.user.aero ? /'(.+?)'/.exec(p.info.user.background)[1] : null
						}
					} : null
				};
			}),
			'text': game.text
		};
	}
	var $gameResultLink = $$$('<a href="#" title="Сохранить результаты заезда"></a>').appendTo('body').on('click', function() {
		$$$(this).attr( {
			'href': 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(getGameData())),
			'download': 'game_' + game.id + '.json'
		});
	});
	$$$('#status .gametype-sign').wrap($gameResultLink);
}
window.addEventListener("load", function() {
// script injection
	exec(main);
}, false);
