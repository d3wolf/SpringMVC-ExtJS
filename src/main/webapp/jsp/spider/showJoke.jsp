<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Show Jokes</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
	function showJokePanel() {//根据不同的节点值,获取不同的加载数据(可以是节点ID\节点文本等等)
		Ext.tip.QuickTipManager.init();
		var location = (window.location+'').split('/');  
		var basePath = location[0]+'//'+location[2]+'/'+location[3]; 
		
	 	var jokeMainPanel = Ext.create('Ext.panel.Panel', {
		    title: "page: ${param.page}",
		    width: 800,
		    id : "jokeMainPanel",
		    bodyPadding: 5,
		    renderTo: Ext.getBody(),
			tools : [ {
				type : 'prev',
				tooltip : '前一页',
				handler : function(){
					var page = ${param.page};
					var prev = page - 1;
					if(prev > 0){
						if(location.indexOf("#")>0){
							window.parent.location.href=basePath  + '/#' + "showJoke.do?page="+prev;
						}else{
							window.location.href=basePath + "/showJoke.do?page="+prev;
						}
					}
				}
			}, {
				type : 'next',
				tooltip : '下一页',
				handler : function(){
					var page = ${param.page};
					var next = page + 1;
				//	window.parent.location.href=basePath  + '/#' + "showJoke.do?page="+next;
					if(location.indexOf("#")>0){
						window.parent.location.href=basePath  + '/#' + "showJoke.do?page="+next;
					}else{
						window.location.href=basePath + "/showJoke.do?page="+next;
					}
				}
			} ],
			listeners : {
				render : function() {
					var page = ${param.page};
					showJoke(page);
				}
			}
		});
	}

	function showJoke(page) {

		Ext.Ajax.request({
			url : "${pageContext.request.contextPath}/joke/show.do",
			method : 'post',
			params : {
				page : page
			},
			success : function(response) {
				var rpText = response.responseText;
				var json = Ext.decode(rpText);

				for (var i = 0; i < json.length; i++) {
					var title = json[i].title;
					var text = json[i].text;
					createJokePanel(title, text);
				}
			},
			failure : function(response, opts) {
				Ext.MessageBox.alert("错误", "获取失败:\n" + response.responseText);
			}
		});
	}

	function createJokePanel(title, text) {
		var jokePanel = Ext.create('Ext.panel.Panel', {
			title : title,
			width : 790,
			name : "jokePanel",
			bodyPadding : 5,
			html : text
		});

		var jokeMainPanel = Ext.getCmp('jokeMainPanel');
		jokeMainPanel.add(jokePanel);
	}

	Ext.onReady(showJokePanel);
</script>
</head>
<body>
</body>
