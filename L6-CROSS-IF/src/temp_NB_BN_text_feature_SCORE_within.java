
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Range;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.AddID;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

// This file will be used to ensemble based prediction using stacking of algorithms
public class temp_NB_BN_text_feature_SCORE_within
{



/*
String path = "E:\\Sangeeta\\Research\\";
String user_name =  "sangeetal";
String password = "sangeetal";
String url = "jdbc:mysql://localhost:3307/";
String driver = "com.mysql.jdbc.Driver"; 
 
// */

///*
String path = "F:\\Research\\";
String user_name =  "root";
String password = "1234";
String url = "jdbc:mysql://localhost:3306/";
String driver = "com.mysql.jdbc.Driver";
//*/


//String type = "catch";
String type = "if";

int iterations=1;
String source_project="tomcat";
//String source_project = "cloudstack";
//String source_project="hd";


String db_name ="logging6_crossif";
String result_table = source_project+"_"+type+"_training_nb_bn_score";  //score will be generated and updated in the target table using source table
String source_file_path = path+"L6-CROSS-IF\\dataset\\"+source_project+"-arff\\"+type+"\\"+source_project+"_"+type+"_text_features_with_id.arff";		

int if_ids[];

DataSource trainsource;
DataSource testsource;
Instances trains;
Instances tests;
Evaluation result;

int instance_count_source = 0;
int instance_count_target =0;
Connection conn=null;	
java.sql.Statement stmt = null;
 DataSource allsource;
Instances all_data;


//This function uses dataset from the ARFF files
public void read_file()
{ 
try 
	{
	
	
		allsource = new DataSource(source_file_path);
		all_data= allsource.getDataSet();	
		
		all_data.randomize(new java.util.Random(1));
		
		all_data.setClassIndex(0); //  new line
		
		int trainSize = (int) Math.round(all_data.numInstances() * 0.5);
		int testSize = all_data.numInstances() - trainSize;
		
		trains = new Instances(all_data, 0, trainSize);
		tests = new Instances(all_data, trainSize, testSize);
		
		
		/* AddID addId = new AddID();
		addId.setInputFormat(all_data); 
		Instances all_data_with_id = Filter.useFilter(all_data, addId);
		all_data_with_id.randomize(new java.util.Random(1));			   
		int trainSize = (int) Math.round(all_data_with_id.numInstances() * 0.5);
		int testSize = all_data_with_id.numInstances() - trainSize;		
		trains = new Instances(all_data_with_id, 0, trainSize);
		tests = new Instances(all_data_with_id, trainSize, testSize);*/
		
		trains.setClassIndex(0);		
		tests.setClassIndex(0);
				
		instance_count_source = trains.numInstances();
		instance_count_target = tests.numInstances();
		
		System.out.println("Instance count source ="+ instance_count_source + "  Instance count target="+ instance_count_target+  source_file_path);
   
	} catch (Exception e) 
	{
	
		e.printStackTrace();
	}	  
	
}


//This function is used to pre-process the dataset
public void pre_process_data()
{

 try
   {	  
	  //1. TF-IDF
	 StringToWordVector tfidf_filter = new StringToWordVector();
	 tfidf_filter.setIDFTransform(true);
	 tfidf_filter.setInputFormat(trains);
	 trains = Filter.useFilter(trains, tfidf_filter);     	  
	
	 tests = Filter.useFilter(tests, tfidf_filter);
 
	 // If you are using cross-validation
	 /* StringToWordVector tfidf_filter = new StringToWordVector();
	  tfidf_filter.setIDFTransform(true);
	  tfidf_filter.setInputFormat(all_data);
	  all_data = Filter.useFilter(all_data, tfidf_filter);  */  	  
	
     
	  //2. Add remove ID filter
	 // http://weka.8497.n7.nabble.com/How-to-map-original-dataset-with-ID-attribute-and-trained-dataset-without-ID-attribute-td27360.html
	  

		/*Remove rm = new Remove();
		rm.setAttributeIndices("2");
		rm.setInputFormat(trains);
		trains = Filter.useFilter(trains, rm);     	  
		
		tests = Filter.useFilter(tests, rm);*/
	 
		 
	  //  FilteredClassifier fc = new FilteredClassifier();
	   // fc.setFilter(rm);
	    //fc.setClassifier(m1);
	  
	  
	  /*

     //3. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	  Standardize  std_filter =  new Standardize();
	  std_filter.setInputFormat(trains);
	  trains= Filter.useFilter(trains,std_filter);     	  
	 
	  tests= Filter.useFilter(tests,std_filter);  	  
     
     //4. Discretizations
	  Discretize dfilter = new Discretize();
     dfilter.setInputFormat(trains);
     trains = Filter.useFilter(trains, dfilter);
     
     tests = Filter.useFilter(tests, dfilter);	      
     */


	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

//This function is used to train and test a using a given classifier
public void generate_nb_bn_score(Classifier m1) 
{

    read_file();
	pre_process_data();
	
	Evaluation evaluation = null;
	
	//NaiveBayes  m1 =  new NaiveBayes();
	System.out.println("Classifier="+m1.getClass().getName());

	try
	{
	
		Remove rm = new Remove();
	    rm.setAttributeIndices("2");
	    FilteredClassifier fc = new FilteredClassifier();
	    fc.setFilter(rm);
	    fc.setClassifier(m1);		
		//m1.buildClassifier(all_data);
		
	    m1.buildClassifier(trains);
	    evaluation= new Evaluation(trains);
	    evaluation.evaluateModel(m1, tests);
	    
		//Evaluation eval = new Evaluation(all_data);        
	 //   StringBuffer forPredictionsPrinting = new StringBuffer();      
	  //  Range attsToOutput = new Range("first-last");
	   // Boolean outputDist = new Boolean(true);

	    // eval.crossValidateModel(fc, all_data, 2, new Random(1), forPredictionsPrinting, attsToOutput, outputDist);
	    // System.out.println(eval.toSummaryString());
	    // System.out.println(forPredictionsPrinting + " " + attsToOutput+ " "+ outputDist);


		conn = initdb(db_name);
		if(conn==null)
			{
				System.out.println(" Databasse connection is null");
	
			} 
 
		else
		{
			try 
				{

				for (int j = 0; j < tests.numInstances(); j++) 
					{
    
						double score[] ;
						Instance curr  =  tests.instance(j);  
						double actual = curr.classValue();

 
						score= m1.distributionForInstance(curr);
	
						String update_score = "update "+ result_table +"  set "+ source_project+ "_to_"+source_project+"_nb_score=" +score[1];
						System.out.println("update="+ j+ "  ID="+ tests.instance(j).value(1) + "  score="+ score[1]);
	
						java.sql.Statement stmt = conn.createStatement();
						stmt.executeUpdate(update_score);

					}//for
		
				stmt.close();
				conn.close();
		
				} catch (SQLException e) 
				{
		
					e.printStackTrace();
				}

           }// else


		} catch (Exception e) 
		{

			e.printStackTrace();
			}
		

}

public Connection initdb(String db_name)
{
try {
	      Class.forName(driver).newInstance();
	      conn = DriverManager.getConnection(url+db_name,user_name,password);
	      //System.out.println(" dbname="+ db_name+ "user name"+ userName+ " password="+ password);
	      if(conn==null)
	      {
	    	  System.out.println(" Database connection is null. Check it.");
	      }
	      
	 } catch (Exception e) 
	 {
	      e.printStackTrace();
	 }
	return conn;
}


//generate fids
private void get_if_ids()
{
	
	
	Connection conn1 = initdb(db_name);
	if(conn1==null)
	{
		System.out.println(" Databasse connection is null");
		
	}
	
	String row_count = "select count(*) from " + result_table;
	String get_str = "select if_id from "+  result_table;
	
	try 
	{
		java.sql.Statement stmt1 = conn1.createStatement();
		
		ResultSet rs = stmt1.executeQuery(row_count);
		while(rs.next())
		{
			int no_of_id = rs.getInt(1);
			if_ids  =   new int[no_of_id];
			
			
			System.out.println(" no of id="+no_of_id);
		}
				
		rs = stmt1.executeQuery(get_str);
		
	    int i=0;
		while(rs.next())
		{
			int id = rs.getInt(1);
			if_ids[i]  =  id;
			i++;
			
			System.out.println(" id="+id);
		}
		
		stmt1.close();
		conn1.close();
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
}

//This is the main function
public static void main(String args[])
{	  	

	  temp_NB_BN_text_feature_SCORE_within gnbs =  new temp_NB_BN_text_feature_SCORE_within();
	  gnbs.get_if_ids();  
	 
	  gnbs.generate_nb_bn_score(new NaiveBayes());
	 // gnbs.generate_nb_bn_score(new BayesNet());
	    
     }//main	

}// classs




