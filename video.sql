prompt PL/SQL Developer import file
prompt Created on 2021年5月25日 by ODouy
set feedback off
set define off
prompt Creating VADMIN...
create table VADMIN
(
  aid   NUMBER not null,
  apwd  VARCHAR2(20),
  aname VARCHAR2(20)
)
;
alter table VADMIN
  add primary key (AID);
alter table VADMIN
  add unique (ANAME);

prompt Creating VIDEO...
create table VIDEO
(
  vid       NUMBER not null,
  vname     VARCHAR2(1024),
  vdesc     VARCHAR2(1024),
  vtime     VARCHAR2(30),
  vdirector VARCHAR2(20),
  vactor    VARCHAR2(1024),
  vurl      VARCHAR2(1024),
  vimg      VARCHAR2(1024),
  vtype     NUMBER,
  vplaynum  NUMBER,
  vstorenum NUMBER
)
;
alter table VIDEO
  add primary key (VID);

prompt Creating VSTORE...
create table VSTORE
(
  vid NUMBER,
  pid NUMBER
)
;

prompt Creating VTYPE...
create table VTYPE
(
  tid   NUMBER,
  tname VARCHAR2(20)
)
;

prompt Creating VUSER...
create table VUSER
(
  id    NUMBER not null,
  uname VARCHAR2(20),
  upwd  VARCHAR2(20),
  usex  VARCHAR2(20),
  uage  NUMBER,
  utel  VARCHAR2(20),
  uimg  VARCHAR2(1024)
)
;
alter table VUSER
  add primary key (ID);
alter table VUSER
  add unique (UNAME);

prompt Disabling triggers for VADMIN...
alter table VADMIN disable all triggers;
prompt Disabling triggers for VIDEO...
alter table VIDEO disable all triggers;
prompt Disabling triggers for VSTORE...
alter table VSTORE disable all triggers;
prompt Disabling triggers for VTYPE...
alter table VTYPE disable all triggers;
prompt Disabling triggers for VUSER...
alter table VUSER disable all triggers;
prompt Loading VADMIN...
insert into VADMIN (aid, apwd, aname)
values (1, '123', 'admin');
commit;
prompt 1 records loaded
prompt Loading VIDEO...
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (9, '猫', '喵喵喵', '2021/05/24', '铲屎官', '咪咪', 'http://192.168.8.62:8080/videoplayer/resources/test/video/c732b37d-ae20-42c0-b2d2-1da3c35eb930.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/7d287f5d-07e6-4cf8-a0ac-73d66c66347b.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (24, 'aa', '111', '2021', 'sa', 'aa', 'http://192.168.8.62:8080/videoplayer/resources/test/video/418ab3f7-70ac-4aa2-a1b9-f6f40fce0a72.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/ada58317-5ce3-4431-ba75-b0c0b440f717.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (22, '狗狗', '123', '2021', '3', '小六', 'http://192.168.8.62:8080/videoplayer/resources/test/video/3b325755-779b-4f40-9f29-694382041514.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/a07b6268-733c-4fb0-81bd-c93f782ac5b0.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (23, '狗狗', '123', '2021', '3', '小六', 'http://192.168.8.62:8080/videoplayer/resources/test/video/9bf4085c-0f11-48e3-aeda-b1bdb155706b.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/fe57847c-61aa-4aa8-ae79-7950435925e4.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (2, '叶问2：宗师传奇', '叶问（甄子丹 饰）携家眷来到香港，艰难度日。叶问开武馆宣扬咏春拳术，但无人问津。某日，黄梁（黄晓明 饰）登门拜访，比武之后，甘愿携一班兄弟，拜在叶问门下。黄梁因故与洪拳门下斗殴，结果被捉。叶问来救，却巧遇昔日对手金山找（樊少皇 饰），两人冰释前嫌，却不想被洪拳掌门洪震南（洪金宝 饰）拦下，以致众人被警察（郑则仕 饰）拘捕。', '2010', '叶伟信', '甄子丹/洪金宝', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'http://image11.m1905.cn/uploadfile/2014/0812/thumb_1_270_360_20140812032218881755.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (3, '爱丽丝梦游仙境', '爱丽斯（米娅·华希科沃斯卡 Mia Wasikowska 饰）始终被同一个梦魇所困扰，直到她20岁时参加的一场聚会。本认为是一场无聊至极的聚会而已，没想到却是精心策划的求婚仪式。面对养尊处优的公子哥哈米什突如其来的求婚，毫无准备的爱丽斯着实被吓了一跳。爱丽斯被一只身马甲 的兔子所吸引，在追赶过程中爱丽斯掉入了一个深不见底的树洞里，来到了一个如同仙境般不可思议的“地下世界”。此时，生活在地下世界的善良人们似乎都在盼望着她的到来，可是她却认为这是一场梦，只想快点醒过来回到现实世界。然而......', '2019', '蒂姆·波顿', '米娅·华希科沃斯卡/约翰尼·德普/海伦娜·伯翰·卡特/安妮·海瑟薇/克里斯平·格洛弗/马特·卢卡斯/斯蒂芬·弗雷/麦克·辛/艾伦·瑞克曼/克里斯托弗·李  ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://tu.12tu.cc/uploads/10/1591219843.jpg', 4, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (4, '千与千寻222', '千寻和爸爸妈妈一同驱车前往新家，在郊外的小路上不慎进入了神秘的隧道——他们去到了另外一个诡异世界—一个中世纪的小镇。远处飘来食物的香味，爸爸妈妈大快朵颐，孰料之后变成了猪！这时小镇上渐渐来了许多样子古怪、半透明的人。', '2019', '宫崎骏', '柊瑠美 / 入野自由 / 夏木真理 / 菅原文太 / 中村彰男 ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic5.iqiyipic.com/image/20200908/b9/f0/v_129742881_m_601_m10_180_236.jpg', 5, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (5, '飞驰人生', '曾经在赛车界叱咤风云、如今却只能经营炒饭大排档的赛车手张驰决定重返车坛挑战年轻一代的天才。然而没钱没车没队友，甚至驾照都得重新考，这场笑料百出不断被打脸的复出之路，还有更多哭笑不得的窘境在等待着这位过气车神……', '2019', '韩寒', '沈腾 / 黄景瑜 / 尹正 / 尹昉 / 田雨 / 腾格尔 / 赵文瑄 / 张本煜', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic5.iqiyipic.com/image/20200908/95/ba/v_117911970_m_601_m26_180_236.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (6, '后来的我们', '这是一个爱情故事，关于一对异乡漂泊的年轻人。十年前，见清和小晓偶然地相识在归乡过年的火车上。两人怀揣着共同的梦想，一起在北京打拼，并开始了一段相聚相离的情感之路。十年后，见清和小晓在飞机上再次偶然重逢……命运似乎是一个轮回。在一次次的偶然下，平行线交叉，再平行，故事始终有“然后”。可后来的他们，学会如何去爱了吗？', '2019', '刘若英', '井柏然 / 周冬雨 / 田壮壮 / 苏小明 / 张子贤 / 施予斐 / 曲哲明 / 刘启恒 ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://p0.ssl.qhimgs1.com/sdr/400__/t015ad9b70de62fcf34.jpg', 3, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (7, '冰雪奇缘2（普通话)', '历经严酷考验，阿伦戴尔王国终于回归往日平静。艾莎女王、安娜公主以及他们的好友雪宝、克里斯托弗、驯鹿斯文过着平静安逸的生活。可是最近一段时间，艾莎总会被一段神秘的吟唱所困扰，为了追寻真相，她义无反顾踏上了征途。担心姐姐的安全，安娜和雪宝、克里斯托弗他们紧紧跟随。在那座常年被浓雾所笼罩的森林里，不仅藏着神秘的自然力量，更隐藏着关于阿伦戴尔王国、艾莎的魔法来源以及两位公主父母丧生等一系列的秘密。 艾莎开启了一段寻找自我的旅程……', '2019', '克里斯·巴克', ' 克里斯汀·贝尔 饰 安娜 伊迪娜·门泽尔 饰 艾莎 乔纳森·格罗夫 饰 克里斯托夫', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic4.iqiyipic.com/image/20200908/bf/67/v_144855000_m_601_m10_180_236.jpg', 5, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (13, '熊猫', '下辈子我也要当一只熊猫', '2021-5-24', '张琳瑶', '熊猫', 'http://192.168.8.62:8080/videoplayer/resources/test/video/f1d41b0c-b9e4-4361-90b7-71f39be6cc0e.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/4aeb0a97-0585-49e0-b200-bb48a2b1b3ad.jpg', 6, 0, 0);
commit;
prompt 11 records loaded
prompt Loading VSTORE...
insert into VSTORE (vid, pid)
values (2, 27);
insert into VSTORE (vid, pid)
values (9, 32);
insert into VSTORE (vid, pid)
values (9, 34);
insert into VSTORE (vid, pid)
values (9, 42);
insert into VSTORE (vid, pid)
values (2, 43);
insert into VSTORE (vid, pid)
values (9, 43);
insert into VSTORE (vid, pid)
values (7, 27);
insert into VSTORE (vid, pid)
values (6, 33);
insert into VSTORE (vid, pid)
values (2, 30);
insert into VSTORE (vid, pid)
values (7, 30);
commit;
prompt 10 records loaded
prompt Loading VTYPE...
insert into VTYPE (tid, tname)
values (1, '喜剧');
insert into VTYPE (tid, tname)
values (2, '动作');
insert into VTYPE (tid, tname)
values (3, '爱情');
insert into VTYPE (tid, tname)
values (4, '奇幻');
insert into VTYPE (tid, tname)
values (5, '动画');
insert into VTYPE (tid, tname)
values (6, '历史');
commit;
prompt 6 records loaded
prompt Loading VUSER...
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (27, 'aa', '123456', '女', 18, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/b616f903-ebb5-4bc8-8964-2dcf5d6e5452.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (33, '大大益', '147258', '女', 22, '15123943552', 'http://192.168.8.62:8080/videoplayer/resources/test/image/b6455d29-5b43-4cdb-a183-b139d62eb78e.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (34, '不是DD', '123456', '女', 21, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/cd85983c-d5df-4e93-8b0b-db377646da7f.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (31, '小o', '123456', '女', 16, '15815815815', 'http://192.168.8.62:8080/videoplayer/resources/test/image/0f509141-411a-40c9-8535-584cef8251ba.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (32, '张琳瑶', '1234567', '女', 18, '13611111111', 'http://192.168.8.62:8080/videoplayer/resources/test/image/61ede70a-be47-4b2f-9b62-55c066ccffce.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (41, 'LL', '123456', '女', 20, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/5d2c3c7e-0b41-4c5f-be60-85a6358751b8.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (43, 'cc', '1234567', '女', 18, '15123943552', 'http://192.168.8.62:8080/videoplayer/resources/test/image/d0a2ddd8-8721-41f4-a687-0a2e8e17c782.jpg');
commit;
prompt 7 records loaded
prompt Enabling triggers for VADMIN...
alter table VADMIN enable all triggers;
prompt Enabling triggers for VIDEO...
alter table VIDEO enable all triggers;
prompt Enabling triggers for VSTORE...
alter table VSTORE enable all triggers;
prompt Enabling triggers for VTYPE...
alter table VTYPE enable all triggers;
prompt Enabling triggers for VUSER...
alter table VUSER enable all triggers;
set feedback on
set define on
prompt Done.
