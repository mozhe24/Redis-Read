/**
 * 
 */
package ReadRedis;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip操作类
 * 
 * @author zlyan
 * @time 2017年3月24日-上午10:33:32
 */
public class IPUnit {

	/**
	 * 验证IP是否属于某个IP段 ipSection IP段（以'-'分隔） ip 所验证的IP号码
	 *
	 */
	public static boolean ipExistsInRange(String ip, String ipSection) {
		ipSection = ipSection.trim();
		ip = ip.trim();
		int idx = ipSection.indexOf('-');
		String beginIP = ipSection.substring(0, idx);
		String endIP = ipSection.substring(idx + 1);
		return Long.valueOf(beginIP) <= getIp2long(ip) && getIp2long(ip) <= Long.valueOf(endIP);
	}

	public static long getIp2long(String ip) {
		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip2long = 0L;
		for (int i = 0; i < 4; ++i) {
			ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
		}
		return ip2long;
	}

	public static long getIp2long2(String ip) {
		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip1 = Integer.parseInt(ips[0]);
		long ip2 = Integer.parseInt(ips[1]);
		long ip3 = Integer.parseInt(ips[2]);
		long ip4 = Integer.parseInt(ips[3]);
		long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;
		return ip2long;
	}

	/**
	 * 从IP段集合中根据IP获取IP段
	 * 
	 * @param ip
	 * @param ipSet
	 * @return
	 */
	public static String getIpRangeByIpFromIpRangeSet(String ip, Set<String> ipRangeSet) {
		if (StringUtils.isNotBlank(ip) && ipRangeSet != null && ipRangeSet.size() > 0) {
			for (String ipRange : ipRangeSet) {
				if (ipExistsInRange(ip, ipRange)) {
					return ipRange;
				}
			}
		}
		return "";
	}

	/**
	 * 根据0.0.0.0结构的ip获取IP范围
	 * 
	 * @param ip
	 * @return 1279303-1234343 为空时返回"";
	 */
	public static String getIpRangeByIp(String ip) {
		long ipLong = getIp2long(ip);
		List<Long> ipBeginList = FileInfoCache.getIpBeginList();
		List<Long> ipEndList = FileInfoCache.getIpEndList();
		//同时满足IP不为空，
		//IP范围开始结束list都不为空且大小相同
		//IP值大于等于搜索范围最小值 且小于等于搜索范围最大值
		if (StringUtils.isNotBlank(ip) && ipBeginList != null && ipBeginList.size() > 0
				&& ipBeginList.size() == ipEndList.size() && ipLong >= ipBeginList.get(0)
				&& ipLong <= ipEndList.get(ipEndList.size()-1)) {
			int start = 0;
			int end = ipBeginList.size();
			int mid = 0;
			// 二分查找
			while (end >= start) {
				mid = (end + start) / 2;
				Long bgip = ipBeginList.get(mid);
				Long enip = ipEndList.get(mid);
				// 当大于等于开始时间并且小于等于结束时间，返回bgip+"-"+enip
				if (bgip <= ipLong && ipLong <= enip) {
					return bgip + "-" + enip;
				} else if (enip < ipLong) {
					start = mid + 1;
				} else {
					end = mid - 1;
				}
			}
		}
		return "";
	}

	/**
	 * 判断 IP 是否符合要求
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		return mat.matches();
	}

	public static void main(String[] args) {
		// 10.10.10.116 是否属于固定格式的IP段10.10.1.00-10.10.255.255
		String ip = "10.10.10.116";
		// String ipSection = "10.10.1.00-10.10.255.255";
		// boolean exists = ipExistsInRange(ip, ipSection);
		// System.out.println(exists);
		System.out.println(getIp2long(ip));
		System.out.println(getIp2long2(ip));
		System.out.println(isIP("1.0.0.1"));
		List<Long> ipBeginList = new ArrayList<Long>();
		ipBeginList.add(1L);
		// ipBeginList.add(101L);
		List<Long> ipEndList = new ArrayList<Long>();
		ipEndList.add(100L);
		// ipEndList.add(200L);
		FileInfoCache.setIpBeginList(ipBeginList);
		FileInfoCache.setIpEndList(ipEndList);
		System.out.println(getIpRangeByIp("-90"));
	}

}
