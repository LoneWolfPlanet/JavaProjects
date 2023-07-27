package com.dyntrace.download.metric;

import com.dyntrace.download.metric.util.CSVUtil;
import com.dyntrace.download.metric.util.DynatraceDataExpUtil;
import com.dyntrace.download.metric.util.FileType;
import com.dyntrace.download.metric.util.S3Util;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DynatraceDataExpUtil util = new DynatraceDataExpUtil();
		util.setURLAPI("https://vcf56488.live.dynatrace.com/api/v2/metrics/query?metricSelector=(builtin:host.cpu.usage:splitBy():sort(value(auto,descending)):limit(20)):limit(100):names&from=1690337520000&to=1690348320000");
		String token = "dt0c01.NEL75IBI24T4M64NHSJJHCVH.YRAAPBYHHQS24DWQMWTL26CGXBGP5UAGA2VQ6RDVEZOTQA2H6NIU5WY5V5ADFWMX";
		boolean result = util.download(token, FileType.CSV);
		
		CSVUtil csv = new CSVUtil();
		StringBuilder builder = csv.format(util.getData(),util.getResponseSize());
		S3Util s3 = new S3Util();
		String role = "arn:aws:iam::488861720237:role/log-management-role";
		result = s3.getRole(role);
		s3.setRegion("ap-northeast-1");
		result = s3.authenticate();
		result=s3.saveToS3(builder.toString(), "sample.csv", "exploration-quicksight-log", "dynatrace/");
		
	}

}
