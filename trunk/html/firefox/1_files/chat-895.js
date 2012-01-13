function recalcFixedChat()
{
	if(!chat)
		return;
	if(MSIE6)
		return;
	if($('chat-wrapper').parentNode.id == 'chat-inline-placeholder')
		return;
	
	var totalHeight = $(document.body).getHeight();
	var viewHeight = document.viewport.getHeight();
	
	var scroll = document.viewport.getScrollOffsets().top;
	
	var offset = $('chat-wrapper').viewportOffset().top;
	if(Prototype.Browser.Opera)
		offset -= scroll;
	
	if(typeof __buggyFixedPosition != 'undefined')
	{
		$('chat-container').style.position = 'absolute';
		$('chat-container').style.top = ( scroll + viewHeight - $('chat-container').getHeight() ) + 'px';
	}
	else
	{		
		$('chat-container').style.bottom = '0px';
		/*if(offset + $('chat-wrapper').getHeight() > viewHeight)
		{
			$('chat-wrapper').addClassName('fixed');
		}
		else
		{
			$('chat-wrapper').removeClassName('fixed');
		}*/
	}
	
	/*var cont = $.select('.messages div').last();
	
	if(cont.scrollTop != cont.scrollHeight)
	{
		cont.scrollTop = cont.scrollHeight;
	}*/
}

function xml2array(node)
{	
	if( node.firstChild.nodeType == 3 )	// TEXT_NODE
		return node.textContent || node.text;

	var result = {};
	for(var i=0;i<node.childNodes.length;i++)
	{
		result[node.childNodes[i].tagName] = xml2array(node.childNodes[i]);
	}
	return result;
		
}


function getElementByAttribute( node, tagName, attr, value )
{
	var elems = node.getElementsByTagName(tagName);
	for(var i=0;i<elems.length;i++)
		if( elems[i].getAttribute(attr) == value )
			return elems[i];
	return null;
}

function log(msg) 
{
    $('test').insert({top: '<div style="margin-top: 10px;"></div>'});
    $('test').insert({top: msg});
}

function rawInput(data)
{
    log('RECV: ' + data.escapeHTML());
}

function rawOutput(data)
{
    log('<span style="color: #555;">SENT: ' + data.escapeHTML() + '</span>');
}

function toggleChatVisible()
{
	if( $('chat-wrapper').parentNode.id == 'chat-inline-placeholder' )
		return;
	
	$('chat-wrapper').toggleClassName('hidden');
	if( chat.params.game_id )
		setPrefCookie('chat-game-hide', $('chat-wrapper').hasClassName('hidden'));
	else
		setPrefCookie('chat-general-hide', $('chat-wrapper').hasClassName('hidden'));
	recalcFixedChat();
	
	if( $('chat-wrapper').hasClassName('hidden') )
	{
		if(chat.resize)
			delete chat.resize;
		if(chat.active_room == 'general')
			chat.leaveRoom('general');
		
		$$('#chat-title .mostright').last().title = 'Развернуть';
	}
	if( !$('chat-wrapper').hasClassName('hidden') )		
	{
		if(chat.active_room == 'general')
		{
			chat.enterRoom('general');
		}
		else
		{
			$$('#chat-title .game.c span').last().removeClassName('new');
			if(chat.blink_new_ingame)
			{
				clearInterval(chat.blink_new_ingame);
				chat.blink_new_ingame = null;
			}
		}
		
		$$('#chat-title .mostright').last().title = 'Cвернуть';
		
	}
}

function toggleChatFixed()
{
	if( $('chat-wrapper').hasClassName('hidden') )
		return;
		
	if( $('chat-wrapper').parentNode.id == 'chat-inline-placeholder' )
	{
		$('chat-fixed-placeholder').appendChild(
			$('chat-inline-placeholder').removeChild($('chat-wrapper')) );
		$('chat-inline-placeholder').hide();	
		
		setPrefCookie('inline_chat', false);
	}
	else
	{
		$('chat-inline-placeholder').show();		
		$('chat-inline-placeholder').appendChild(
			$('chat-fixed-placeholder').removeChild($('chat-wrapper')) );
		
		
		setPrefCookie('inline_chat', true);
		
		setChatHeight( __vk ? 40 : 120, true);
	}
	
	recalcFixedChat();
	
	
}

function setChatHeight(height, withMargin)
{
	var elems = $$('#chat-wrapper .messages-content');
	for(var i=0;i<elems.length;i++)
		elems[i].setStyle({height: height - 6 + 'px'});
	
	elems = $$('#chat-wrapper .userlist-content, #chat-wrapper .smile-tab');			
	for(var i=0;i<elems.length;i++)
		elems[i].setStyle({height: height + 26 + 'px'});
	
	if(withMargin)
		$('chat-wrapper').setStyle({marginTop: height + 5 + 'px'});
	
	setPrefCookie(chat.params.game_id ? 'chat-game-height' : 'chat-general-height', height - 6);
	
	recalcFixedChat();
}

function openSmileTab()
{
	$$('#chat-wrapper .userlist-content').each( function(el) { el.toggle(); } );
	$$('#chat-wrapper .smile-tab').each( function(el) { el.toggle(); } );
	$$('#chat-wrapper .smile-btn').each( function(el) { el.toggleClassName('active'); } );
}

function insertSmile(smile)
{
	var input = $('chat-'+chat.active_room).select('input.text').last();
	input.value += ':'+smile+':';
	input.focus();
}

function changeChatFilter()
{
	chat.filter = chat.filter ? 0 : 1;
	if(chat.filter)
		deletePrefCookie('no_chat_filter');
	else
		setPrefCookie('no_chat_filter', 1);
	$$('#chat-wrapper .filter-btn').each( function(el) { el.toggleClassName('active'); } );
}
	
Chat = function(params)
{
	var _this = this;

	this.params = params;
	this.user_list = new Object();
	this.user_data = new Object();
	this.cached_user_ids = new Object();
	this.resize = null;
	this.connected = false;
	this.blink_new_ingame = null;
	this.user_list_dirty = new Object();
	
	this.role = new Object();
	
	this.filter = 1;
	if(getPrefCookie('no_chat_filter'))	
		this.filter = 0;
	
	
	if( this.params.user )
	{
		if( this.params.user.background.charAt(0) == '#' )
			this.params.user.background = colortools.capByBrightness(this.params.user.background);
		else
			this.params.user.background = 'black';
	}
	
	this.rooms = ['general'];
	
	if(this.params.game_id)
	{
		this.rooms.push('game'+this.params.game_id);
		this.active_room = 'game'+this.params.game_id;
		this.changeActiveRoom('game'+this.params.game_id);
	}
	else
	{
		this.active_room = 'general';
		this.changeActiveRoom('general');
	}
	
	recalcFixedChat();	
	$$('#chat-title .mostright').last().observe( 'click', toggleChatVisible );	
	if(typeof this.params.game_id != 'undefined')
		$$('#chat-title .pin').last().observe( 'click', toggleChatFixed );
	$$('#chat-title .dummy .hide-bar').last().observe( 'click', toggleChatVisible );
	//$$('#chat-title .dummy').last().observe( 'click', shrinkChat );
	
}

Chat.prototype.connect = function()
	{
		var _this = this;
		this.connection = new Strophe.Connection('/xmpp-httpbind/');
		
		/*this.connection.rawInput = rawInput;
		this.connection.rawOutput = rawOutput;	*/
		
		var jid = 'jabber.klavogonki.ru/web';
		if(this.params.user)
			jid = this.params.user.id + '@' + jid;
		this.connection.connect(jid,
						       this.params.pass,
						       function(status) { _this.onConnect(status); } );
		
		this.connection.rawInput = function(data)
		{
			if(/\<body\s+type=['"]terminate['"]/.test(data))
			{
				_this.connection.connect(jid,
					       _this.params.pass,
					       function(status) 
				{ 
					_this.onConnect(status);
					if(_this.params.game_id && _this.active_room == 'general')
						_this.enterRoom('general');
					_this.changeActiveRoom(_this.active_room);
						
				} );
				
				_this.connected = false;
			}
		};
	}


Chat.prototype.onConnect = function(status)
{
	if (status == Strophe.Status.CONNECTING) {
		
    } else if (status == Strophe.Status.CONNFAIL) {
    	
    } else if (status == Strophe.Status.DISCONNECTING) {
    	
    } else if (status == Strophe.Status.DISCONNECTED) {
    	
    } else if (status == Strophe.Status.CONNECTED) {
				 
    	this.connected = true;
    	
    	var _this = this;
    	this.connection.addHandler(function(msg) { return _this.onMessage(msg); }, null, 'message', null, null,  null);
    	this.connection.addHandler(function(msg) { return _this.onPresence(msg); }, null, 'presence', null, null,  null);

    	for( var i=0;i<this.rooms.length;i++ )
    	{
	    	$('chat-'+this.rooms[i]).select('input.send').last().observe('click', (function(_this,i)	    			
			{
	    		return function()
	    		{
	    			_this.sendMsg(_this.rooms[i]);
	    		};
			}) (_this,i) );
    	}
    	 		
    	if(this.params.game_id)
    		this.enterRoom('game'+this.params.game_id);
    	else if( !$('chat-wrapper').hasClassName('hidden') )
    		this.enterRoom('general');
    	
    	if(this.params.game_id)
    	{
    		$$('#chat-title .general.c').last().observe( 'click', function() 
    		{
    			
    			_this.enterRoom('general');
    			
    			_this.changeActiveRoom('general');
    		} );
    		$$('#chat-title .game.c').last().observe( 'click', function() 
    		{
    			
    			
    			_this.leaveRoom('general');
    			
    			_this.changeActiveRoom('game'+_this.params.game_id);
    			
    		} );
    	}
    	
    	this.userlist_update_interval = setInterval( function()
    	{
    		for(var i in _this.rooms)
    		{
    			var room = _this.rooms[i];
    			if(_this.user_list_dirty[room])
    			{
    				_this.updateUserList(room);
    				_this.user_list_dirty[room] = false;
    			}
    		}
    	}, 2000 );
	
	
			
		if(this.params.user)
		{
			var els = $$('#chat-wrapper .send');			
			for( var i=0;i<els.length;i++ )
				els[i].disabled = false;
		}
    	
		
		/*this.connection.send(
			$iq( { from: this.connection.jid,
			 id: 'create1',
			 to: this.params.room+'@conference.jabber.klavogonki.ru',
			 type: 'get'} )
			.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#owner' } )
			.tree());*/
		
		
    }
};

Chat.prototype.changeActiveRoom = function(room)
{
	if(!this.connected && room != this.active_room)
		return;
	
	var _this = this;
	
	
	
	if(this.active_room)
		$('chat-'+this.active_room).hide();
	this.active_room = room;
	$('chat-'+this.active_room).show();
		
	if(room == 'general')
	{
		$$('#chat-general .messages > div > div').last().update('');
		$$('#chat-general .userlist-content').last().update('');
		
		var elems = $$('#chat-title .game');
		for( var i=0;i<elems.length;i++ )
			elems[i].removeClassName('active');
		var elems = $$('#chat-title .general');
		for( var i=0;i<elems.length;i++ )
			elems[i].addClassName('active');
	}
	else
	{
		var elems = $$('#chat-title .general');
		for( var i=0;i<elems.length;i++ )
			elems[i].removeClassName('active');
		var elems = $$('#chat-title .game');
		for( var i=0;i<elems.length;i++ )
			elems[i].addClassName('active');
		
		if(this.blink_new_ingame)
		{
			clearInterval(this.blink_new_ingame);
			this.blink_new_ingame = null;
		}
		$$('#chat-title .game.c span').last().removeClassName('new');
	}
	
	if(this.params.game_id)
	{
		setPrefCookie( 'chat_active_room', room == 'general' ? 'general' : 'game');
	}
	
	if(this.resize)
		delete this.resize;
	this.resize = new Resizeable($$('#chat-wrapper #chat-'+room+' .messages > div').last(), {
		handle: $$('#chat-title .dummy .resize-bar').last(),
		maxHeight: 400,
		resize: function(el)
		{			
			setChatHeight(el.getHeight(), true);
		},
		update: function(el)
		{
			setChatHeight(el.getHeight());
		} });
}

Chat.prototype.getXUserdata = function(items)
{
	var data = $build( 'x', { xmlns: 'klavogonki:userdata' } );
	
	data.c('user');
	
	for(var k in this.params.user)
		if( typeof(this.params.user[k]) == 'string' )
			data.c(k).t(this.params.user[k]).up();
	
	if( this.params.game_id )
		data.up().c('game_id').t(this.params.game_id);
	
	return data;
}


Chat.prototype.enterRoom = function(room)
{
	if(!this.connected)
		return;
	
	var pres = $pres( { 
		from: this.connection.jid,
		to: room+'@conference.jabber.klavogonki.ru/' + Strophe.getNodeFromJid(this.connection.jid) } )
	.c( 'x', { xmlns: 'http://jabber.org/protocol/muc' } )	
	.up();
	
	
	
	if( this.params.user )
	{	
		pres.cnode( this.getXUserdata().tree() );
	}
	
		
	this.connection.send( pres.tree() );

	this.user_list[room] = [];
}

Chat.prototype.leaveRoom = function(room)
{
	if(!this.connected)
		return;
	
	this.connection.send( $pres( { 
		from: this.connection.jid,
		to: room+'@conference.jabber.klavogonki.ru/' + Strophe.getNodeFromJid(this.connection.jid),
		type: 'unavailable' } )		
		.tree()); 
	
	$$('#chat-'+room+' .messages > div > div').last().update('');
	$$('#chat-'+room+' .userlist-content').last().update('');
}

Chat.prototype.sendPrivateMsg = function(user_id, msg)
{
	var user_name = this.cached_user_ids[user_id];
	this.addMsgInList({
		room: this.active_room,
		text: msg, 
		user_id: this.params.user.id, 
		time: new Date(), 
		type: 'private',
		to: user_name,
		to_id: user_id});
	
	this.connection.send(
			$msg( { 
				from: Strophe.getNodeFromJid(this.connection.jid)+'@jabber.klavogonki.ru/web',
				to: user_id+'@jabber.klavogonki.ru/web',
				type: 'chat' } )
			.c( 'body' )
			.t( msg )
			.up()
			.cnode( this.getXUserdata().tree() )
			.tree() );
	
	$('chat-'+this.active_room).select('input.text').last().value = '<'+user_name+'>';
}

Chat.prototype.sendMsg = function(room)
{
	if(!this.connected)
		return;
		
	var _this = this;
	var msg = $('chat-'+room).select('input.text').last().value;
	
	msg = msg.replace(/^ +$/,'');
	if(msg.length == 0)
		return;
		
	var to,type;
		
	var m = msg.match(/^<(.*?)>(.*)$/);	
	
	msg = msg.replace(/</,'&lt;');
	msg = msg.replace(/>/,'&gt;');
	
	msg = msg.replace(/&/,'&amp;');
	
	if(m)
	{
		var user_id = null;
		msg = m[2];
		
		var found_user_id = null;
		for( var i in this.cached_user_ids )
			if(this.cached_user_ids[i] == m[1])
				found_user_id = i;
		
		if(found_user_id)
			this.sendPrivateMsg(found_user_id, msg);
		else
		{		
			new Ajax.Request('/.fetchuser', {
				parameters: {
					login: m[1]},
				onSuccess: function(transport)
				{
						
					eval('var json='+transport.responseText+';');
					if(!json.id)
					{
						popalert('Пользователь с таким именем не найден.');
						return;
					}
					
					_this.cached_user_ids[json.id] = m[1];
						
					_this.sendPrivateMsg(json.id, msg);
						
					
				}});
		}	
			
		
		
	}
	else
	{	
		//if(Strophe.getNodeFromJid(this.connection.jid) == 21)
//			return;
		
		$('chat-'+room).select('input.text').last().value = '';
		
		this.connection.send(
				$msg( { 
					from: this.connection.jid,
					to: room+'@conference.jabber.klavogonki.ru',
					type: 'groupchat' } )
				.c( 'body' )
				.t( msg )
				.up()
				.cnode( this.getXUserdata().tree() )
				.tree() );
		
	}
		
	
	
	
}

Chat.prototype.onMessage = function(msg) 
{
	var _this = this;
	
    var to = msg.getAttribute('to');
    var from = msg.getAttribute('from');
    var elems = msg.getElementsByTagName('body');
    
    var room;
    
    
    if (elems.length > 0) {
		var body = elems[0];	
		
		var user_id, type;
		if(msg.getAttribute('type') == 'chat')
		{
			type = 'private';
			user_id = Strophe.getNodeFromJid(from);
			room = this.active_room;
		}
		else
		{
			type = 'normal';
			user_id = Strophe.getResourceFromJid(from);
			room = Strophe.getNodeFromJid(from);
		}
		
		var x = getElementByAttribute(msg, 'x', 'xmlns', 'klavogonki:userdata');
		if(x)
		{
			var data = xml2array(x);					
			if(data.user) {
				data.user = this.filterUserData(data.user);					
				this.user_data[user_id] = data;
			}
		}
			
		
		if(!this.user_data[user_id])
			return true;		
		
		var time = new Date();
		
		var x;
		if( x = getElementByAttribute(msg, 'x', 'xmlns', 'jabber:x:delay') )
		{
			var m = x.getAttribute('stamp').match(/(\d{4})(\d{2})(\d{2})T(\d{2}):(\d{2}):(\d{2})/);
			time = new Date( Date.UTC(m[1],m[2],m[3],m[4],m[5],m[6]) );
		}
		
		this.addMsgInList({
			room: room,
			text: Strophe.getText(body), 
			user_id: user_id, 
			time: time, 
			type: type });
		
		
		
    }

    return true;
}

Chat.prototype.addMsgInList = function(args)
{
	var user = this.user_data[args.user_id].user;
	
	var time = args.time.getHours() + ':' + args.time.getMinutes().format() + ':' + args.time.getSeconds().format();
	
	var room_html = '';
	if(args.type == 'game')
		room_html = '<span class=room>[заезд]</span>';
	if(args.type == 'private')
	{
		if(args.to)
			room_html = '<a href="javascript:chat.insertPrivate('+args.to_id+');" class="room private">[шепчет '+args.to+']</a>';
		else
			room_html = '<a href="javascript:chat.insertPrivate('+args.user_id+');" class="room private">[шепчет вам]</a>';
	}
	
	var cont_outer = $('chat-'+args.room).select('.messages > div').last();
	var cont = $('chat-'+args.room).select('.messages > div > div').last(); 
	
	var needScroll = (cont_outer.scrollTop+cont_outer.getHeight()) >= cont_outer.scrollHeight;
	
	args.text = args.text.replace(/</g, '&lt;');
	args.text = args.text.replace(/>/g, '&gt;');
	
	// game link parse
	args.text = args.text.replace(/http:\/\/(?:www\.)?klavogonki\.ru\/play\/\?gmid=(\d+)\&?/g, '[<a class="gamelink-not-resolved gamelink-$1" href="/play/?gmid=$1">Заезд #$1</a>]');
	args.text = args.text.replace(/http:\/\/([^ ]*)/g, '<a target=_blank href="http://$1">http://$1</a>');
	
	// мат
	if(this.filter)
	{
		var replace_str = '$1<span class=censored>[вырезано]</span>';
		args.text = args.text.replace(/­/g,'');
		args.text = ' '+args.text+' ';
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*[хxΧ]+[уy]+[eеийяё]+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[хxΧ]+[уy]+ю+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*м+у+д+[^рс]+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*[аеeoиоуыьъ]+[eеё]+б+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[eеё]+б+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*п+[иeеё]+[cсз]+д+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*б+л+я+д+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])б+л+я+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*п+и+д+[оеаeo]+[рp]+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[а-яА-Я]*г+[aoао]+в+[eеё]*н+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/([^а-яА-Я])[cс]+ц*у+[кч]+[eеaoаои]+[а-яА-Я]*(?=[^а-яА-Я])/gi, replace_str);
		args.text = args.text.replace(/^ (.*) $/,'$1');
	}
	
	// длинные строки
	while(/([^\/ ­]{40,})([^\/ ­]{40,})/.test(args.text))
	{
		args.text = args.text.replace(/([^\/ ­]{40,})([^\/ ­]{40,})/, '$1­$2');
	}
	args.text = args.text.replace(/­/, '<wbr/>');
	
	// смайлы
	var smilies = {
		smile: /(:-\)|:\)|:smile:)/g,
		biggrin: /(:-D|:D|:biggrin:)/g,
		angry: /(>:\(|:angry:)/g,
		blink: /(oO|Oo|o_O|O_o|оО|Оо|о_О|О_о|:blink:)/g,
		blush: /:blush:/g,
		cool: /(8\)|:cool:)/g,
		dry: /:dry:/g,
		excl: /:excl:/g,
		happy: /(\^\^|\^_\^|:happy:)/g,
		huh: /:huh:/g,
		laugh: /:laugh:/g,
		mellow: /:mellow:/g,
		ohmy: /:ohmy:/g,
		ph34r: /:ph34r:/g,
		rolleyes: /:rolleyes:/g,
		sad: /(:\(|:-\(|:sad:)/g,
		sleep: /:sleep:/g,
		tongue: /(:P|:-P|:Р|:-Р|:tongue:)/g,
		unsure: /:unsure:/g,
		wacko: /(\%\)|:wacko:)/g,
		wink: /(;\)|;-\)|:wink:)/g,
		wub: /:wub:/g,
		first: /:first:/g,
		second: /:second:/g,
		third: /:third:/g,
		power: /:power:/g,
		badcomp: /:badcomp:/g,
		complaugh: /:complaugh:/g,
		girlnotebook: /:girlnotebook:/g,
		crazy: /:crazy:/g,
		boredom: /:boredom:/g,
		cry: /:cry:/g,
		bye: /:bye:/g,
		dance: /:dance:/g,
		gamer: /:gamer:/g,
		rofl: /:rofl:/g,
		beer: /:beer:/g,
		kidtruck: /:kidtruck:/g,
		angry2: /:angry2:/g,
		spiteful: /:spiteful:/g,
		sorry: /:sorry:/g,
		boykiss: /:boykiss:/g,
		girlkiss: /(:girlkiss:|:\*|:-\*)/g,
		kissed: /:kissed:/g,
		yes: /:yes:/g,		
		no: /:no:/g,
		hi: /:hi:/g,
		ok: /:ok:/g		
	};

	for(var name in smilies)
	{			
		args.text = args.text.replace(smilies[name], '<img class=smile src="/img/smilies/'+name+'.gif" alt=":'+name+':" title=":'+name+':">');
	}
	
	args.text = args.text.replace(/script/g, 'sсript');
	
	if(args.type == 'system')
		cont.insert('<p><span class=time>['+time+']</span><span class=system-message>Пользователь '+user.login+' '+args.text+'</span></p>');
	else if(args.type == 'private')
		cont.insert('<p><span class=time>['+time+']</span><span class=username style="color:'+user.background+'">&lt;<span onclick="chat.insertPrefix('+args.user_id+');">'+user.login+'</span>&gt;</span>'+room_html+'<span class=private>'+args.text+'</span></p>');	
	else
		cont.insert('<p><span class=time>['+time+']</span><span class=username style="color:'+user.background+'">&lt;<span onclick="chat.insertPrefix('+args.user_id+');">'+user.login+'</span>&gt;</span>'+room_html+args.text+'</p>');
	
	// ссылки на игры
	var links = cont.select('.gamelink-not-resolved');
	for(var i=0;i<links.length;i++)
	{
		var m = links[i].className.match(/gamelink-(\d+)/);
		var els = $$('.gamelink-not-resolved.gamelink-'+m[1]);
		for(var j=0;j<els.length;j++)
			els[j].removeClassName('gamelink-not-resolved');
		new Ajax.Request( '/ajax/fetchgameinfo', {
			method: 'get',
			parameters: {
				game: m[1] },
			onSuccess: function(transport)
			{
				eval('var json='+transport.responseText+';');
				var text = '';
				if(json.type == 'practice')
					return;
				if(json.type == 'private')
					text = 'Игра с друзьями ';
				if(json.type == 'normal')
					text = 'Открытая игра ';
				if(json.competition)
				{
					text = 'Соревнование '
					if(json.regular_competition)
						text += '(x'+json.regular_competition+') ';
				}
				text += json.gametype_html;
				var els = $$('.gamelink-'+json.game_id);
				for(var j=0;j<els.length;j++)
					els[j].update(text);
			}});
	}
	
	
	
	if(needScroll)
		cont_outer.scrollTop = cont_outer.scrollHeight;
	
	// мигаем уведомлением
	if( args.room != 'general' && 
		(this.active_room == 'general' || $('chat-wrapper').hasClassName('hidden')) && 
		this.blink_new_ingame == null)
		this.blink_new_ingame = setInterval(function()
		{
			$$('#chat-title .game.c span').last().toggleClassName('new');
		}, 500);
}

Chat.prototype.onPresence = function(msg) 
{
	var user_id = Strophe.getResourceFromJid(msg.getAttribute('from'));
	var room = Strophe.getNodeFromJid(msg.getAttribute('from'));
	
	if(this.user_list[room] == undefined)
		this.user_list[room] = [];
	
	// REMOVE
	if(msg.getAttribute('type') == 'unavailable')
	{
		if( this.user_list[room].indexOf(user_id) != -1 )
			this.user_list[room].splice( this.user_list[room].indexOf(user_id), 1 );		
		this.user_list_dirty[room] = true;
		
		// кик
		var x = getElementByAttribute(msg, 'x', 'xmlns', 'http://jabber.org/protocol/muc#user');
		if(x)
		{
			var status = x.getElementsByTagName('status');
			if(status.length && status[0].getAttribute('code') == 307)
			{
				var item = x.getElementsByTagName('item');				
				var reason = item[0].childNodes[0];
				var reason_data = Strophe.getText(reason).split(':');
				
				var reason_periods = {
					1: '1 минуту',
					5: '5 минут',
					10: '10 минут',
					30: '30 минут',
					60: '1 час',
					180: '3 часа',
					360: '6 часов',
					720: '12 часов',
					1440: 'сутки',
					4320: '3 суток'};
				
				this.addMsgInList( {
					room: room,
					text: 'заблокирован на '+reason_periods[reason_data[1]]+' модератором '+reason_data[0]+' по причине: '+reason_data[2], 
					user_id: user_id, 
					time: new Date(), 
					type: 'system'
				});
				
				if(this.params.user && user_id == this.params.user.id)
				{
					$$('#chat-general .messages input.send').each(function (el){ el.disabled = true; });
					this.connection.disconnect();					
				}
			}
		}
	}
	// ERROR
	else if(msg.getAttribute('type') == 'error')
	{
		
	}
	// ADD 
	else
	{		
		if(this.user_list[room].indexOf(user_id) == -1)
		{	
			var x = getElementByAttribute(msg, 'x', 'xmlns', 'klavogonki:userdata');
			if(x)
			{
				var data = xml2array(x);					
				if(data.user)	// not invisible guest
				{
					data.user = this.filterUserData(data.user);			
					this.user_data[user_id] = data;
					this.user_list[room].push(user_id);
					this.user_list_dirty[room] = true;
				}
			}
			if(user_id == Strophe.getNodeFromJid(this.connection.jid))
			{
				x = getElementByAttribute(msg, 'x', 'xmlns', 'http://jabber.org/protocol/muc#user');
				var item = x.childNodes[0];
				this.role[room] = item.getAttribute('role');
			}
		}
	}
	
	
	return true;
}

Chat.prototype.updateUserList = function(room)
{
	var _this = this;
	
	this.user_list[room].sort( function (a,b) 
		{
			if(!_this.user_data[a])
				return 0;
			if(!_this.user_data[b])
				return 0;
			a_login = String.toLowerCase ? _this.user_data[a].user.login.toLowerCase() : _this.user_data[a].user.login;
			b_login = String.toLowerCase ? _this.user_data[b].user.login.toLowerCase() : _this.user_data[b].user.login;
			return ( a_login < b_login ? -1 : ( a_login > b_login ? 1 : 0 ) );
		} );
	
	var html = '';
	for(var i in this.user_list[room])
	{		
		var user_id = this.user_list[room][i];
		if(typeof user_id == 'function')
			continue;
		if(user_id == 21)
			continue;
		
		var item = this.user_data[user_id];
				
		var avatar_html = '';
		if(item.user.avatar)
			avatar_html = 'style="background: transparent url('+item.user.avatar+') no-repeat 0% 0%"';
		
		var game_html = '';
		if(item.game_id && room == 'general' )
			game_html = 'в игре';
		
		var moderator_tools = '';
		if(room == 'general' && this.params.user && this.params.user.moderator)
			moderator_tools = ' <div class=moderator-tools><a onclick="chat.kick('+user_id+');">&times;</a></div>';
		
		var icons = '<a class=info title="Профиль" href="/profile/'+user_id+'/"><img src="/img/information-small.png"></a>';
		if(item.user.moderator)
			icons += '<img src="/img/moderator_icon-2.gif" style="position: relative; top: 2px" title="Модератор">';
		
		html += '<ins class=user'+user_id+'><a class=name '+avatar_html+' title="Написать в приват" onclick="chat.insertPrivate('+user_id+');">'+item.user.login+'</a>'+icons+moderator_tools+game_html+'</ins>';
				
		
	}
	
	$('chat-'+room).select('.userlist-content').last().update(html);
}

Chat.prototype.insertPrivate = function(user_id)
{
	var input = $('chat-'+this.active_room).select('input.text').last();
	input.value = '<'+this.user_data[user_id].user.login+'>';
	input.focus();
}

Chat.prototype.insertPrefix = function(user_id)
{
	var input = $('chat-'+this.active_room).select('input.text').last();
	input.value += this.user_data[user_id].user.login+', ';
	input.focus();
}

Chat.prototype.kick = function(user_id)
{
	var item = this.user_data[user_id];
	var _this = this;
	
	
	popconfirm('Заблокировать пользователя '+item.user.login+' на <select id=chat_kick_period><option value=1>1 минуту</option><option value=5>5 минут</option><option value=10>10 минут</option><option value=30>30 минут</option><option value=60>1 час</option><option value=180>3 часа</option><option value=360>6 часов</option><option value=720>12 часов</option><option value=1440>сутки</option><option value=4320>3 суток</option></select><br/>Причина: <input type=text id=chat_kick_reason>',
		function()
		{
		
			new Ajax.Request('/ajax/chat-kick', {
				parameters: {
					user: user_id,
					period: $('chat_kick_period').value,
					reason: $('chat_kick_reason').value},
				onSuccess: function()
				{
					_this.connection.send(
							$iq( { 
								from: _this.connection.jid,
								id: 'kick1',
								to: 'general@conference.jabber.klavogonki.ru',
								type: 'set' } )
							.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#admin' } )
							.c( 'item', { nick: user_id, role: 'none' } )
							.c( 'reason' )
							.t( _this.params.user.login+':'+$('chat_kick_period').value+':'+$('chat_kick_reason').value )				
							.up().up().up()
							.tree() );
				}});
		});
}

Chat.prototype.grantModerator = function(user_id)
{
	this.connection.send(
			$iq( { 
				from: this.connection.jid,
				id: 'admin1',
				to: 'general@conference.jabber.klavogonki.ru',
				type: 'set' } )
			.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#admin' } )
			.c( 'item', { jid: user_id+'@jabber.klavogonki.ru', affiliation: 'admin' } )
			.up().up()
			.tree() );
	
	this.connection.send(
		$iq( { 
			from: this.connection.jid,
			id: 'mod1',
			to: 'general@conference.jabber.klavogonki.ru',
			type: 'set' } )
		.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#admin' } )
		.c( 'item', { nick: user_id, role: 'moderator' } )
		.up().up()
		.tree() );
}

Chat.prototype.revokeModerator = function(user_id)
{
	this.connection.send(
			$iq( { 
				from: this.connection.jid,
				id: 'admin2',
				to: 'general@conference.jabber.klavogonki.ru',
				type: 'set' } )
			.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#admin' } )
			.c( 'item', { jid: user_id+'@jabber.klavogonki.ru', affiliation: 'member' } )
			.up().up()
			.tree() );
	
	this.connection.send(
		$iq( { 
			from: this.connection.jid,
			id: 'mod2',
			to: 'general@conference.jabber.klavogonki.ru',
			type: 'set' } )
		.c( 'query', { xmlns: 'http://jabber.org/protocol/muc#admin' } )
		.c( 'item', { nick: user_id, role: 'participant' } )
		.up().up()
		.tree() );
}

Chat.prototype.filterUserData = function(user) {
	user.login = user.login.replace(/[^a-zA-Z0-9_\-а-яА-Я]*/g, '');
	user.login = user.login.substr(0,16);
	if(!/^http:\/\/img.klavogonki.ru\/avatars\/\d+\.gif$/.test(user.avatar))
		user.avatar = '';
	if(!/\#[A-Fa-f\d]+$/.test(user.background))
		user.background = '';
	return user;
}