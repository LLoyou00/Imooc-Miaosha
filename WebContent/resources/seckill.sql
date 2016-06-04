
--数据库初始化脚本


--创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill

--创建秒杀库存表

CREATE TABLE seckill(
'seckill_id' SMALLINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
'name' VARCHAR(120) COMMENT '商品名称',
'number' INT NOT NULL COMMENT '库存数量',
'start_time' TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
'end time' TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
'create_time' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time (start_time),
KEY idx_end_time (end_time),
KEY idx_create_time (create_time),
)ENGINE=INNODB AUTO_INCREMENT = 1000 DEFAULT CHARSET('UTF-8') COMMENT='秒杀库存表';

--初始化数据

INSERT INTO
	seckill(name,number,start_time,end_time)
VALUES
	('1000元秒杀iphone6',100,'2016-05-30 00:00:00','2016-05-31 00:00:00'),
	('500元秒杀ipad2',200,'2016-06-01 00:00:00','2016-06-2 00:00:00'),
	('300元秒杀小米4',100,'2016-06-1 00:00:00','2016-06-2 00:00:00'),
	('200元秒杀红米note',100,'2016-06-1 00:00:00','2016-06-2 00:00:00');

--秒杀成功明细表

CREATE TABLE success_killed(
'seckill_id' SMALLINT NOT NULL COMMENT '秒杀商品ID',
'user_phone' SMALLINT NOT NULL COMMENT '用户手机号',
'state' TINYINT NOT NULL DEFAULT -1 COMMENT '状态表示:-1 无效; 0 成功; 1已付款; 2 已发货',
'create_time' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET('UTF-8') COMMENT='秒杀成功明细表';



--执行秒杀存储过程

DELIMITER $$

CREATE PROCEDURE `seckill`.`execute_seckill`
	(IN v_seckill_id BIGINT ,IN v_phone BIGINT,
		IN v_kill_time TIMESTAMP ,OUT r_result INT)
	BEGIN
		DECLARE insert_count INT DEFAULT 0;
		
		START TRANSACTION;
		
		INSERT IGNORE INTO success_killed
			(seckill_id,user_phone,create_time)
			VALUES(v_seckill_id, v_phone, v_kill_time);
		
		SELECT ROW_COUNT() INTO insert_count;

		IF(insert_count = 0) THEN
			ROLLBACK;
			SET r_result = -1;
		ELSEIF(insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -2;
		ELSE
			UPDATE seckill
			SET number = number -1
			WHERE seckill_id = v_seckill_id
				AND end_time > v_kill_time
				AND start_time < v_kill_time
				AND number > 0;
			
			SELECT ROW_COUNT() INTO insert_count;
			
			IF(insert_count = 0)THEN
				ROLLBACK;
				SET r_result = -1;
			ELSEIF(insert_count < 0) THEN
				ROLLBACK;
				SET r_result = -2;
			ELSE
				COMMIT;
				SET r_result = 1;
			END IF;
		END IF;
	END;
$$

DELIMITER ;

SET @r_result = -3;

CALL execute_seckill(1003,15606006487,now(),@r_result);

SELECT @r_result;








