var seckill = {
	//封装秒杀相关ajax的url
	URL:{
		now : function(){
			return 'seckill/time/now'
		},

		exposer : function(seckillId){
			return 'seckill/'+seckillId+'/exposer';
		},
		
		execution : function(seckillId,md5){
			return 'seckill/'+seckillId+'/'+md5+'/execution';
		}
	},
	
	//验证手机号
	validatePhone : function(phone){
		if(phone && phone.length == 11 && !isNaN(phone)){
			return true;
		}else{
			return false;
		}
	},
	
	//处理秒杀逻辑
	handleSeckillKill: function(seckillId,node){
		node.hide()
			.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			//在回调函数中，执行交互流程
			if(result && result['success']){
				var exposer = result['data'];
				if(exposer['exposed']){
					//开启了秒杀
					//获取秒杀地址
					var md5 = exposer['md5']
					var killUrl = seckill.URL.execution(seckillId, md5);
					console.log('killUrl:'+killUrl);
					//绑定一次点击事件
					$('#killBtn').one('click',function(){
						//绑定执行秒杀请求
						$(this).addClass('disabled');
						//发送秒杀请求
						$.post(killUrl,{},function(result){
							if(result && result['success']){
								var killResult = result['data'];
								var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];
								//显示秒杀结果
								node.html('<span class="label label-success">'+ stateInfo +'</soan>');
							}else{
								console.log('result:'+result);
							}
						});
					});
					node.show();
				}else{
					//未开启秒杀
					var now = exposer['now'];
					var start = exposer['start'];
					var end = exposer['end'];
					//重新倒计时
					seckill.countdown(seckillId,now,start,end);
				}
			}else{
				console.log("result:"+result);
			}
		});
	},
	
	//
	countdown: function(seckillId,nowTime,startTime,endTime){
		var seckillBox = $('#seckill-box');
		//时间判断
		if(nowTime > endTime){
			//秒杀结束
			seckillBox.html('秒杀结束');
		}else if(nowTime < startTime){
			//秒杀未开始
			var killTime = new Date(startTime + 1000);
			seckillBox.countdown(killTime,function(event){
				//控制时间的格式
				var format = event.strftime('秒杀倒计时： %D天 %H时 %M分 %S秒');
				seckillBox.html(format);
				//倒计时完成回调时间
			}).on('finish.countdown',function(){
				//获取秒杀地址,控制显示逻辑
				seckill.handleSeckillKill(seckillId,seckillBox);
				
			});
		}else{
			//秒杀开始
			seckill.handleSeckillKill(seckillId,seckillBox);
		}
	},
	//详情页秒杀逻辑
	detail:{
		//详情页初始化
		init : function(params){
			//用户手机验证和登录，计时交互
			//在cookie中查找手机号
			var killPhone = $.cookie('killPhone');
			if(!seckill.validatePhone(killPhone)){
				//绑定phone
				//控制输出
				var killPhoneModal = $('#killPhoneModal');
				//显示弹出层
				killPhoneModal.modal({
					show : true,
					backdrop : 'static',
					keyboard : false
				});
				
				$('#killPhoneBtn').click(function(){
					var inputPhone = $('#killPhoneKey').val();
					if(seckill.validatePhone(inputPhone)){
						//写入cookie
						$.cookie('killPhone',inputPhone,{expires:7,path:'/Imooc/seckill'});
						//刷新页面
						location.reload();
					}else{
						$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
					}
				});
			}
			
			//已经登录
			//计时交互
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			$.get(seckill.URL.now(),{},function(result){
				if(result && result['success']){
					var nowTime = result['data'];
					//时间判断,计时交互
					seckill.countdown(seckillId,nowTime,startTime,endTime);
				}else{
					console.log('result:'+result)
				}
			});
			
		}
	}
}