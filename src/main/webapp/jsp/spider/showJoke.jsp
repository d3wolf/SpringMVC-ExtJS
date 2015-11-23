<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Menu Manage</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>


<script type="text/javascript">
	function showJokePanel() {//根据不同的节点值,获取不同的加载数据(可以是节点ID\节点文本等等)
		Ext.tip.QuickTipManager.init();
		
	 	var jokeMainPanel = Ext.create('Ext.panel.Panel', {
		    title: "Jokes",
		    width: 800,
		    id : "jokeMainPanel",
		    bodyPadding: 5,
		    renderTo: Ext.getBody(),
		    bbar: Ext.create('Ext.toolbar.Paging', {
		        displayInfo: true,
		        items: [
		            '-', {
		                text: '第10页',
		                handler: function () {
		                    store.loadPage(10);
		                }
		            }]
		    }),
		    listeners: {
	            render: function() {
	            	var page = ${param.page};
	            	showJoke(page);
	            }
	        }
		}); 
	
	//	var page = ${param.page};
	//	showJoke(page);
	}
	
	function showJoke(page){
			
			Ext.Ajax.request({
			    url: "${pageContext.request.contextPath}/joke/show.do",
			    method : 'post',
			    params : {page : page},
			    success: function(response){
			    	var rpText = response.responseText;
			    	var json =  Ext.decode(rpText);
			    
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
	
	function createJokePanel(title, text){
		var jokePanel = Ext.create('Ext.panel.Panel', {
		    title: title,
		    width: 790,
		    name : "jokePanel",
		    bodyPadding: 5,
		 //   renderTo: Ext.getBody(),
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
