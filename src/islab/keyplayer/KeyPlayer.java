package islab.keyplayer;

import java.math.BigDecimal;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class KeyPlayer {
	public static SparkConf conf = new SparkConf().setAppName("KeyPlayer").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	
	public static void main(String[] args) {
		System.out.println("--------------------------------->>>>>>Ngưỡng sức ảnh hưởng là: " + args[0]);
		Data.theta = new BigDecimal(args[0]);
		System.out.println("--------------------------------->>>>>>Ngưỡng số đỉnh chịu sức ảnh hưởng là: " + args[1]);
		Data.iNeed = Integer.parseInt(args[1]);
		
		// TODO Auto-generated method stub
		long lStart = System.currentTimeMillis();
		Graph g = new Graph();
		
		JavaRDD<String> dataFile = sc.textFile("./graph_data/graph_oneline.json");
		//dataFile.cache();
		JavaRDD<Graph> gRDD = dataFile.map(new Data());
		//gRDD.saveAsTextFile("AAAAAA");
		gRDD.cache();
		g = gRDD.first();
		System.out.println("--------------------------------->>>>>>" + g.toString());
		JavaRDD<List<String>> s = g.getAllPathBetweenTwoVertex("1", "18");
		s.cache();
		System.out.println("--------------------------------->>>>>>Số đường đi: " + s.count());
		System.out.println("--------------------------------->>>>>>" + s.toString());
		System.out.println("--------------------------------->>>>>>Sức ảnh hưởng gián tiếp giữa 2 đỉnh là: " + g.IndirectInfluenceOfVertexOnOtherVertex("1", "18"));
		
		JavaPairRDD<String, BigDecimal> all = g.getAllInfluenceOfVertices();
		
		System.out.println("--------------------------------->>>>>>Sức ảnh hưởng của tất cả các đỉnh:\n" + all.toString());
		
		IndirectInfluence inif = g.getIndirectInfluence();
		System.out.println("--------------------------------->>>>>>Sức ảnh hưởng vượt ngưỡng của tất cả các đỉnh:\n" + inif.toString());
		
		String kp = g.getKeyPlayer();
		List<String> res = g.getSmallestGroup();
		long lEnd = System.currentTimeMillis();
		
		System.out.println("--------------------------------->>>>>>Thời gian tính toán là: " + (lEnd - lStart) + " ms");
		
		System.out.println("--------------------------------->>>>>>Key Player: " + kp.toString());
		
		System.out.println("--------------------------------->>>>>>Nhóm nhỏ nhất thỏa ngưỡng là: " + res);
	}
}
