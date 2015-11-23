<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>File Upload Field Example</title>
		<link rel="stylesheet" type="text/css"
			href="ext/resources/css/ext-all.css" />
		<script type="text/javascript" src="ext/adapter/ext/ext-base.js"> </script>
		<script type="text/javascript" src="ext/ext-all.js"> </script>
		<style>
</style>
	</head>
	<body>
		<a href="http://blog.csdn.net/sunxing007">sunxing007</a>
		<div id="form"></div>
	</body>
	<script>
var fm = new Ext.FormPanel({
	title: '上传excel文件',
	url:'uploadController.jsp?t=' + new Date(),
	autoScroll:true,
	applyTo: 'form',
	height: 120,
	width: 500,
	frame:false,
	fileUpload: true,
	defaultType:'textfield',
	labelWidth:200,
	items:[{
		xtype:'field',
		fieldLabel:'请选择要上传的Excel文件 ',
		allowBlank:false,
		inputType:'file',
		name:'file'
	}],
	buttons: [{
		text: '开始上传',
		handler: function(){
			//点击'开始上传'之后，将由这个function来处理。
		    if(fm.form.isValid()){//验证form， 本例略掉了
		    //显示进度条
				Ext.MessageBox.show({ 
				    title: '正在上传文件', 
				    //msg: 'Processing...', 
				    width:240, 
				    progress:true, 
				    closable:false, 
				    buttons:{cancel:'Cancel'} 
				}); 
				//form提交
        fm.getForm().submit();
        //设置一个定时器，每500毫秒向processController发送一次ajax请求
		    var i = 0;
		    var timer = setInterval(function(){
		    		//请求事例
			      Ext.Ajax.request({
			      //下面的url的写法很关键，我为了这个调试了好半天
			      //以后凡是在ajax的请求的url上面都要带上日期戳，
			      //否则极有可能每次出现的数据都是一样的，
			      //这和浏览器缓存有关
						url: 'processController.jsp?t=' + new Date(),
						method: 'get',
						//处理ajax的返回数据
						success: function(response, options){
							status = response.responseText + " " + i++;
							var obj = Ext.util.JSON.decode(response.responseText);
							if(obj.success!=false){
								if(obj.finished){
									clearInterval(timer);	
									//status = response.responseText;
									Ext.MessageBox.updateProgress(1, 'finished', 'finished');
									Ext.MessageBox.hide();
								}
								else{
									Ext.MessageBox.updateProgress(obj.percentage, obj.msg);	
								}
							}
						},
						failure: function(){
							clearInterval(timer);
							Ext.Msg.alert('错误', '发生错误了。');
						} 
					});
		    }, 500);
		        
		    }
		    else{
		    	Ext.Msg.alert("消息","请先选择Excel文件再上传.");
		    }
		} 
	}]
});
</script>
</html>
