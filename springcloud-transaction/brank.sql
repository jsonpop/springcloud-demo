DROP TABLE IF EXISTS brank_account;
CREATE TABLE brank_account(
	aid VARCHAR(36) PRIMARY KEY,
	money int(6),
	nick VARCHAR(256)
);

insert into brank_account values("123456",100,"BrankA:张三");

insert into brank_account values("123456",100,"BrankB:张三");

select * from brank_account;