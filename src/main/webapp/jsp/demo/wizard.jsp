<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<head>
<title>Combo</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javascript/extjs/resources/css/ext-all.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	Ext.QuickTips.init();
	var navigate = function(panel, direction){
	    // 此程序可以包含一些控制导航步骤的必要业务逻辑. 比如调用setActiveItem, 管理导航按钮的状态,
	    // 处理可能出现的分支逻辑, 处理特殊操作像取消或结束等等. 一个完整的向导页, 对于复杂的需求
	    // 实现起来可能也会相当复杂, 在实际的程序中通常应该以继承CardLayout的方式来实现. 
	    var layout = panel.getLayout();
	    layout[direction]();
	    Ext.getCmp('move-prev').setDisabled(!layout.getPrev());
	    Ext.getCmp('move-next').setDisabled(!layout.getNext());
	};
	
	Ext.create('Ext.panel.Panel', {
	    title: 'Example Wizard',
	    width: 500,
	    height: 500,
	    layout: 'card',
	    bodyStyle: 'padding:15px',
	    defaults: {
	        // 应用到所有子面板
	        border: false
	    },
	
	    // 这里仅仅用几个按钮来示例一种可能的导航场景.
	    bbar: [
	        {
	            id: 'move-prev',
	            text: 'Back',
	            handler: function(btn) {
	                navigate(btn.up("panel"), "prev");
	            },
	            disabled: true
	        },
	     //   '->', // 一个长间隔, 使两个按钮分布在两边
	        {
	            id: 'move-next',
	            text: 'Next',
	            handler: function(btn) {
	                navigate(btn.up("panel"), "next");
	            }
	        }
	    ],
	    // 布局下的各子面板
	    items: [{
	        id: 'card-0',
	        html: '<h1>Welcome to the Wizard!</h1><p>Step 1 of 3</p>'
	    },{
	        id: 'card-1',
	        html: '<p>Step 2 of 3</p>'
	    },{
	        id: 'card-2',
	        html: '<h1>Congratulations!</h1><p>Step 3 of 3 - Complete</p>'
	    }],
	    renderTo: Ext.getBody()
	});
});



</script>
</head>
<body>

</body>