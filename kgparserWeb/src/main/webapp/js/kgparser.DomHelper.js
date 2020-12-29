//Ext.namespace('su.opencode');

var DomHelper = function() {
	var CLASS_NAMES_SEPARATOR = ' ';

	return {
		TAG_OPEN: '<',
		TAG_CLOSE: '>',

		BODY_TAG: 'body',
		HEAD_TAG: 'head',

		SCRIPT_TAG: 'script',
		LINK_TAG: 'link',

		DIV_TAG: 'div',
		SPAN_TAG: 'span',
		A_TAG: 'a',

		BR_TAG: 'br',

		INPUT_TAG: 'input',
		LABEL_TAG: 'label',

		H1_TAG: 'h1',
		H2_TAG: 'h2',
		H3_TAG: 'h3',
		H4_TAG: 'h4',

		TABLE_TAG: 'table',
		TBODY_TAG: 'tbody',
		TR_TAG: 'tr',
		TH_TAG: 'th',
		TD_TAG: 'td',
		COL_GROUP_TAG: 'colgroup',
		COL_TAG: 'col',

		IMG_TAG: 'img',

		UL_TAG: 'ul',
		LI_TAG: 'li',

		ID_ATTRIBUTE: 'id',
		CLASS_ATTRIBUTE: 'class',
		STYLE_ATTRIBUTE: 'style',
		TITLE_ATTRIBUTE: 'title',
		FOR_ATTRIBUTE: 'for',
		ROW_SPAN_ATTRIBUTE: 'rowspan',
		COL_SPAN_ATTRIBUTE: 'colspan',

		SRC_ATTRIBUTE: 'src',
		ALT_ATTRIBUTE: 'alt',
		TYPE_ATTRIBUTE: 'type',
		NAME_ATTRIBUTE: 'name',
		VALUE_ATTRIBUTE: 'value',
		WIDTH_ATTRIBUTE: 'width',
		HEIGHT_ATTRIBUTE: 'height',
		MAX_LENGTH_ATTRIBUTE: 'maxlength',
		PATTERN_ATTRIBUTE: 'pattern',
		CHARSET_ATTRIBUTE: 'charset',
		REL_ATTRIBUTE: 'rel',
		HREF_ATTRIBUTE: 'href',
		TAB_INDEX_ATTRIBUTE: 'tabindex',

		HIDDEN_TYPE_ATTRIBUTE_VALUE: 'hidden',
		TEXT_TYPE_ATTRIBUTE_VALUE: 'text',
		CHECKBOX_TYPE_ATTRIBUTE_VALUE: 'checkbox',
		RADIO_TYPE_ATTRIBUTE_VALUE: 'radio',
		PASSWORD_TYPE_ATTRIBUTE_VALUE: 'password',
		BUTTON_TYPE_ATTRIBUTE_VALUE: 'button',

		getEl: function(id) {
			return document.getElementById(id);
		},

		createEl: function(tag, id, cls, text) {
			var el = document.createElement(tag);

			if (id)
				el.id = id;

			if (cls)
			{
				var cssClass = DomHelper.getClass(cls);
				if ( cssClass )
					el.className = cssClass;
			}

			if (text)
				el.appendChild(document.createTextNode(text));

			return el;
		},

		createTN: function(text) {
			return document.createTextNode(text);
		},

		createScriptEl: function(src) {
			var scriptElement = document.createElement(DomHelper.SCRIPT_TAG);
			scriptElement.setAttribute(DomHelper.TYPE_ATTRIBUTE, 'text/javascript');
			scriptElement.setAttribute(DomHelper.SRC_ATTRIBUTE, src);
			scriptElement.setAttribute(DomHelper.CHARSET_ATTRIBUTE, 'UTF-8');

			return scriptElement;
		},

		createImg: function(src, alt) {
			var imgElement = document.createElement(DomHelper.IMG_TAG);
			imgElement.setAttribute(DomHelper.SRC_ATTRIBUTE, src);
			imgElement.setAttribute(DomHelper.ALT_ATTRIBUTE, alt || '');

			return imgElement;
		},

		appendCss: function(cssUrl, headId) {
			if ( !cssUrl )
				return;

			var cssLink = document.createElement(DomHelper.LINK_TAG);
			cssLink.setAttribute(DomHelper.REL_ATTRIBUTE, 'stylesheet');
			cssLink.setAttribute(DomHelper.TYPE_ATTRIBUTE, 'text/css');
			cssLink.setAttribute(DomHelper.HREF_ATTRIBUTE, cssUrl);

			var headElement = document.getElementsByTagName(DomHelper.HEAD_TAG)[0];
			if ( headId )
				headElement = document.getElementById(headId);

			if ( !headElement )
			{
				alert('No head element for append css link to');
				return;
			}

			headElement.appendChild(cssLink);
		},

		insertAfter: function(nodeBefore, nodeToInsert) {
			if ( !nodeBefore )
				return;

			var parentNode = nodeBefore.parentNode;
			if (nodeBefore.nextSibling)
				parentNode.insertBefore(nodeToInsert, nodeBefore.nextSibling); // append before next sibling
			else
				parentNode.appendChild(nodeToInsert); // append last
		},
		insertBefore: function(parentNode, nodeAfter, nodeToInsert) {
			if ( !nodeAfter )
				parentNode.appendChild(nodeToInsert); // append last
			else
				parentNode.insertBefore(nodeToInsert, nodeAfter);
		},

		getClass: function(classNames) {
			if (!classNames)
				return null;

			if ( !(classNames instanceof Array ) )
				return classNames;

			var className = '';
			for (var i = 0; i < classNames.length; i++)
				className += (classNames[i] + CLASS_NAMES_SEPARATOR);

			className = className.substring( 0, (className.length - CLASS_NAMES_SEPARATOR.length) ); // remove trailing space
			return className;
		},

		removeChildren: function(element) {
			while (element.hasChildNodes()) {
				element.removeChild(element.lastChild);
			}
		},

		removeChildrenHierarchy: function(element) {
			while (element.hasChildNodes()) {
				DomHelper.removeChildrenHierarchy(element.lastChild);
				element.removeChild(element.lastChild);
			}
		},

		removeNbsps: function(str) {
			if ( !str || !str.length )
				return '';

			return str.replace('&nbsp;', ' ');
		}
	}
}();
