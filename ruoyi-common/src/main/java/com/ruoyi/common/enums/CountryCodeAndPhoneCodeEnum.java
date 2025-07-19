package com.ruoyi.common.enums;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.ChineseCharacterUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
/**
 * 国家区号 和 手机区号 枚举
 * <p>
 * todo: 需要放到后台 vchatAll 中统一标准
 */
@Getter
@AllArgsConstructor
@Deprecated
public enum CountryCodeAndPhoneCodeEnum {
        AFGHANISTAN("Afghanistan", "阿富汗", "AF", "93"),
        ALASKA("Alaska", "阿拉斯加", "US", "1907"),
        ALBANIA("Albania", "阿尔巴尼亚", "AL", "355"),
        ALGERIA("Algeria", "阿尔及利亚", "DZ", "213"),
        AMERICAN_SAMOA("American Samoa", "美属萨摩亚", "AS", "1684"),
        ANDORRA("Andorra", "安道尔", "AD", "376"),
        ANGOLA("Angola", "安哥拉", "AO", "244"),
        ANGUILLA("Anguilla", "安圭拉", "AI", "1264"),
        ANTIGUA_AND_BARBUDA("Antigua and Barbuda", "安提瓜和巴布达", "AG", "1268"),
        ARGENTINA("Argentina", "阿根廷", "AR", "54"),
        ARMENIA("Armenia", "亚美尼亚", "AM", "374"),
        ARUBA("Aruba", "阿鲁巴", "AW", "297"),
        ASCENSION("Ascension", "阿森松", "SH", "247"),
        AUSTRALIA("Australia", "澳大利亚", "AU", "61"),
        AUSTRIA("Austria", "奥地利", "AT", "43"),
        AZERBAIJAN("Azerbaijan", "阿塞拜疆", "AZ", "994"),
        BAHAMAS("Bahamas", "巴哈马", "BS", "1242"),
        BAHRAIN("Bahrain", "巴林", "BH", "973"),
        BANGLADESH("Bangladesh", "孟加拉国", "BD", "880"),
        BARBADOS("Barbados", "巴巴多斯", "BB", "1246"),
        BELARUS("Belarus", "白俄罗斯", "BY", "375"),
        BELGIUM("Belgium", "比利时", "BE", "32"),
        BELIZE("Belize", "伯利兹", "BZ", "501"),
        BENIN("Benin", "贝宁", "BJ", "229"),
        BERMUDA("Bermuda", "百慕大群岛", "BM", "1441"),
        BHUTAN("Bhutan", "不丹", "BT", "975"),
        BOLIVIA("Bolivia", "玻利维亚", "BO", "591"),
        BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "波斯尼亚和黑塞哥维那", "BA", "387"),
        BOTSWANA("Botswana", "博茨瓦纳", "BW", "267"),
        BRAZIL("Brazil", "巴西", "BR", "55"),
        BRUNEI("Brunei", "文莱", "BN", "673"),
        BULGARIA("Bulgaria", "保加利亚", "BG", "359"),
        BURKINA_FASO("Burkina Faso", "布基纳法索", "BF", "226"),
        BURUNDI("Burundi", "布隆迪", "BI", "257"),
        CAMBODIA("Cambodia", "柬埔寨", "KH", "855"),
        CAMEROON("Cameroon", "喀麦隆", "CM", "237"),
        CANADA("Canada", "加拿大", "CA", "1"),
        ISLAS_CANARIAS("Islas Canarias", "加那利群岛", "ES", "34"),
        CAPE_VERDE("Cape Verde", "开普", "CV", "238"),
        CAYMAN_ISLANDS("Cayman Islands", "开曼群岛", "KY", "1345"),
        CENTRAL_AFRICAN_REPUBLIC("Central African Republic", "中非共和国", "CF", "236"),
        CHAD("Chad", "乍得", "TD", "235"),
        CHINA("China", "中国", "CN", "86"),
        CHILE("Chile", "智利", "CL", "56"),
        /* CHRISTMAS_ISLAND("Christmas Island", "圣诞岛", "CX", "0061 9164"),
         COCOS_ISLAND("Cocos Island", "科科斯岛", "CC", "0061 9162"),*/
        COLOMBIA("Colombia", "哥伦比亚", "CO", "57"),
        DOMINICAN_REPUBLIC("Dominican Republic", "多米尼加共和国", "DO", "1809"),
        COMOROS("Comoros", "科摩罗", "KM", "269"),
        REPUBLIC_OF_THE_CONGO("Republic Of The Congo", "刚果共和国", "CG", "242"),
        COOK_ISLANDS("Cook Islands", "库克群岛", "CK", "682"),
        COSTA_RICA("Costa Rica", "哥斯达黎加", "CR", "506"),
        CROATIA("Croatia", "克罗地亚", "HR", "385"),
        CUBA("Cuba", "古巴", "CU", "53"),
        CURACAO("Curacao", "库拉索", "CW", "599"),
        CYPRUS("Cyprus", "塞浦路斯", "CY", "357"),
        CZECH("Czech", "捷克", "CZ", "420"),
        DENMARK("Denmark", "丹麦", "DK", "45"),
        DJIBOUTI("Djibouti", "吉布提", "DJ", "253"),
        DOMINICA("Dominica", "多米尼加", "DM", "1767"),
        ECUADOR("Ecuador", "厄瓜多尔", "EC", "593"),
        EGYPT("Egypt", "埃及", "EG", "20"),
        EL_SALVADOR("El Salvador", "萨尔瓦多", "SV", "503"),
        EQUATORIAL_GUINEA("Equatorial Guinea", "赤道几内亚", "GQ", "240"),
        ERITREA("Eritrea", "厄立特里亚", "ER", "291"),
        ESTONIA("Estonia", "爱沙尼亚", "EE", "372"),
        ETHIOPIA("Ethiopia", "埃塞俄比亚", "ET", "251"),
        FALKLAND_ISLANDS("Falkland Islands", "福克兰群岛", "FK", "500"),
        FAROE_ISLANDS("Faroe Islands", "法罗群岛", "FO", "298"),
        FIJI("Fiji", "斐济", "FJ", "679"),
        FINLAND("Finland", "芬兰", "FI", "358"),
        FRANCE("France", "法国", "FR", "33"),
        FRENCH_GUIANA("French Guiana", "法属圭亚那", "GF", "594"),
        FRENCH_POLYNESIA("French Polynesia", "法属波利尼西亚", "PF", "689"),
        GABON("Gabon", "加蓬", "GA", "241"),
        GAMBIA("Gambia", "冈比亚", "GM", "220"),
        GEORGIA("Georgia", "格鲁吉亚", "GE", "995"),
        GERMANY("Germany", "德国", "DE", "49"),
        GHANA("Ghana", "加纳", "GH", "233"),
        GIBRALTAR("Gibraltar", "直布罗陀", "GI", "350"),
        GREECE("Greece", "希腊", "GR", "30"),
        GREENLAND("Greenland", "格陵兰岛", "GL", "299"),
        GRENADA("Grenada", "格林纳达", "GD", "1473"),
        GUADELOUPE("Guadeloupe", "瓜德罗普岛", "GP", "590"),
        GUAM("Guam", "关岛", "GU", "1671"),
        GUATEMALA("Guatemala", "瓜地马拉", "GT", "502"),
        GUINEA("Guinea", "几内亚", "GN", "224"),
        GUINEA_BISSAU("Guinea-Bissau", "几内亚比绍共和国", "GW", "245"),
        GUYANA("Guyana", "圭亚那", "GY", "592"),
        HAITI("Haiti", "海地", "HT", "509"),
        HAWAII("Hawaii", "夏威夷", "US", "1808"),
        HONDURAS("Honduras", "洪都拉斯", "HN", "504"),
        HONG_KONG("Hong Kong", "中国香港", "HK", "852"),
        HUNGARY("Hungary", "匈牙利", "HU", "36"),
        ICELAND("Iceland", "冰岛", "IS", "354"),
        INDIA("India", "印度", "IN", "91"),
        INDONESIA("Indonesia", "印度尼西亚", "ID", "62"),
        IRAN("Iran", "伊朗", "IR", "98"),
        IRAQ("Iraq", "伊拉克", "IQ", "964"),
        IRELAND("Ireland", "爱尔兰", "IE", "353"),
        ISRAEL("Israel", "以色列", "IL", "972"),
        ITALY("Italy", "意大利", "IT", "39"),
        IVORY_COAST("Ivory Coast", "象牙海岸", "CI", "225"),
        JAMAICA("Jamaica", "牙买加", "JM", "1876"),
        JAPAN("Japan", "日本", "JP", "81"),
        JORDAN("Jordan", "约旦", "JO", "962"),
        KAZAKHSTAN("Kazakhstan", "哈萨克斯坦", "KZ", "7"),
        KENYA("Kenya", "肯尼亚", "KE", "254"),
        KIRIBATI("Kiribati", "基里巴斯", "KI", "686"),
        KOREA_DEMOCRATIC_REP("Korea Democratic Rep.", "朝鲜", "KP", "85"),
        SOUTH_KOREA("South Korea", "韩国", "KR", "82"),
        KUWAIT("Kuwait", "科威特", "KW", "965"),
        KYRGYZSTAN("Kyrgyzstan", "吉尔吉斯斯坦", "KG", "996"),
        LAOS("Laos", "老挝", "LA", "856"),
        LATVIA("Latvia", "拉脱维亚", "LV", "371"),
        LEBANON("Lebanon", "黎巴嫩", "LB", "961"),
        LESOTHO("Lesotho", "莱索托", "LS", "266"),
        LIBERIA("Liberia", "利比里亚", "LR", "231"),
        LIBYA("Libya", "利比亚", "LY", "218"),
        LIECHTENSTEIN("Liechtenstein", "列支敦士登", "LI", "423"),
        LITHUANIA("Lithuania", "立陶宛", "LT", "370"),
        LUXEMBOURG("Luxembourg", "卢森堡", "LU", "352"),
        MACAU("Macau", "中国澳门", "MO", "853"),
        MACEDONIA("Macedonia", "马其顿", "MK", "389"),
        MADAGASCAR("Madagascar", "马达加斯加", "MG", "261"),
        MALAWI("Malawi", "马拉维", "MW", "265"),
        MALAYSIA("Malaysia", "马来西亚", "MY", "60"),
        MALDIVES("Maldives", "马尔代夫", "MV", "960"),
        MALI("Mali", "马里", "ML", "223"),
        MALTA("Malta", "马耳他", "MT", "356"),
        MARSHALL_ISLANDS("Marshall Islands", "马绍尔群岛", "MH", "692"),
        MARTINIQUE("Martinique", "马提尼克", "MQ", "596"),
        MAURITANIA("Mauritania", "毛里塔尼亚", "MR", "222"),
        MAURITIUS("Mauritius", "毛里求斯", "MU", "230"),
        MAYOTTE("Mayotte", "马约特", "YT", "269"),
        MEXICO("Mexico", "墨西哥", "MX", "52"),
        MICRONESIA("Micronesia", "密克罗尼西亚", "FM", "691"),
        MOLDOVA("Moldova", "摩尔多瓦", "MD", "373"),
        MONACO("Monaco", "摩纳哥", "MC", "377"),
        MONGOLIA("Mongolia", "蒙古", "MN", "976"),
        MONTENEGRO("Montenegro", "黑山", "ME", "382"),
        MONTSERRAT("Montserrat", "蒙特塞拉特岛", "MS", "1664"),
        MOROCCO("Morocco", "摩洛哥", "MA", "212"),
        MOZAMBIQUE("Mozambique", "莫桑比克", "MZ", "258"),
        MYANMAR("Myanmar", "缅甸", "MM", "95"),
        NAMIBIA("Namibia", "纳米比亚", "NA", "264"),
        NAURU("Nauru", "拿鲁岛", "NR", "674"),
        NEPAL("Nepal", "尼泊尔", "NP", "977"),
        NETHERLANDS("Netherlands", "荷兰", "NL", "31"),
        NEW_CALEDONIA("New Caledonia", "新喀里多尼亚", "NC", "687"),
        NEW_ZEALAND("New Zealand", "新西兰", "NZ", "64"),
        NICARAGUA("Nicaragua", "尼加拉瓜", "NI", "505"),
        NIGER("Niger", "尼日尔", "NE", "227"),
        NIGERIA("Nigeria", "尼日利亚", "NG", "234"),
        NIUE_ISLAND("Niue Island", "纽埃岛(新)", "NU", "683"),
        NORFOLK_ISLAND("Norfolk Island", "诺福克岛(澳)", "NF", "6723"),
        NORWAY("Norway", "挪威", "NO", "47"),
        OMAN("Oman", "阿曼", "OM", "968"),
        PALAU("Palau", "帕劳", "PW", "680"),
        PANAMA("Panama", "巴拿马", "PA", "507"),
        PAPUA_NEW_GUINEA("Papua New Guinea", "巴布亚新几内亚", "PG", "675"),
        PARAGUAY("Paraguay", "巴拉圭", "PY", "595"),
        PERU("Peru", "秘鲁", "PE", "51"),
        PHILIPPINES("Philippines", "菲律宾", "PH", "63"),
        POLAND("Poland", "波兰", "PL", "48"),
        PORTUGAL("Portugal", "葡萄牙", "PT", "351"),
        PAKISTAN("Pakistan", "巴基斯坦", "PK", "92"),
        PUERTO_RICO("Puerto Rico", "波多黎各", "PR", "1787"),
        QATAR("Qatar", "卡塔尔", "QA", "974"),
        RÉUNION_ISLAND("Réunion Island", "留尼汪", "RE", "262"),
        ROMANIA("Romania", "罗马尼亚", "RO", "40"),
        RUSSIA("Russia", "俄罗斯", "RU", "7"),
        RWANDA("Rwanda", "卢旺达", "RW", "250"),
        SAMOA_EASTERN("Samoa,Eastern", "东萨摩亚(美)", "AS", "684"),
        SAMOA("Samoa", "萨摩亚", "WS", "685"),
        SAN_MARINO("San Marino", "圣马力诺", "SM", "378"),
        SAINT_PIERRE_AND_MIQUELON("Saint Pierre and Miquelon", "圣彼埃尔和密克隆岛", "PM", "508"),
        SAO_TOME_AND_PRINCIPE("Sao Tome and Principe", "圣多美和普林西比", "ST", "239"),
        SAUDI_ARABIA("Saudi Arabia", "沙特阿拉伯", "SA", "966"),
        SENEGAL("Senegal", "塞内加尔", "SN", "221"),
        SERBIA("Serbia", "塞尔维亚", "RS", "381"),
        SEYCHELLES("Seychelles", "塞舌尔", "SC", "248"),
        SIERRA_LEONE("Sierra Leone", "塞拉利昂", "SL", "232"),
        SINGAPORE("Singapore", "新加坡", "SG", "65"),
        SAINT_MAARTEN_DUTCH_PART("Saint Maarten (Dutch Part)", "圣马丁岛（荷兰部分）", "SX", "1721"),
        SLOVAKIA("Slovakia", "斯洛伐克", "SK", "421"),
        SLOVENIA("Slovenia", "斯洛文尼亚", "SI", "386"),
        SOLOMON_ISLANDS("Solomon Islands", "所罗门群岛", "SB", "677"),
        SOMALIA("Somalia", "索马里", "SO", "252"),
        SOUTH_AFRICA("South Africa", "南非", "ZA", "27"),
        SPAIN("Spain", "西班牙", "ES", "34"),
        SRI_LANKA("Sri Lanka", "斯里兰卡", "LK", "94"),
        ST_HELENA("St.Helena", "圣赫勒拿", "SH", "290"),
        SAINT_LUCIA("Saint Lucia", "圣露西亚", "LC", "1758"),
        SAINT_VINCENT_AND_THE_GRENADINES("Saint Vincent and The Grenadines", "圣文森特和格林纳丁斯", "VC", "1784"),
        SUDAN("Sudan", "苏丹", "SD", "249"),
        SURINAME("Suriname", "苏里南", "SR", "597"),
        SWAZILAND("Swaziland", "斯威士兰", "SZ", "268"),
        SWEDEN("Sweden", "瑞典", "SE", "46"),
        SWITZERLAND("Switzerland", "瑞士", "CH", "41"),
        SYRIA("Syria", "叙利亚", "SY", "963"),
        TAIWAN("Taiwan", "中国台湾", "TW", "886"),
        TAJIKISTAN("Tajikistan", "塔吉克斯坦", "TJ", "992"),
        TANZANIA("Tanzania", "坦桑尼亚", "TZ", "255"),
        THAILAND("Thailand", "泰国", "TH", "66"),
        TIMOR_LESTE("Timor-Leste", "东帝汶", "TL", "670"),
        UNITED_ARAB_EMIRATES("United Arab Emirates", "阿拉伯联合酋长国", "AE", "971"),
        TOGO("Togo", "多哥", "TG", "228"),
        TOKELAU_IS("Tokelau Is.", "托克劳群岛(新)", "TK", "690"),
        TONGA("Tonga", "汤加", "TO", "676"),
        TRINIDAD_AND_TOBAGO("Trinidad and Tobago", "特立尼达和多巴哥", "TT", "1868"),
        TUNISIA("Tunisia", "突尼斯", "TN", "216"),
        TURKEY("Turkey", "土耳其", "TR", "90"),
        TURKMENISTAN("Turkmenistan", "土库曼斯坦", "TM", "993"),
        TURKS_AND_CAICOS_ISLANDS("Turks and Caicos Islands", "特克斯和凯科斯群岛", "TC", "1649"),
        TUVALU("Tuvalu", "图瓦卢", "TK", "688"),
        UNITED_STATES("United States", "美国", "US", "1"),
        UGANDA("Uganda", "乌干达", "UG", "256"),
        UKRAINE("Ukraine", "乌克兰", "UA", "380"),
        UNITED_KINGDOM("United Kingdom", "英国", "GB", "44"),
        URUGUAY("Uruguay", "乌拉圭", "UY", "598"),
        UZBEKISTAN("Uzbekistan", "乌兹别克斯坦", "UZ", "998"),
        VANUATU("Vanuatu", "瓦努阿图", "VU", "678"),
        VENEZUELA("Venezuela", "委内瑞拉", "VE", "58"),
        VIETNAM("Vietnam", "越南", "VN", "84"),
        VIRGIN_ISLANDS_BRITISH("Virgin Islands, British", "英属处女群岛", "VG", "1340"),
        VIRGIN_ISLANDS_US("Virgin Islands, US", "美属维尔京群岛", "VI", "1284"),
        WAKE_I("Wake I.", "威克岛(美)", "UM", "1808"),
        YEMEN("Yemen", "也门", "YE", "967"),
        ZAMBIA("Zambia", "赞比亚", "ZM", "260"),
        ZANZIBAR("Zanzibar", "桑给巴尔", "TZ", "259"),
        ZIMBABWE("Zimbabwe", "津巴布韦", "ZW", "263"),
        ;
        private String englishName;
        private String chineseName;
        private String countryCode;
        private String phoneCode;


        public static List<Map> getJsonArray() {
            StringBuilder str = new StringBuilder();
            str.append("[");
            for (CountryCodeAndPhoneCodeEnum anEnum : CountryCodeAndPhoneCodeEnum.values()) {
                str.append("{")
                        .append("\"englishName\":\"" + anEnum.getEnglishName() + "\",")
                        .append("\"chineseName\":\"" + anEnum.getChineseName() + "\",")
                        .append("\"countryCode\":\"" + anEnum.getCountryCode() + "\",")
                        .append("\"phoneCode\":\"" + anEnum.getPhoneCode() + "\"")
                        .append("},");
            }
            str.deleteCharAt(str.length() - 1);
            str.append("]");
            JSONArray jsonArr = JSONUtil.parseArray(str.toString());
            List<Map> maps = jsonArr.toList(Map.class);

            ArrayList result = countrySort(maps);
            return result;
        }

        private static ArrayList countrySort(List<Map> maps) {
            ArrayList result = new ArrayList();
            for (char c = 'A'; c <= 'Z'; ++c) {
                List data = new ArrayList<>();
                for (Map map : maps) {
                    Set<Map.Entry<String, String>> set = map.entrySet();

                    for (Map.Entry<String, String> entry : set) {
                        String key = entry.getKey();
                        if ("englishName".equals(key)) {
                            String value = entry.getValue();
                            String s = ChineseCharacterUtil.convertHanzi2Pinyin(value, false);
                            if ((c + "").equalsIgnoreCase(s.charAt(0) + "")) {
                                data.add(map);
                            }
                        }
                    }
                }

                if (data.size() > 0) {
                    Map item = new HashMap<>();
                    item.put("letter", c + "");
                    item.put("data", data);

                    result.add(item);
                }
            }
            return result;
        }

    }