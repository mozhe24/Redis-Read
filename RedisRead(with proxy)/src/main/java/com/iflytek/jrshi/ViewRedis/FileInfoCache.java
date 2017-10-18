/**
 * 
 */
package com.iflytek.jrshi.ViewRedis;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 地域信息缓存
 * 
 * @author zlyan
 * @time 2017年3月24日-上午10:51:23
 */
public class FileInfoCache {

	/*
	 * 地域代码与地域信息关联map
	 */
	private static Map<String, String[]> codeToAreaMap = null;

	/*
	 * IP段与地域代码关联map
	 */
	private static Map<String, String> ipRangeToCodeMap = null;

	/**
	 * dmp标签代码map
	 */
	private static Map<Integer, String> tagsCodeMap = null;

	/**
	 * ip范围开始值
	 */
	private static List<Long> ipBeginList;

	/**
	 * ip范围结束值
	 */
	private static List<Long> ipEndList;

	/**
	 * 读取DMP标签code文件
	 */
	public static void readTagsInfoFile() {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = FileInfoCache.class.getResourceAsStream("/tagid2sequenceid.txt");
			reader = new BufferedReader(new InputStreamReader(is));
			Map<Integer, String> tagsMap = new HashMap<Integer, String>();
			String str = null;
			String strs[] = null;
			while ((str = reader.readLine()) != null) {
				// str 100820205100, 15
				strs = str.split(",");
				tagsMap.put(Integer.parseInt(strs[1].trim()), strs[0].trim());
			}
			setTagsCodeMap(tagsMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					is = null;
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e3) {
					e3.printStackTrace();
				} finally {
					reader = null;
				}
			}
		}
	}

	/**
	 * 读取地区编码文件
	 */
	public static void readAreaCodeFile() {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = FileInfoCache.class.getResourceAsStream("/area_code.txt");
			reader = new BufferedReader(new InputStreamReader(is));
			Map<String, String[]> codeToAreaMap = new HashMap<String, String[]>();
			String str = null;
			while ((str = reader.readLine()) != null) {
				// str 浙江省,杭州市,1156330100
				String[] areaCodeArr = str.split(",");
				if (areaCodeArr.length == 3) {
					String[] arr = { areaCodeArr[0],areaCodeArr[1] };
					codeToAreaMap.put(areaCodeArr[2], arr);
				}
			}
			setCodeToAreaMap(codeToAreaMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					is = null;
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e3) {
					e3.printStackTrace();
				} finally {
					reader = null;
				}
			}
		}
	}

	/**
	 * 读取ip范围文件
	 */
	public static void readIPRangeInfoFile() {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = FileInfoCache.class.getResourceAsStream("/superadmin.txt");
			reader = new BufferedReader(new InputStreamReader(is));
			Map<String, String> ipRangeToCodeMap = new HashMap<String, String>();
			// ip范围开始值
			List<Long> ipBeginList = new ArrayList<Long>();
			// ip范围结束值
			List<Long> ipEndList = new ArrayList<Long>();
			String str = null;
			while ((str = reader.readLine()) != null) {
				// 1.2.2.0,1.2.2.255,1156110000,0
				String[] ipRangeCodeArr = str.split(",");
				if (ipRangeCodeArr.length >= 3) {
					long ipBegin = IPUnit.getIp2long(ipRangeCodeArr[0]);
					long ipEnd = IPUnit.getIp2long(ipRangeCodeArr[1]);
					String ipRange = ipBegin + "-" + ipEnd;
					ipBeginList.add(ipBegin);
					ipEndList.add(ipEnd);
					ipRangeToCodeMap.put(ipRange, ipRangeCodeArr[2]);
				}
			}
			Collections.sort(ipBeginList);
			Collections.sort(ipEndList);
			setIpBeginList(ipBeginList);
			setIpEndList(ipEndList);
			setIpRangeToCodeMap(ipRangeToCodeMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					is = null;
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e3) {
					e3.printStackTrace();
				} finally {
					reader = null;
				}
			}
		}
	}

	public static Map<String, String[]> getCodeToAreaMap() {
		return codeToAreaMap;
	}

	public static void setCodeToAreaMap(Map<String, String[]> codeToAreaMap) {
		FileInfoCache.codeToAreaMap = codeToAreaMap;
	}

	public static Map<String, String> getIpRangeToCodeMap() {
		return ipRangeToCodeMap;
	}

	public static void setIpRangeToCodeMap(Map<String, String> ipRangeToCodeMap) {
		FileInfoCache.ipRangeToCodeMap = ipRangeToCodeMap;
	}

	public static Map<Integer, String> getTagsCodeMap() {
		readTagsInfoFile();
		return tagsCodeMap;
	}

	public static void setTagsCodeMap(Map<Integer, String> tagsCodeMap) {
		FileInfoCache.tagsCodeMap = tagsCodeMap;
	}

	public static List<Long> getIpBeginList() {
		return ipBeginList;
	}

	public static void setIpBeginList(List<Long> ipBeginList) {
		FileInfoCache.ipBeginList = ipBeginList;
	}

	public static List<Long> getIpEndList() {
		return ipEndList;
	}

	public static void setIpEndList(List<Long> ipEndList) {
		FileInfoCache.ipEndList = ipEndList;
	}

}
