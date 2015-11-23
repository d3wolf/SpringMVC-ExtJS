Ext.onReady(function() {
	Ext.QuickTips.init();
	
	var location = (window.location+'').split('/');  
	var basePath = location[0]+'//'+location[2]+'/'+location[3]; 
	
	//主面板
	var centerPanel = window.centerPanel = new Ext.TabPanel({
		region : 'center',
		id : 'mainTabPanel',
		plugins: [new Ext.create('Ext.ux.TabCloseMenu',{
            closeTabText: '关闭面板',
            closeOthersTabsText: '关闭其他',
            closeAllTabsText: '关闭所有'
        }),new Ext.create('Ext.ux.dbClickCloseTab',{})],
		contentEl : "maincontent"
	});
	
	var store = Ext.create('Ext.data.TreeStore', {
		fields : [ 
		           {name:'internal', type:'string'},
		           {name:'text', type:'string'},
		           {name:'actionUrl', type:'string'},
		           {name:'id', type:'int'},
		           {name:'modefyTimestamp', type:'date', dateFormat : 'time'}
		         ],
		root : {
			display : '根节点',
			text : 'root',
			id : 1,
			expanded : true
		},
		proxy : {
			type : 'ajax',// 请求方式
			url : "menu/child.do"
		}
	});
	
	var navigatorTools = [{
		type : 'expand',
		tooltip : 'expand',
		handler : function() {
			Ext.getCmp('menu1').expandAll();
		}
	}, {
		type : 'collapse',
		tooltip : 'collapse',
		handler : function() {
			Ext.getCmp('menu1').collapseAll();
		}
	},{
		type : 'refresh',
		tooltip : 'refresh and expand',
		handler : function(){
			var tree = Ext.getCmp('menu1');
			tree.getStore().load({
	    		callback : function(records, operation, success){
					tree.expandAll();//load之后才能展开
	    		}
	    	});
		}
	}];
	
	var navigatorPanel = new Ext.Panel({
		region : 'west',
		collapsible : true,
		title : '左侧导航',
		xtype : 'panel',
		width : 180,
		autoScroll : true,
		layout: 'accordion',
		items : [{
			title : '面板2',
			xtype : "panel",
			items : [{
				tools : navigatorTools,
				xtype : 'treepanel',
				id : 'menu1',
				store : store,
				expanded : true,
				autoHeight:true,
				height: 800,
				autoScroll : true,
                containerScroll : true,
				rootVisible: false,
				listeners: {
					itemclick: function (view, record, item, index, e, eOpts) {
                        if (record.get('leaf') && record.get('actionUrl') != '') { //叶子节点
                        //	com.test.comm.utils.showTab(record.get('internal'),record.get('text'),record.get('actionUrl'));
                        	                      
                        	window.location.href=basePath  + '/#' + record.get('actionUrl'); 
                        }
                    }
                }
			}]
		}, {
			id : 'panel3',
			title : '面板3',
			xtype : "panel",
			html : "子元素3"
		} ]
	});
	
	Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [new Ext.Toolbar({
			region:'north',
			height:40,
			border:false,
			items : [{
				text : '首页',
				handler:function(){
				//	Ext.getCmp('home').show();
					window.location.href=basePath  + '/#jsp/homepage.jsp' ;
				}
			}]
		}), navigatorPanel, centerPanel]
	});
	
	handleHashChange();
});

window.onhashchange=function(){
//	alert(window.location.hash);
	handleHashChange();
}

var handleHashChange = function(){
	var hash = window.location.hash;
	if(hash != null && hash != '' || hash=='#'){
	}else{
	//	hash = '#jsp/navigator/homepage.jsp';//TODO:应换成homepage
	}
	var rightUrl = hash.replace("#", "");
	
	resetIframe(rightUrl);
}

function resetIframe(rightUrl){
	var iframe = document.getElementById("maincontentframe");
	document.getElementById("maincontent").removeChild(iframe);//先将旧的移除
	
	var newFrame = document.createElement("iframe");//创建新的iframe，id不变
	newFrame.setAttribute("id","maincontentframe");
	newFrame.setAttribute("src",rightUrl);
	newFrame.setAttribute("style","width:100%;height:100%;border:0px solid #ccc;");
    document.getElementById("maincontent").appendChild(newFrame);
    
    newFrame.onload = function() { //将iframe的title赋值到父窗口
    	var title = newFrame.contentWindow.document.title;
    	document.title = title;
    };
}

function post(URL, PARAMS) {   
    var frame = document.getElementById("maincontentframe"); 
    frame.style.display ="none"; //隐藏 
    
    //构造form
    var temp = document.createElement("form");      
    temp.action = URL; 
    temp.method = "post";
    temp.target="maincontentframe";
    alert(temp.target);
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp.appendChild(opt);
    }      
    document.body.appendChild(temp);
    temp.submit();
    document.body.removeChild(temp) ;
    //form提交完成

    frame.style.display ="block"; 

}  

var setAndEvalJs = function(html){
 	var hd = document.getElementsByTagName("head")[0];
 	var re = /(?:<script([^>]*)?>)((\n|\r|.)*?)(?:<\/script>)/ig;
 //	var re = /<script(.|\n)*?>(.|\n|\r\n)*?<\/script>/ig;
 	var srcRe = /\ssrc=([\'\"])(.*?)\1/i;
 	var typeRe = /\stype=([\'\"])(.*?)\1/i;
 	var match;
 	
 	var jsSrc = new Array();
 	var jsStr = new Array();
 	while(match = re.exec(html)){
		var attrs = match[1];
		var srcMatch = attrs ? attrs.match(srcRe) : false;
		if(srcMatch && srcMatch[2]){
			var s = document.createElement("script");
			s.src = srcMatch[2];
			var typeMatch = attrs.match(typeRe);
			if(typeMatch && typeMatch[2]){
				s.type = typeMatch[2];
			}
			jsSrc.push(s.src);
		}else if(match[2] && match[2].length > 0){
			jsStr.push(match[2]);
			if(window.execScript) {
				window.execScript(match[2]);
			} else {
				window.eval(match[2]);
			}
		}
	}

//	loadJs(jsSrc, jsStr);
}

function loadJs(jsSrcArr,jsStrArr){
	if(jsSrcArr.length>0){
		var src = jsSrcArr.pop();
		alert("载入: "+ src);
		jQuery.getScript(src, loadJs(jsSrcArr, jsStrArr));
	}else{
	//	alert("处理非src js****************** " + jsStrArr);
		for(x in jsStrArr){
			if(window.execScript) {
				window.execScript(jsStrArr[x]);
			} else {
				window.eval(jsStrArr[x]);
			}
		}
	}
}
