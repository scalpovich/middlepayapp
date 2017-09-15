$(function () {
    var dataJson = [
        {
            "value": "110000",
            "name": "北京市",
            "shortName": "北京",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "110100",
                    "name": "北京市",
                    "shortName": "北京",
                    "parentId": "110000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "120000",
            "name": "天津市",
            "shortName": "天津",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "120100",
                    "name": "天津市",
                    "shortName": "天津",
                    "parentId": "120000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "130000",
            "name": "河北省",
            "shortName": "河北",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "130100",
                    "name": "石家庄市",
                    "shortName": "石家庄",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130200",
                    "name": "唐山市",
                    "shortName": "唐山",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130300",
                    "name": "秦皇岛市",
                    "shortName": "秦皇岛",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130400",
                    "name": "邯郸市",
                    "shortName": "邯郸",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130500",
                    "name": "邢台市",
                    "shortName": "邢台",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130600",
                    "name": "保定市",
                    "shortName": "保定",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130700",
                    "name": "张家口市",
                    "shortName": "张家口",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130800",
                    "name": "承德市",
                    "shortName": "承德",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "130900",
                    "name": "沧州市",
                    "shortName": "沧州",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "131000",
                    "name": "廊坊市",
                    "shortName": "廊坊",
                    "parentId": "130000",
                    "level": "2"
                },
                {
                    "value": "131100",
                    "name": "衡水市",
                    "shortName": "衡水",
                    "parentId": "130000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "140000",
            "name": "山西省",
            "shortName": "山西",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "140100",
                    "name": "太原市",
                    "shortName": "太原",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140200",
                    "name": "大同市",
                    "shortName": "大同",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140300",
                    "name": "阳泉市",
                    "shortName": "阳泉",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140400",
                    "name": "长治市",
                    "shortName": "长治",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140500",
                    "name": "晋城市",
                    "shortName": "晋城",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140600",
                    "name": "朔州市",
                    "shortName": "朔州",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140700",
                    "name": "晋中市",
                    "shortName": "晋中",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140800",
                    "name": "运城市",
                    "shortName": "运城",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "140900",
                    "name": "忻州市",
                    "shortName": "忻州",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "141000",
                    "name": "临汾市",
                    "shortName": "临汾",
                    "parentId": "140000",
                    "level": "2"
                },
                {
                    "value": "141100",
                    "name": "吕梁市",
                    "shortName": "吕梁",
                    "parentId": "140000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "150000",
            "name": "内蒙古自治区",
            "shortName": "内蒙古",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "150100",
                    "name": "呼和浩特市",
                    "shortName": "呼和浩特",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150200",
                    "name": "包头市",
                    "shortName": "包头",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150300",
                    "name": "乌海市",
                    "shortName": "乌海",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150400",
                    "name": "赤峰市",
                    "shortName": "赤峰",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150500",
                    "name": "通辽市",
                    "shortName": "通辽",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150600",
                    "name": "鄂尔多斯市",
                    "shortName": "鄂尔多斯",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150700",
                    "name": "呼伦贝尔市",
                    "shortName": "呼伦贝尔",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150800",
                    "name": "巴彦淖尔市",
                    "shortName": "巴彦淖尔",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "150900",
                    "name": "乌兰察布市",
                    "shortName": "乌兰察布",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "152200",
                    "name": "兴安盟",
                    "shortName": "兴安盟",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "152500",
                    "name": "锡林郭勒盟",
                    "shortName": "锡林郭勒盟",
                    "parentId": "150000",
                    "level": "2"
                },
                {
                    "value": "152900",
                    "name": "阿拉善盟",
                    "shortName": "阿拉善盟",
                    "parentId": "150000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "210000",
            "name": "辽宁省",
            "shortName": "辽宁",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "210100",
                    "name": "沈阳市",
                    "shortName": "沈阳",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210200",
                    "name": "大连市",
                    "shortName": "大连",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210300",
                    "name": "鞍山市",
                    "shortName": "鞍山",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210400",
                    "name": "抚顺市",
                    "shortName": "抚顺",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210500",
                    "name": "本溪市",
                    "shortName": "本溪",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210600",
                    "name": "丹东市",
                    "shortName": "丹东",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210700",
                    "name": "锦州市",
                    "shortName": "锦州",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210800",
                    "name": "营口市",
                    "shortName": "营口",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "210900",
                    "name": "阜新市",
                    "shortName": "阜新",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211000",
                    "name": "辽阳市",
                    "shortName": "辽阳",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211100",
                    "name": "盘锦市",
                    "shortName": "盘锦",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211200",
                    "name": "铁岭市",
                    "shortName": "铁岭",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211300",
                    "name": "朝阳市",
                    "shortName": "朝阳",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211400",
                    "name": "葫芦岛市",
                    "shortName": "葫芦岛",
                    "parentId": "210000",
                    "level": "2"
                },
                {
                    "value": "211500",
                    "name": "金普新区",
                    "shortName": "金普新区",
                    "parentId": "210000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "220000",
            "name": "吉林省",
            "shortName": "吉林",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "220100",
                    "name": "长春市",
                    "shortName": "长春",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220200",
                    "name": "吉林市",
                    "shortName": "吉林",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220300",
                    "name": "四平市",
                    "shortName": "四平",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220400",
                    "name": "辽源市",
                    "shortName": "辽源",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220500",
                    "name": "通化市",
                    "shortName": "通化",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220600",
                    "name": "白山市",
                    "shortName": "白山",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220700",
                    "name": "松原市",
                    "shortName": "松原",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "220800",
                    "name": "白城市",
                    "shortName": "白城",
                    "parentId": "220000",
                    "level": "2"
                },
                {
                    "value": "222400",
                    "name": "延边朝鲜族自治州",
                    "shortName": "延边",
                    "parentId": "220000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "230000",
            "name": "黑龙江省",
            "shortName": "黑龙江",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "230100",
                    "name": "哈尔滨市",
                    "shortName": "哈尔滨",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230200",
                    "name": "齐齐哈尔市",
                    "shortName": "齐齐哈尔",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230300",
                    "name": "鸡西市",
                    "shortName": "鸡西",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230400",
                    "name": "鹤岗市",
                    "shortName": "鹤岗",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230500",
                    "name": "双鸭山市",
                    "shortName": "双鸭山",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230600",
                    "name": "大庆市",
                    "shortName": "大庆",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230700",
                    "name": "伊春市",
                    "shortName": "伊春",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230800",
                    "name": "佳木斯市",
                    "shortName": "佳木斯",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "230900",
                    "name": "七台河市",
                    "shortName": "七台河",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "231000",
                    "name": "牡丹江市",
                    "shortName": "牡丹江",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "231100",
                    "name": "黑河市",
                    "shortName": "黑河",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "231200",
                    "name": "绥化市",
                    "shortName": "绥化",
                    "parentId": "230000",
                    "level": "2"
                },
                {
                    "value": "232700",
                    "name": "大兴安岭地区",
                    "shortName": "大兴安岭",
                    "parentId": "230000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "310000",
            "name": "上海市",
            "shortName": "上海",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "310100",
                    "name": "上海市",
                    "shortName": "上海",
                    "parentId": "310000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "320000",
            "name": "江苏省",
            "shortName": "江苏",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "320100",
                    "name": "南京市",
                    "shortName": "南京",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320200",
                    "name": "无锡市",
                    "shortName": "无锡",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320300",
                    "name": "徐州市",
                    "shortName": "徐州",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320400",
                    "name": "常州市",
                    "shortName": "常州",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320500",
                    "name": "苏州市",
                    "shortName": "苏州",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320600",
                    "name": "南通市",
                    "shortName": "南通",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320700",
                    "name": "连云港市",
                    "shortName": "连云港",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320800",
                    "name": "淮安市",
                    "shortName": "淮安",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "320900",
                    "name": "盐城市",
                    "shortName": "盐城",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "321000",
                    "name": "扬州市",
                    "shortName": "扬州",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "321100",
                    "name": "镇江市",
                    "shortName": "镇江",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "321200",
                    "name": "泰州市",
                    "shortName": "泰州",
                    "parentId": "320000",
                    "level": "2"
                },
                {
                    "value": "321300",
                    "name": "宿迁市",
                    "shortName": "宿迁",
                    "parentId": "320000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "330000",
            "name": "浙江省",
            "shortName": "浙江",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "330100",
                    "name": "杭州市",
                    "shortName": "杭州",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330200",
                    "name": "宁波市",
                    "shortName": "宁波",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330300",
                    "name": "温州市",
                    "shortName": "温州",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330400",
                    "name": "嘉兴市",
                    "shortName": "嘉兴",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330500",
                    "name": "湖州市",
                    "shortName": "湖州",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330600",
                    "name": "绍兴市",
                    "shortName": "绍兴",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330700",
                    "name": "金华市",
                    "shortName": "金华",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330800",
                    "name": "衢州市",
                    "shortName": "衢州",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "330900",
                    "name": "舟山市",
                    "shortName": "舟山",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "331000",
                    "name": "台州市",
                    "shortName": "台州",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "331100",
                    "name": "丽水市",
                    "shortName": "丽水",
                    "parentId": "330000",
                    "level": "2"
                },
                {
                    "value": "331200",
                    "name": "舟山群岛新区",
                    "shortName": "舟山新区",
                    "parentId": "330000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "340000",
            "name": "安徽省",
            "shortName": "安徽",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "340100",
                    "name": "合肥市",
                    "shortName": "合肥",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340200",
                    "name": "芜湖市",
                    "shortName": "芜湖",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340300",
                    "name": "蚌埠市",
                    "shortName": "蚌埠",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340400",
                    "name": "淮南市",
                    "shortName": "淮南",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340500",
                    "name": "马鞍山市",
                    "shortName": "马鞍山",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340600",
                    "name": "淮北市",
                    "shortName": "淮北",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340700",
                    "name": "铜陵市",
                    "shortName": "铜陵",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "340800",
                    "name": "安庆市",
                    "shortName": "安庆",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341000",
                    "name": "黄山市",
                    "shortName": "黄山",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341100",
                    "name": "滁州市",
                    "shortName": "滁州",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341200",
                    "name": "阜阳市",
                    "shortName": "阜阳",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341300",
                    "name": "宿州市",
                    "shortName": "宿州",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341500",
                    "name": "六安市",
                    "shortName": "六安",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341600",
                    "name": "亳州市",
                    "shortName": "亳州",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341700",
                    "name": "池州市",
                    "shortName": "池州",
                    "parentId": "340000",
                    "level": "2"
                },
                {
                    "value": "341800",
                    "name": "宣城市",
                    "shortName": "宣城",
                    "parentId": "340000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "350000",
            "name": "福建省",
            "shortName": "福建",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "350100",
                    "name": "福州市",
                    "shortName": "福州",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350200",
                    "name": "厦门市",
                    "shortName": "厦门",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350300",
                    "name": "莆田市",
                    "shortName": "莆田",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350400",
                    "name": "三明市",
                    "shortName": "三明",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350500",
                    "name": "泉州市",
                    "shortName": "泉州",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350600",
                    "name": "漳州市",
                    "shortName": "漳州",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350700",
                    "name": "南平市",
                    "shortName": "南平",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350800",
                    "name": "龙岩市",
                    "shortName": "龙岩",
                    "parentId": "350000",
                    "level": "2"
                },
                {
                    "value": "350900",
                    "name": "宁德市",
                    "shortName": "宁德",
                    "parentId": "350000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "360000",
            "name": "江西省",
            "shortName": "江西",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "360100",
                    "name": "南昌市",
                    "shortName": "南昌",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360200",
                    "name": "景德镇市",
                    "shortName": "景德镇",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360300",
                    "name": "萍乡市",
                    "shortName": "萍乡",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360400",
                    "name": "九江市",
                    "shortName": "九江",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360500",
                    "name": "新余市",
                    "shortName": "新余",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360600",
                    "name": "鹰潭市",
                    "shortName": "鹰潭",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360700",
                    "name": "赣州市",
                    "shortName": "赣州",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360800",
                    "name": "吉安市",
                    "shortName": "吉安",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "360900",
                    "name": "宜春市",
                    "shortName": "宜春",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "361000",
                    "name": "抚州市",
                    "shortName": "抚州",
                    "parentId": "360000",
                    "level": "2"
                },
                {
                    "value": "361100",
                    "name": "上饶市",
                    "shortName": "上饶",
                    "parentId": "360000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "370000",
            "name": "山东省",
            "shortName": "山东",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "370100",
                    "name": "济南市",
                    "shortName": "济南",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370200",
                    "name": "青岛市",
                    "shortName": "青岛",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370300",
                    "name": "淄博市",
                    "shortName": "淄博",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370400",
                    "name": "枣庄市",
                    "shortName": "枣庄",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370500",
                    "name": "东营市",
                    "shortName": "东营",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370600",
                    "name": "烟台市",
                    "shortName": "烟台",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370700",
                    "name": "潍坊市",
                    "shortName": "潍坊",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370800",
                    "name": "济宁市",
                    "shortName": "济宁",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "370900",
                    "name": "泰安市",
                    "shortName": "泰安",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371000",
                    "name": "威海市",
                    "shortName": "威海",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371100",
                    "name": "日照市",
                    "shortName": "日照",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371200",
                    "name": "莱芜市",
                    "shortName": "莱芜",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371300",
                    "name": "临沂市",
                    "shortName": "临沂",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371400",
                    "name": "德州市",
                    "shortName": "德州",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371500",
                    "name": "聊城市",
                    "shortName": "聊城",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371600",
                    "name": "滨州市",
                    "shortName": "滨州",
                    "parentId": "370000",
                    "level": "2"
                },
                {
                    "value": "371700",
                    "name": "菏泽市",
                    "shortName": "菏泽",
                    "parentId": "370000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "410000",
            "name": "河南省",
            "shortName": "河南",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "410100",
                    "name": "郑州市",
                    "shortName": "郑州",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410200",
                    "name": "开封市",
                    "shortName": "开封",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410300",
                    "name": "洛阳市",
                    "shortName": "洛阳",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410400",
                    "name": "平顶山市",
                    "shortName": "平顶山",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410500",
                    "name": "安阳市",
                    "shortName": "安阳",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410600",
                    "name": "鹤壁市",
                    "shortName": "鹤壁",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410700",
                    "name": "新乡市",
                    "shortName": "新乡",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410800",
                    "name": "焦作市",
                    "shortName": "焦作",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "410900",
                    "name": "濮阳市",
                    "shortName": "濮阳",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411000",
                    "name": "许昌市",
                    "shortName": "许昌",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411100",
                    "name": "漯河市",
                    "shortName": "漯河",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411200",
                    "name": "三门峡市",
                    "shortName": "三门峡",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411300",
                    "name": "南阳市",
                    "shortName": "南阳",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411400",
                    "name": "商丘市",
                    "shortName": "商丘",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411500",
                    "name": "信阳市",
                    "shortName": "信阳",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411600",
                    "name": "周口市",
                    "shortName": "周口",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "411700",
                    "name": "驻马店市",
                    "shortName": "驻马店",
                    "parentId": "410000",
                    "level": "2"
                },
                {
                    "value": "419000",
                    "name": "直辖县级",
                    "shortName": "直辖县",
                    "parentId": "410000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "420000",
            "name": "湖北省",
            "shortName": "湖北",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "420100",
                    "name": "武汉市",
                    "shortName": "武汉",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420200",
                    "name": "黄石市",
                    "shortName": "黄石",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420300",
                    "name": "十堰市",
                    "shortName": "十堰",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420500",
                    "name": "宜昌市",
                    "shortName": "宜昌",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420600",
                    "name": "襄阳市",
                    "shortName": "襄阳",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420700",
                    "name": "鄂州市",
                    "shortName": "鄂州",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420800",
                    "name": "荆门市",
                    "shortName": "荆门",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "420900",
                    "name": "孝感市",
                    "shortName": "孝感",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "421000",
                    "name": "荆州市",
                    "shortName": "荆州",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "421100",
                    "name": "黄冈市",
                    "shortName": "黄冈",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "421200",
                    "name": "咸宁市",
                    "shortName": "咸宁",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "421300",
                    "name": "随州市",
                    "shortName": "随州",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "422800",
                    "name": "恩施土家族苗族自治州",
                    "shortName": "恩施",
                    "parentId": "420000",
                    "level": "2"
                },
                {
                    "value": "429000",
                    "name": "直辖县级",
                    "shortName": "直辖县",
                    "parentId": "420000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "430000",
            "name": "湖南省",
            "shortName": "湖南",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "430100",
                    "name": "长沙市",
                    "shortName": "长沙",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430200",
                    "name": "株洲市",
                    "shortName": "株洲",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430300",
                    "name": "湘潭市",
                    "shortName": "湘潭",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430400",
                    "name": "衡阳市",
                    "shortName": "衡阳",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430500",
                    "name": "邵阳市",
                    "shortName": "邵阳",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430600",
                    "name": "岳阳市",
                    "shortName": "岳阳",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430700",
                    "name": "常德市",
                    "shortName": "常德",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430800",
                    "name": "张家界市",
                    "shortName": "张家界",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "430900",
                    "name": "益阳市",
                    "shortName": "益阳",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "431000",
                    "name": "郴州市",
                    "shortName": "郴州",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "431100",
                    "name": "永州市",
                    "shortName": "永州",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "431200",
                    "name": "怀化市",
                    "shortName": "怀化",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "431300",
                    "name": "娄底市",
                    "shortName": "娄底",
                    "parentId": "430000",
                    "level": "2"
                },
                {
                    "value": "433100",
                    "name": "湘西土家族苗族自治州",
                    "shortName": "湘西",
                    "parentId": "430000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "440000",
            "name": "广东省",
            "shortName": "广东",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "440100",
                    "name": "广州市",
                    "shortName": "广州",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440200",
                    "name": "韶关市",
                    "shortName": "韶关",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440300",
                    "name": "深圳市",
                    "shortName": "深圳",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440400",
                    "name": "珠海市",
                    "shortName": "珠海",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440500",
                    "name": "汕头市",
                    "shortName": "汕头",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440600",
                    "name": "佛山市",
                    "shortName": "佛山",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440700",
                    "name": "江门市",
                    "shortName": "江门",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440800",
                    "name": "湛江市",
                    "shortName": "湛江",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "440900",
                    "name": "茂名市",
                    "shortName": "茂名",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441200",
                    "name": "肇庆市",
                    "shortName": "肇庆",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441300",
                    "name": "惠州市",
                    "shortName": "惠州",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441400",
                    "name": "梅州市",
                    "shortName": "梅州",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441500",
                    "name": "汕尾市",
                    "shortName": "汕尾",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441600",
                    "name": "河源市",
                    "shortName": "河源",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441700",
                    "name": "阳江市",
                    "shortName": "阳江",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441800",
                    "name": "清远市",
                    "shortName": "清远",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "441900",
                    "name": "东莞市",
                    "shortName": "东莞",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "442000",
                    "name": "中山市",
                    "shortName": "中山",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "445100",
                    "name": "潮州市",
                    "shortName": "潮州",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "445200",
                    "name": "揭阳市",
                    "shortName": "揭阳",
                    "parentId": "440000",
                    "level": "2"
                },
                {
                    "value": "445300",
                    "name": "云浮市",
                    "shortName": "云浮",
                    "parentId": "440000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "450000",
            "name": "广西壮族自治区",
            "shortName": "广西",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "450100",
                    "name": "南宁市",
                    "shortName": "南宁",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450200",
                    "name": "柳州市",
                    "shortName": "柳州",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450300",
                    "name": "桂林市",
                    "shortName": "桂林",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450400",
                    "name": "梧州市",
                    "shortName": "梧州",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450500",
                    "name": "北海市",
                    "shortName": "北海",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450600",
                    "name": "防城港市",
                    "shortName": "防城港",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450700",
                    "name": "钦州市",
                    "shortName": "钦州",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450800",
                    "name": "贵港市",
                    "shortName": "贵港",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "450900",
                    "name": "玉林市",
                    "shortName": "玉林",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "451000",
                    "name": "百色市",
                    "shortName": "百色",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "451100",
                    "name": "贺州市",
                    "shortName": "贺州",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "451200",
                    "name": "河池市",
                    "shortName": "河池",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "451300",
                    "name": "来宾市",
                    "shortName": "来宾",
                    "parentId": "450000",
                    "level": "2"
                },
                {
                    "value": "451400",
                    "name": "崇左市",
                    "shortName": "崇左",
                    "parentId": "450000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "460000",
            "name": "海南省",
            "shortName": "海南",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "460100",
                    "name": "海口市",
                    "shortName": "海口",
                    "parentId": "460000",
                    "level": "2"
                },
                {
                    "value": "460200",
                    "name": "三亚市",
                    "shortName": "三亚",
                    "parentId": "460000",
                    "level": "2"
                },
                {
                    "value": "460300",
                    "name": "三沙市",
                    "shortName": "三沙",
                    "parentId": "460000",
                    "level": "2"
                },
                {
                    "value": "469000",
                    "name": "直辖县级",
                    "shortName": "直辖县",
                    "parentId": "460000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "500000",
            "name": "重庆市",
            "shortName": "重庆",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "500100",
                    "name": "重庆市",
                    "shortName": "重庆",
                    "parentId": "500000",
                    "level": "2"
                },
                {
                    "value": "500300",
                    "name": "两江新区",
                    "shortName": "两江新区",
                    "parentId": "500000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "510000",
            "name": "四川省",
            "shortName": "四川",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "510100",
                    "name": "成都市",
                    "shortName": "成都",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510300",
                    "name": "自贡市",
                    "shortName": "自贡",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510400",
                    "name": "攀枝花市",
                    "shortName": "攀枝花",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510500",
                    "name": "泸州市",
                    "shortName": "泸州",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510600",
                    "name": "德阳市",
                    "shortName": "德阳",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510700",
                    "name": "绵阳市",
                    "shortName": "绵阳",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510800",
                    "name": "广元市",
                    "shortName": "广元",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "510900",
                    "name": "遂宁市",
                    "shortName": "遂宁",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511000",
                    "name": "内江市",
                    "shortName": "内江",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511100",
                    "name": "乐山市",
                    "shortName": "乐山",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511300",
                    "name": "南充市",
                    "shortName": "南充",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511400",
                    "name": "眉山市",
                    "shortName": "眉山",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511500",
                    "name": "宜宾市",
                    "shortName": "宜宾",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511600",
                    "name": "广安市",
                    "shortName": "广安",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511700",
                    "name": "达州市",
                    "shortName": "达州",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511800",
                    "name": "雅安市",
                    "shortName": "雅安",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "511900",
                    "name": "巴中市",
                    "shortName": "巴中",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "512000",
                    "name": "资阳市",
                    "shortName": "资阳",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "513200",
                    "name": "阿坝藏族羌族自治州",
                    "shortName": "阿坝",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "513300",
                    "name": "甘孜藏族自治州",
                    "shortName": "甘孜",
                    "parentId": "510000",
                    "level": "2"
                },
                {
                    "value": "513400",
                    "name": "凉山彝族自治州",
                    "shortName": "凉山",
                    "parentId": "510000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "520000",
            "name": "贵州省",
            "shortName": "贵州",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "520100",
                    "name": "贵阳市",
                    "shortName": "贵阳",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "520200",
                    "name": "六盘水市",
                    "shortName": "六盘水",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "520300",
                    "name": "遵义市",
                    "shortName": "遵义",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "520400",
                    "name": "安顺市",
                    "shortName": "安顺",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "520500",
                    "name": "毕节市",
                    "shortName": "毕节",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "520600",
                    "name": "铜仁市",
                    "shortName": "铜仁",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "522300",
                    "name": "黔西南布依族苗族自治州",
                    "shortName": "黔西南",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "522600",
                    "name": "黔东南苗族侗族自治州",
                    "shortName": "黔东南",
                    "parentId": "520000",
                    "level": "2"
                },
                {
                    "value": "522700",
                    "name": "黔南布依族苗族自治州",
                    "shortName": "黔南",
                    "parentId": "520000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "530000",
            "name": "云南省",
            "shortName": "云南",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "530100",
                    "name": "昆明市",
                    "shortName": "昆明",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530300",
                    "name": "曲靖市",
                    "shortName": "曲靖",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530400",
                    "name": "玉溪市",
                    "shortName": "玉溪",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530500",
                    "name": "保山市",
                    "shortName": "保山",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530600",
                    "name": "昭通市",
                    "shortName": "昭通",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530700",
                    "name": "丽江市",
                    "shortName": "丽江",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530800",
                    "name": "普洱市",
                    "shortName": "普洱",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "530900",
                    "name": "临沧市",
                    "shortName": "临沧",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "532300",
                    "name": "楚雄彝族自治州",
                    "shortName": "楚雄",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "532500",
                    "name": "红河哈尼族彝族自治州",
                    "shortName": "红河",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "532600",
                    "name": "文山壮族苗族自治州",
                    "shortName": "文山",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "532800",
                    "name": "西双版纳傣族自治州",
                    "shortName": "西双版纳",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "532900",
                    "name": "大理白族自治州",
                    "shortName": "大理",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "533100",
                    "name": "德宏傣族景颇族自治州",
                    "shortName": "德宏",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "533300",
                    "name": "怒江傈僳族自治州",
                    "shortName": "怒江",
                    "parentId": "530000",
                    "level": "2"
                },
                {
                    "value": "533400",
                    "name": "迪庆藏族自治州",
                    "shortName": "迪庆",
                    "parentId": "530000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "540000",
            "name": "西藏自治区",
            "shortName": "西藏",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "540100",
                    "name": "拉萨市",
                    "shortName": "拉萨",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "540200",
                    "name": "日喀则市",
                    "shortName": "日喀则",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "540300",
                    "name": "昌都市",
                    "shortName": "昌都",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "542200",
                    "name": "山南地区",
                    "shortName": "山南",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "542400",
                    "name": "那曲地区",
                    "shortName": "那曲",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "542500",
                    "name": "阿里地区",
                    "shortName": "阿里",
                    "parentId": "540000",
                    "level": "2"
                },
                {
                    "value": "542600",
                    "name": "林芝地区",
                    "shortName": "林芝",
                    "parentId": "540000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "610000",
            "name": "陕西省",
            "shortName": "陕西",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "610100",
                    "name": "西安市",
                    "shortName": "西安",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610200",
                    "name": "铜川市",
                    "shortName": "铜川",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610300",
                    "name": "宝鸡市",
                    "shortName": "宝鸡",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610400",
                    "name": "咸阳市",
                    "shortName": "咸阳",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610500",
                    "name": "渭南市",
                    "shortName": "渭南",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610600",
                    "name": "延安市",
                    "shortName": "延安",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610700",
                    "name": "汉中市",
                    "shortName": "汉中",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610800",
                    "name": "榆林市",
                    "shortName": "榆林",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "610900",
                    "name": "安康市",
                    "shortName": "安康",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "611000",
                    "name": "商洛市",
                    "shortName": "商洛",
                    "parentId": "610000",
                    "level": "2"
                },
                {
                    "value": "611100",
                    "name": "西咸新区",
                    "shortName": "西咸",
                    "parentId": "610000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "620000",
            "name": "甘肃省",
            "shortName": "甘肃",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "620100",
                    "name": "兰州市",
                    "shortName": "兰州",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620200",
                    "name": "嘉峪关市",
                    "shortName": "嘉峪关",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620300",
                    "name": "金昌市",
                    "shortName": "金昌",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620400",
                    "name": "白银市",
                    "shortName": "白银",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620500",
                    "name": "天水市",
                    "shortName": "天水",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620600",
                    "name": "武威市",
                    "shortName": "武威",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620700",
                    "name": "张掖市",
                    "shortName": "张掖",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620800",
                    "name": "平凉市",
                    "shortName": "平凉",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "620900",
                    "name": "酒泉市",
                    "shortName": "酒泉",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "621000",
                    "name": "庆阳市",
                    "shortName": "庆阳",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "621100",
                    "name": "定西市",
                    "shortName": "定西",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "621200",
                    "name": "陇南市",
                    "shortName": "陇南",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "622900",
                    "name": "临夏回族自治州",
                    "shortName": "临夏",
                    "parentId": "620000",
                    "level": "2"
                },
                {
                    "value": "623000",
                    "name": "甘南藏族自治州",
                    "shortName": "甘南",
                    "parentId": "620000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "630000",
            "name": "青海省",
            "shortName": "青海",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "630100",
                    "name": "西宁市",
                    "shortName": "西宁",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "630200",
                    "name": "海东市",
                    "shortName": "海东",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632200",
                    "name": "海北藏族自治州",
                    "shortName": "海北",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632300",
                    "name": "黄南藏族自治州",
                    "shortName": "黄南",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632500",
                    "name": "海南藏族自治州",
                    "shortName": "海南",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632600",
                    "name": "果洛藏族自治州",
                    "shortName": "果洛",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632700",
                    "name": "玉树藏族自治州",
                    "shortName": "玉树",
                    "parentId": "630000",
                    "level": "2"
                },
                {
                    "value": "632800",
                    "name": "海西蒙古族藏族自治州",
                    "shortName": "海西",
                    "parentId": "630000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "640000",
            "name": "宁夏回族自治区",
            "shortName": "宁夏",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "640100",
                    "name": "银川市",
                    "shortName": "银川",
                    "parentId": "640000",
                    "level": "2"
                },
                {
                    "value": "640200",
                    "name": "石嘴山市",
                    "shortName": "石嘴山",
                    "parentId": "640000",
                    "level": "2"
                },
                {
                    "value": "640300",
                    "name": "吴忠市",
                    "shortName": "吴忠",
                    "parentId": "640000",
                    "level": "2"
                },
                {
                    "value": "640400",
                    "name": "固原市",
                    "shortName": "固原",
                    "parentId": "640000",
                    "level": "2"
                },
                {
                    "value": "640500",
                    "name": "中卫市",
                    "shortName": "中卫",
                    "parentId": "640000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "650000",
            "name": "新疆维吾尔自治区",
            "shortName": "新疆",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "650100",
                    "name": "乌鲁木齐市",
                    "shortName": "乌鲁木齐",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "650200",
                    "name": "克拉玛依市",
                    "shortName": "克拉玛依",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652100",
                    "name": "吐鲁番地区",
                    "shortName": "吐鲁番",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652200",
                    "name": "哈密地区",
                    "shortName": "哈密",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652300",
                    "name": "昌吉回族自治州",
                    "shortName": "昌吉",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652700",
                    "name": "博尔塔拉蒙古自治州",
                    "shortName": "博尔塔拉",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652800",
                    "name": "巴音郭楞蒙古自治州",
                    "shortName": "巴音郭楞",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "652900",
                    "name": "阿克苏地区",
                    "shortName": "阿克苏",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "653000",
                    "name": "克孜勒苏柯尔克孜自治州",
                    "shortName": "克孜勒苏",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "653100",
                    "name": "喀什地区",
                    "shortName": "喀什",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "653200",
                    "name": "和田地区",
                    "shortName": "和田",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "654000",
                    "name": "伊犁哈萨克自治州",
                    "shortName": "伊犁",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "654200",
                    "name": "塔城地区",
                    "shortName": "塔城",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "654300",
                    "name": "阿勒泰地区",
                    "shortName": "阿勒泰",
                    "parentId": "650000",
                    "level": "2"
                },
                {
                    "value": "659000",
                    "name": "直辖县级",
                    "shortName": "直辖县",
                    "parentId": "650000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "710000",
            "name": "台湾省",
            "shortName": "台湾",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "710100",
                    "name": "台北市",
                    "shortName": "台北",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710200",
                    "name": "高雄市",
                    "shortName": "高雄",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710300",
                    "name": "基隆市",
                    "shortName": "基隆",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710400",
                    "name": "台中市",
                    "shortName": "台中",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710500",
                    "name": "台南市",
                    "shortName": "台南",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710600",
                    "name": "新竹市",
                    "shortName": "新竹",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710700",
                    "name": "嘉义市",
                    "shortName": "嘉义",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "710800",
                    "name": "新北市",
                    "shortName": "新北",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712200",
                    "name": "宜兰县",
                    "shortName": "宜兰",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712300",
                    "name": "桃园县",
                    "shortName": "桃园",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712400",
                    "name": "新竹县",
                    "shortName": "新竹",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712500",
                    "name": "苗栗县",
                    "shortName": "苗栗",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712700",
                    "name": "彰化县",
                    "shortName": "彰化",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712800",
                    "name": "南投县",
                    "shortName": "南投",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "712900",
                    "name": "云林县",
                    "shortName": "云林",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713000",
                    "name": "嘉义县",
                    "shortName": "嘉义",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713300",
                    "name": "屏东县",
                    "shortName": "屏东",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713400",
                    "name": "台东县",
                    "shortName": "台东",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713500",
                    "name": "花莲县",
                    "shortName": "花莲",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713600",
                    "name": "澎湖县",
                    "shortName": "澎湖",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713700",
                    "name": "金门县",
                    "shortName": "金门",
                    "parentId": "710000",
                    "level": "2"
                },
                {
                    "value": "713800",
                    "name": "连江县",
                    "shortName": "连江",
                    "parentId": "710000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "810000",
            "name": "香港特别行政区",
            "shortName": "香港",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "810100",
                    "name": "香港岛",
                    "shortName": "香港岛",
                    "parentId": "810000",
                    "level": "2"
                },
                {
                    "value": "810200",
                    "name": "九龙",
                    "shortName": "九龙",
                    "parentId": "810000",
                    "level": "2"
                },
                {
                    "value": "810300",
                    "name": "新界",
                    "shortName": "新界",
                    "parentId": "810000",
                    "level": "2"
                }
            ]
        },
        {
            "value": "820000",
            "name": "澳门特别行政区",
            "shortName": "澳门",
            "parentId": "100000",
            "level": "1",
            "child": [
                {
                    "value": "820100",
                    "name": "澳门半岛",
                    "shortName": "澳门半岛",
                    "parentId": "820000",
                    "level": "2"
                },
                {
                    "value": "820200",
                    "name": "氹仔岛",
                    "shortName": "氹仔岛",
                    "parentId": "820000",
                    "level": "2"
                },
                {
                    "value": "820300",
                    "name": "路环岛",
                    "shortName": "路环岛",
                    "parentId": "820000",
                    "level": "2"
                }
            ]
        }
    ];
    // 二级联动
    $('#bankAdd').mPicker({
        level: 2,
        dataJson: dataJson,
        Linkage: true,
        rows: 6,
        idDefault: true,
        splitStr: '-',
        //header:'<div class="mPicker-header">两级联动选择插件</div>',
        footer: "",//'<div class="mPicker-header"></div>',
        header: '<div class="mPicker-footer"><a href="javascript:;" class="mPicker-cancel">取消</a><a href="javascript:;" class="mPicker-confirm">确定</a></div>',
        confirm: function () {
            //更新json
            // this.container.data('mPicker').updateData(level3);
            //console.info($('.select-value').data('value1')+'-'+$('.select-value').data('value2'));
        },
        cancel: function () {
            //console.info($('.select-value').data('value1')+'-'+$('.select-value').data('value2'));
        }
    })
});