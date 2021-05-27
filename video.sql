prompt PL/SQL Developer import file
prompt Created on 2021��5��25�� by ODouy
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
values (9, 'è', '������', '2021/05/24', '��ʺ��', '����', 'http://192.168.8.62:8080/videoplayer/resources/test/video/c732b37d-ae20-42c0-b2d2-1da3c35eb930.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/7d287f5d-07e6-4cf8-a0ac-73d66c66347b.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (24, 'aa', '111', '2021', 'sa', 'aa', 'http://192.168.8.62:8080/videoplayer/resources/test/video/418ab3f7-70ac-4aa2-a1b9-f6f40fce0a72.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/ada58317-5ce3-4431-ba75-b0c0b440f717.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (22, '����', '123', '2021', '3', 'С��', 'http://192.168.8.62:8080/videoplayer/resources/test/video/3b325755-779b-4f40-9f29-694382041514.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/a07b6268-733c-4fb0-81bd-c93f782ac5b0.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (23, '����', '123', '2021', '3', 'С��', 'http://192.168.8.62:8080/videoplayer/resources/test/video/9bf4085c-0f11-48e3-aeda-b1bdb155706b.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/fe57847c-61aa-4aa8-ae79-7950435925e4.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (2, 'Ҷ��2����ʦ����', 'Ҷ�ʣ����ӵ� �Σ�Я�Ҿ�������ۣ����Ѷ��ա�Ҷ�ʿ��������ӽ��ȭ�����������ʽ�ĳ�գ������������� �Σ����Űݷã�����֮�󣬸�ԸЯһ���ֵܣ�����Ҷ�����¡�����������ȭ���¶�Ź�������׽��Ҷ�����ȣ�ȴ�������ն��ֽ�ɽ�ң����ٻ� �Σ������˱���ǰ�ӣ�ȴ���뱻��ȭ���ź����ϣ���� �Σ����£��������˱����죨֣���� �Σ��в���', '2010', 'Ҷΰ��', '���ӵ�/���', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'http://image11.m1905.cn/uploadfile/2014/0812/thumb_1_270_360_20140812032218881755.jpg', 2, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (3, '����˿�����ɾ�', '����˹����櫡���ϣ����˹�� Mia Wasikowska �Σ�ʼ�ձ�ͬһ�����������ţ�ֱ����20��ʱ�μӵ�һ���ۻᡣ����Ϊ��һ�����������ľۻ���ѣ�û�뵽ȴ�Ǿ��Ĳ߻��������ʽ����������ŵĹ��Ӹ����ʲͻ����������飬����׼���İ���˹��ʵ������һ��������˹��һֻ����� ����������������׷�Ϲ����а���˹������һ������׵������������һ����ͬ�ɾ��㲻��˼��ġ��������硱����ʱ�������ڵ�����������������ƺ��������������ĵ�����������ȴ��Ϊ����һ���Σ�ֻ�����ѹ����ص���ʵ���硣Ȼ��......', '2019', '��ķ������', '��櫡���ϣ����˹��/Լ���ᡤ����/�����ȡ�����������/���ݡ���ɪޱ/����˹ƽ�����帥/���ء�¬��˹/˹�ٷҡ�����/��ˡ���/���ס������/����˹�и�����  ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://tu.12tu.cc/uploads/10/1591219843.jpg', 4, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (4, 'ǧ��ǧѰ222', 'ǧѰ�Ͱְ�����һͬ����ǰ���¼ң��ڽ����С·�ϲ������������ص������������ȥ��������һ���������硪һ�������͵�С��Զ��Ʈ��ʳ�����ζ���ְ���������ã�����֮����������ʱС���Ͻ�������������ӹŹ֡���͸�����ˡ�', '2019', '���鿥', '������ / ��Ұ���� / ��ľ���� / ��ԭ��̫ / �д����� ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic5.iqiyipic.com/image/20200908/b9/f0/v_129742881_m_601_m10_180_236.jpg', 5, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (5, '�ɳ�����', '������������߳����ơ����ȴֻ�ܾ�Ӫ�������ŵ����������ų۾����ط���̳��ս����һ������š�Ȼ��ûǮû��û���ѣ��������ն������¿����ⳡЦ�ϰٳ����ϱ������ĸ���֮·�����и����Ц���õľ����ڵȴ�����λ�������񡭡�', '2019', '����', '���� / �ƾ�� / ���� / ���P / ���� / �ڸ�� / ���Ĭu / �ű���', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic5.iqiyipic.com/image/20200908/95/ba/v_117911970_m_601_m26_180_236.jpg', 1, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (6, '����������', '����һ��������£�����һ������Ư���������ˡ�ʮ��ǰ�������С��żȻ����ʶ�ڹ������Ļ��ϡ����˻����Ź�ͬ�����룬һ���ڱ�����ƴ������ʼ��һ�������������֮·��ʮ��󣬼����С���ڷɻ����ٴ�żȻ�طꡭ�������ƺ���һ���ֻء���һ�δε�żȻ�£�ƽ���߽��棬��ƽ�У�����ʼ���С�Ȼ�󡱡��ɺ��������ǣ�ѧ�����ȥ������', '2019', '����Ӣ', '����Ȼ / �ܶ��� / ��׳׳ / ��С�� / ������ / ʩ��� / ������ / ������ ', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://p0.ssl.qhimgs1.com/sdr/400__/t015ad9b70de62fcf34.jpg', 3, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (7, '��ѩ��Ե2����ͨ��)', '�����Ͽῼ�飬���״����������ڻع�����ƽ������ɯŮ�������ȹ����Լ����ǵĺ���ѩ��������˹�и���ѱ¹˹�Ĺ���ƽ�����ݵ�����������һ��ʱ�䣬��ɯ�ܻᱻһ�����ص����������ţ�Ϊ��׷Ѱ���࣬�����޷���̤������;�����Ľ��İ�ȫ�����Ⱥ�ѩ��������˹�и����ǽ������档���������걻Ũ�������ֵ�ɭ��������������ص���Ȼ�������������Ź��ڰ��״�����������ɯ��ħ����Դ�Լ���λ������ĸɥ����һϵ�е����ܡ� ��ɯ������һ��Ѱ�����ҵ��ó̡���', '2019', '����˹���Ϳ�', ' ����˹͡������ �� ���� �����ȡ������ �� ��ɯ ����ɭ�����޷� �� ����˹�з�', 'http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4', 'https://pic4.iqiyipic.com/image/20200908/bf/67/v_144855000_m_601_m10_180_236.jpg', 5, 0, 0);
insert into VIDEO (vid, vname, vdesc, vtime, vdirector, vactor, vurl, vimg, vtype, vplaynum, vstorenum)
values (13, '��è', '�±�����ҲҪ��һֻ��è', '2021-5-24', '������', '��è', 'http://192.168.8.62:8080/videoplayer/resources/test/video/f1d41b0c-b9e4-4361-90b7-71f39be6cc0e.mp4', 'http://192.168.8.62:8080/videoplayer/resources/test/image/4aeb0a97-0585-49e0-b200-bb48a2b1b3ad.jpg', 6, 0, 0);
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
values (1, 'ϲ��');
insert into VTYPE (tid, tname)
values (2, '����');
insert into VTYPE (tid, tname)
values (3, '����');
insert into VTYPE (tid, tname)
values (4, '���');
insert into VTYPE (tid, tname)
values (5, '����');
insert into VTYPE (tid, tname)
values (6, '��ʷ');
commit;
prompt 6 records loaded
prompt Loading VUSER...
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (27, 'aa', '123456', 'Ů', 18, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/b616f903-ebb5-4bc8-8964-2dcf5d6e5452.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (33, '�����', '147258', 'Ů', 22, '15123943552', 'http://192.168.8.62:8080/videoplayer/resources/test/image/b6455d29-5b43-4cdb-a183-b139d62eb78e.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (34, '����DD', '123456', 'Ů', 21, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/cd85983c-d5df-4e93-8b0b-db377646da7f.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (31, 'Сo', '123456', 'Ů', 16, '15815815815', 'http://192.168.8.62:8080/videoplayer/resources/test/image/0f509141-411a-40c9-8535-584cef8251ba.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (32, '������', '1234567', 'Ů', 18, '13611111111', 'http://192.168.8.62:8080/videoplayer/resources/test/image/61ede70a-be47-4b2f-9b62-55c066ccffce.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (41, 'LL', '123456', 'Ů', 20, '15203395653', 'http://192.168.8.62:8080/videoplayer/resources/test/image/5d2c3c7e-0b41-4c5f-be60-85a6358751b8.jpg');
insert into VUSER (id, uname, upwd, usex, uage, utel, uimg)
values (43, 'cc', '1234567', 'Ů', 18, '15123943552', 'http://192.168.8.62:8080/videoplayer/resources/test/image/d0a2ddd8-8721-41f4-a687-0a2e8e17c782.jpg');
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
